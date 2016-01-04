package com.baigu.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by JIADONG on 2016/1/4.
 */
public class FileUtils {
    // 获得文件的字节数组
    public static byte[] getBytes(File f) throws Exception {
        FileInputStream in = new FileInputStream(f);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] b = new byte[1024];
        int n;
        while ((n = in.read(b)) != -1) {
            out.write(b, 0, n);
        }
        in.close();
        return out.toByteArray();
    }
    /**
     * 刪除文件，如果当前文件所在文件夹为空，删除改文件夹
     *
     * @param file
     */
    public static final void deleteFile(File file) {
        if (file == null || !file.exists()) {
            return;
        }
        File parentFile = file.getParentFile();
        file.delete();

        if (parentFile == null || !parentFile.exists()) {
            return;
        }

        String[] files = parentFile.list();
        if (files == null || files.length == 0) {
            parentFile.delete();
        }
    }
}
