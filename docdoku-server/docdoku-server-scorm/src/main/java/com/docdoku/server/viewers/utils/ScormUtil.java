package com.docdoku.server.viewers.utils;

import com.docdoku.core.util.FileIO;

import java.io.InputStream;

public class ScormUtil {

    private ScormUtil(){

    }

    public static final String IMS_MANIFEST = "imsmanifest.xml";
    public static final String SCORM_FOLDER = "scorm";

    public static boolean isScormArchive(String fileName, InputStream inputStream) {
        return FileIO.isArchiveFile(fileName) && FileIO.existsInArchive(inputStream, IMS_MANIFEST);
    }

    public static String getScormSubResourceVirtualPath(String uriSubResource) {
        StringBuilder sb = new StringBuilder().append(SCORM_FOLDER).append("/").append(uriSubResource);
        return sb.toString();
    }

}
