package com.app.barbershopweb.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class MultipartFileUtil {
    private MultipartFileUtil(){}

    public static File convertMultipartFileToFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        File file = new File(Objects.requireNonNull(fileName));

        try (FileOutputStream fos = new FileOutputStream(file)){
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }
}
