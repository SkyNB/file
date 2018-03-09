package com.mobanker.selenium;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {




    /**
     * 写入txt文件，可以在原文件内容的基础上追加内容(并判断目录是否存在，不存在则生成目录)
     *
     * @param value
     *            写入文件内容
     * @param fileName
     *            文件名字；
     * @param code
     *            文件的编码；
     * @throws IOException
     */
    public static void WriteFile(String value,  String filePath, String fileName ) {
        File file = null;
        try {
            String path=System.getProperty("user.dir");

            System.out.println("数据保存:"+path+File.separator+filePath+File.separator+fileName);


            file = new File(path+File.separator+filePath);


            if (!file.exists())
                file.mkdirs();



            file = new File( path+File.separator+filePath+File.separator+fileName);
            if (!file.exists())
                file.createNewFile();
            FileOutputStream out = new FileOutputStream(file, true);
            out.write(value.getBytes("UTF-8"));
            out.write("\r\n".getBytes("UTF-8"));
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}