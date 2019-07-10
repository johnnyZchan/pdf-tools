package com.ledi.pdftools.services;

import com.ledi.pdftools.exceptions.ServiceException;

import java.util.Map;

public interface AnkcustomsService {

    public void ankcustomsDownload(String cookie, String startTime, String endTime) throws ServiceException;

}
