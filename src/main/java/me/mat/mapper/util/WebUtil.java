package me.mat.mapper.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WebUtil {

    /**
     * Downloads a file from a url
     * to a file on disk
     *
     * @param url  url of the file that you want to download
     * @param file file handle that you want to download the file to
     */

    public static void download(String url, File file) {
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs()) {
                throw new RuntimeException("Failed to create parent file");
            }
        }
        try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
