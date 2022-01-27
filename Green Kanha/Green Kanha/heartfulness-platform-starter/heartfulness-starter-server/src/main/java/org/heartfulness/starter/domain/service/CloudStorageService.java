package org.heartfulness.starter.domain.service;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public interface CloudStorageService {

    void uploadToCloudStorage(String file, InputStream fileData, boolean makePubliclyAvailable) throws IOException,
            ParserConfigurationException,
            SAXException;

    String getFileURL(String filePath) throws IOException, ParserConfigurationException, SAXException;
}
