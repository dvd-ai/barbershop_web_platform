package com.app.barbershopweb.util;

import com.app.barbershopweb.exception.FileException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class MultipartFileUtil {
    private MultipartFileUtil() {
    }

    public static File convertMultipartFileToFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        File file = new File(Objects.requireNonNull(fileName));
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        } catch (IOException e) {
            file.delete();
            throw new FileException(List.of(e.getMessage()));
        }
        return file;
    }
}
