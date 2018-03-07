package com.tasfe.sis.base.configs;

import java.io.File;

import org.springframework.context.annotation.Configuration;

/**
 * Created by Lait on 2017/8/1.
 */
@Configuration
public class FileUploadConfig {
    //@Value("${file.upload.directory}")
    public static String UPLOAD_DIR = System.getProperty("user.dir") + File.separator+"uploads";

    public static String DNOWLOAD_DIR = System.getProperty("user.dir") + File.separator+"downloads";

    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir")+ File.separator);

    }
}
