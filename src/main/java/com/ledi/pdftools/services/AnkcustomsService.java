package com.ledi.pdftools.services;

import com.ledi.pdftools.beans.ank.AnkDownloadResultModel;
import com.ledi.pdftools.exceptions.ServiceException;

import java.util.Map;

public interface AnkcustomsService {

    public AnkDownloadResultModel ankcustomsDownload(String cookie, String startTime, String endTime) throws ServiceException;

}
