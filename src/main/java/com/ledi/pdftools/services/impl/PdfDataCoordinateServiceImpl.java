package com.ledi.pdftools.services.impl;


import com.itextpdf.text.Rectangle;
import com.ledi.pdftools.beans.RectModel;
import com.ledi.pdftools.entities.PdfDataCoordinateEntity;
import com.ledi.pdftools.entities.PdfListEntity;
import com.ledi.pdftools.mappers.PdfDataCoordinateMapper;
import com.ledi.pdftools.services.PdfDataCoordinateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service("pdfDataCoordinateService")
@Slf4j
public class PdfDataCoordinateServiceImpl implements PdfDataCoordinateService {

    @Value("${pdf.tools.file.width}")
    private String pdfFileWidth;
    @Value("${pdf.tools.file.height}")
    private String pdfFileHeight;

    @Resource
    PdfDataCoordinateMapper pdfDataCoordinateMapper;

    public List<PdfDataCoordinateEntity> getDeletePageDataCoordinateList(String templateName, int pageNo) {
        return this.getPageDataCoordinateList(templateName, pageNo, PdfDataCoordinateEntity.ACTION_TYPE_DEL);
    }

    public List<PdfDataCoordinateEntity> getReplacePageDataCoordinateList(String templateName, int pageNo) {
        return this.getPageDataCoordinateList(templateName, pageNo, PdfDataCoordinateEntity.ACTION_TYPE_REPLACE);
    }

    @Transactional
    public List<PdfDataCoordinateEntity> getPageDataCoordinateList(String templateName, int pageNo, int actionType) {
        List<PdfDataCoordinateEntity> result = this.pdfDataCoordinateMapper.findByPageAndAction(templateName, pageNo, actionType);
        if (result != null && result.size() > 0) {
            for (PdfDataCoordinateEntity entity : result) {
                if (entity.getLlx() == null || entity.getLly() == null
                        || entity.getUrx() == null || entity.getUry() == null) {
                    boolean flg = this.calCoordinate(entity);
                    if (flg) {
                        this.pdfDataCoordinateMapper.update(entity);
                    }
                }
            }
        }

        return result;
    }

    /**
     * 计算PDF矩形的左下坐标和右上坐标
     * 注意：Y轴需要从下面开始做0轴
     * @param entity
     * @return
     */
    public boolean calCoordinate(PdfDataCoordinateEntity entity) {
        boolean result = false;

        if (entity != null && entity.getMarginLeft() != null && entity.getMarginTop() != null
                && entity.getWidth() != null && entity.getHeight() != null) {
            // llx = left
            entity.setLlx(entity.getMarginLeft());
            // lly = PDF高 - top - height
            entity.setLly(new BigDecimal(this.pdfFileHeight).subtract(entity.getMarginTop()).subtract(entity.getHeight()));
            // urx = left + width
            entity.setUrx(entity.getMarginLeft().add(entity.getWidth()));
            // ury = PDF高 - top
            entity.setUry(new BigDecimal(this.pdfFileHeight).subtract(entity.getMarginTop()));
            result = true;
        }

        return result;
    }

    public RectModel createRect(Double left, Double top, Double width, Double height) {
        if (left == null || top == null || width == null || height == null) {
            return null;
        }

        BigDecimal llx = new BigDecimal(left);
        BigDecimal lly = new BigDecimal(this.pdfFileHeight).subtract(new BigDecimal(top)).subtract(new BigDecimal(height));
        BigDecimal urx = new BigDecimal(left).add(new BigDecimal(width));
        BigDecimal ury = new BigDecimal(this.pdfFileHeight).subtract(new BigDecimal(top));

        RectModel result = new RectModel();
        result.setLlx(llx);
        result.setLly(lly);
        result.setUrx(urx);
        result.setUry(ury);

        return result;
    }
}
