package com.xc.media.storage;

import java.io.InputStream;
import java.util.List;

public interface IFileStorage {

    String uploadFile(String fileName, InputStream inputStream, long size, String contentType);

    void deleteFile(String fileName);

    void deleteFiles(List<String> files);
}
