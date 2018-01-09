package com.naver.viewlabs;

import android.text.TextUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by abyss on 2017. 12. 20..
 */

public class ZipUtil {
    private static final int BUFFER_SIZE = 4096;

    public static void unzip(String zipFilePath, String destDirectory) throws IOException {
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();

        if(TextUtils.isEmpty(destDirectory)) {
            destDirectory = zipFilePath + "/temp";
        }
        makeDirectory(destDirectory);

        // iterates over entries in the zip file
        try {
            while (entry != null) {
                String filePath = destDirectory + File.separator + entry.getName();

                try {
                    if (!entry.isDirectory()) {
                        // if the entry is a file, extracts it
                        extractFile(zipIn, filePath);

                    } else {
                        makeDirectory(filePath);
                    }
                } finally {
                    zipIn.closeEntry();
                }

                entry = zipIn.getNextEntry();
            }
        } finally {
            zipIn.close();
        }
    }

    private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read;

        try {
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        } finally {
            bos.close();
        }
    }

    public static void makeDirectory(String path) {
        File file = new File(path);

        if (file.exists()) {
            return;
        }

        file.mkdirs();
    }
}