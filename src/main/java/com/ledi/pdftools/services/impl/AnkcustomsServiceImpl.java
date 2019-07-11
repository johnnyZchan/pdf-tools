package com.ledi.pdftools.services.impl;

import com.alibaba.fastjson.JSONObject;
import com.ledi.pdftools.beans.ank.AnkDownloadResultModel;
import com.ledi.pdftools.beans.ank.ShipListRespModel;
import com.ledi.pdftools.beans.ank.ShipModel;
import com.ledi.pdftools.constants.CodeInfo;
import com.ledi.pdftools.entities.PdfFileEntity;
import com.ledi.pdftools.exceptions.ServiceException;
import com.ledi.pdftools.httpclient.DownloadResult;
import com.ledi.pdftools.httpclient.HttpClientService;
import com.ledi.pdftools.services.AnkcustomsService;
import com.ledi.pdftools.services.PdfFileService;
import com.ledi.pdftools.services.PdfListService;
import com.ledi.pdftools.utils.DataUtil;
import com.ledi.pdftools.utils.RegExpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.util.*;

@Service("ankcustomsService")
@Slf4j
public class AnkcustomsServiceImpl implements AnkcustomsService {

    private static final String loginUrl = "http://www.ankcustoms.com/login.aspx";
    private static final String shipListUrl = "http://www.ankcustoms.com/ShipList.aspx";
    private static final String shipListDataUrl = "http://www.ankcustoms.com/ashx/ReportFileManageOperate.ashx?operatetype=0&page=1&rows=15&token={0}&mawbno=&invoiceno=&DateFrom={1}&DateTo={2}&DateKbn=0&DeclareFlag=0&PermitFlag=0";
    private static final String fileDownloadUrl = "http://www.ankcustoms.com/ashx/ReportFileManageOperate.ashx?token={0}&operatetype=4&mawb={1}&inv={2}";

    @Resource
    private HttpClientService httpClientService;
    @Resource
    private PdfFileService pdfFileService;
    @Resource
    private PdfListService pdfListService;

    private static final Map<String, String> COOKIES_TOKEN_MAP = new HashMap<String, String>();

    public Map<String, String> getHiddenValues() {
        try {
            String response = this.httpClientService.get(loginUrl);

            if (StringUtils.isNotBlank(response)) {
                this.replaceAllBlank(response);
                String regInput = "<input(.*?)/>";
                List<String> inputList = RegExpUtil.getTextsByReg(response, regInput, 1);
                if (inputList != null) {
                    Map<String, String> result = new HashMap<String, String>();
                    for (String html : inputList) {
                        String type = this.getType(html);
                        if ("hidden".equals(type)) {
                            result.put(this.getName(html), this.getValue(html));
                        }
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            log.error("error occurred : ", e);
        }

        return null;
    }

    public String getToken(String cookie) {
        try {
            if (StringUtils.isBlank(cookie)) {
                return null;
            }

            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
            headerMap.put("Accept-Encoding", "gzip, deflate");
            headerMap.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            headerMap.put("Cache-Control", "max-age=0");
            headerMap.put("Connection", "keep-alive");
            headerMap.put("Cookie", cookie);
            headerMap.put("Host", "www.ankcustoms.com");
            headerMap.put("Referer", "http://www.ankcustoms.com/ShipList.aspx");
            headerMap.put("Upgrade-Insecure-Requests", "1");
            headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");

            String response = this.httpClientService.get(shipListUrl + "?random=" + System.currentTimeMillis(), headerMap);
            if (StringUtils.isNotBlank(response)) {
                this.replaceAllBlank(response);
                String regToken = "GlobalManager.Token='(.*?)'";
                String token = RegExpUtil.getTextByReg(response, regToken, 1);
                if (StringUtils.isNotBlank(token)) {
                    return token;
                } else {
                    log.warn("获取Token时，未能获取到期望的返回结果：" + response);
                }
            }
        } catch (Exception e) {
            log.error("error occurred : ", e);
        }

        return null;
    }

    public AnkDownloadResultModel ankcustomsDownload(String cookie, String startTime, String endTime) throws ServiceException {
        if (StringUtils.isBlank(cookie)) {
            throw new ServiceException(CodeInfo.CODE_PARAMS_NOT_NULL, "Cookie");
        }

        Timestamp tStartTime = null;
        if (StringUtils.isNotBlank(startTime)) {
            tStartTime = DataUtil.convertString2Timestamp(startTime, "yyyy-MM-dd");
        }
        Timestamp tEndTime = null;
        if (StringUtils.isNotBlank(endTime)) {
            tEndTime = DataUtil.convertString2Timestamp(endTime, "yyyy-MM-dd");
        }

        // 如果token存在，先用存储的token
        String token = COOKIES_TOKEN_MAP.get(cookie);
        if (StringUtils.isBlank(token)) {
            token = this.getToken(cookie);
        }
        if (StringUtils.isBlank(token)) {
            throw new ServiceException("token.can.not.be.got");
        }

        ShipListRespModel dataList = this.getShipList(cookie, token, tStartTime, tEndTime);
        // 如果StatusCode不等于0的话，有可能是token过期了，尝试一次获取新的token
        if (dataList != null && dataList.getStatusCode() != 0) {
            token = this.getToken(cookie);
            if (StringUtils.isBlank(token)) {
                throw new ServiceException("token.can.not.be.got");
            }

            dataList = this.getShipList(cookie, token, tStartTime, tEndTime);
        }

        // 保存一下token
        COOKIES_TOKEN_MAP.put(cookie, token);

        AnkDownloadResultModel result = new AnkDownloadResultModel();
        // 下载pdf
        if (dataList != null
                && dataList.getRows() != null
                && !dataList.getRows().isEmpty()) {

            result.setTotalCount(dataList.getTotal());

            PdfFileEntity pdfFileEntity = null;
            boolean coverFlg = false;
            for (ShipModel shipModel : dataList.getRows()) {
                try {
                    pdfFileEntity = this.downloadFile(cookie, token, shipModel.getMawbNo(), shipModel.getInvoiceNo(), coverFlg);
                    if (pdfFileEntity != null) {
                        this.pdfListService.addPdf(pdfFileEntity.getPdfFileId(), shipModel, coverFlg);
                        result.setSuccessCount(result.getSuccessCount() + 1);
                    } else {
                        result.setPassCount(result.getPassCount() + 1);
                    }
                } catch (ServiceException e) {
                    if (e.getCode() == CodeInfo.CODE_AWB_ALREADY_EXIST) {
                        result.setPassCount(result.getPassCount() + 1);

                        if (pdfFileEntity != null && StringUtils.isNotBlank(pdfFileEntity.getFilePath())) {
                            this.pdfFileService.deletePdfFile(pdfFileEntity);
                        }
                    } else {
                        result.setFailCount(result.getFailCount() + 1);
                    }
                } catch (Exception e) {
                    result.setFailCount(result.getFailCount() + 1);

                    log.error("下载或保存文件失败[MAWB=" + shipModel.getMawbNo() + ", INV=" + shipModel.getInvoiceNo() + "]", e);
                }
            }
        }

        return result;
    }

    public ShipListRespModel getShipList(String cookie, String token, Timestamp startTime, Timestamp endTime) throws ServiceException {
        try {
            String sStartTime = "";
            if (startTime != null) {
                sStartTime = URLEncoder.encode(DataUtil.formatTimestamp2String(startTime, "yyyy-MM-dd HH:mm:ss"), "UTF-8");
            }
            String sEndTime = "";
            if (endTime != null) {
                sEndTime = URLEncoder.encode(DataUtil.formatTimestamp2String(endTime, "yyyy-MM-dd HH:mm:ss"), "UTF-8");
            }

            String url = MessageFormat.format(shipListDataUrl, token, sStartTime, sEndTime);
            Map<String, String> headerMap = new HashMap<String, String>();
            headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
            headerMap.put("Accept-Encoding", "gzip, deflate");
            headerMap.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
            headerMap.put("Connection", "keep-alive");
            headerMap.put("keep-alive", cookie);
            headerMap.put("Host", "www.ankcustoms.com");
            headerMap.put("Referer", "http://www.ankcustoms.com/ShipList.aspx");
            headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
            headerMap.put("X-Requested-With", "XMLHttpRequest");

            String response = this.httpClientService.get(url, headerMap);
            if (StringUtils.isNotBlank(response)) {
                return JSONObject.parseObject(response, ShipListRespModel.class);
            }

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log.error("error occurred : ", e);
        }

        return null;
    }

    @Transactional
    public PdfFileEntity downloadFile(String cookie, String token, String mawb, String inv, boolean coverFlg) throws Exception {
        if (StringUtils.isBlank(mawb) || StringUtils.isBlank(inv)) {
            return null;
        }

        if (!coverFlg && this.pdfListService.isAwbExist(this.formatAwb(inv))) {
            return null;
        }

        String url = MessageFormat.format(fileDownloadUrl, token, mawb, inv);

        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("Accept", "application/json, text/javascript, */*; q=0.01");
        headerMap.put("Accept-Encoding", "gzip, deflate");
        headerMap.put("Accept-Language", "zh-CN,zh;q=0.9,en;q=0.8");
        headerMap.put("Connection", "keep-alive");
        headerMap.put("keep-alive", cookie);
        headerMap.put("Host", "www.ankcustoms.com");
        headerMap.put("Referer", "http://www.ankcustoms.com/ShipList.aspx");
        headerMap.put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        headerMap.put("X-Requested-With", "XMLHttpRequest");

        File file = new File(pdfFileService.getFileBaseDir() + mawb + "_" + inv + "_" + System.currentTimeMillis() + ".pdf");
        DownloadResult downloadResult = this.httpClientService.downloadViaGet(url, file, headerMap);
        if (downloadResult.isSuccess()) {
            return this.pdfFileService.uploadPdfFile(file);
        } else {
            throw new ServiceException(downloadResult.getHttpCode(), downloadResult.getErrorMessage());
        }
    }

    private void replaceAllBlank(String content) {
        if (StringUtils.isNotBlank(content)) {
            content = content.replaceAll("\\t", "").replaceAll("\\n", "").replaceAll("\\r", "");
        }
    }

    private String getType(String content) {
        return this.getValue(content, "type");
    }
    private String getName(String content) {
        return this.getValue(content, "name");
    }
    private String getValue(String content) {
        return this.getValue(content, "value");
    }

    private String getValue(String content, String key) {
        if (StringUtils.isNotBlank(content) && StringUtils.isNotBlank(key)) {
            String reg = (key + "=\"(.*?)\"");
            return RegExpUtil.getTextByReg(content, reg, 1);
        }
        return null;
    }

    private String formatAwb(String inv) {
        if (StringUtils.isNotBlank(inv) && inv.startsWith("800")) {
            return inv.substring(0, 3) + "-" + inv.substring(3);
        }

        return inv;
    }
}
