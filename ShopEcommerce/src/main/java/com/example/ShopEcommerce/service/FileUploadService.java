package com.example.ShopEcommerce.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileUploadService {

    private final String uploadDir = "D:/upload/";

    public String saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        Path uploadPath = Paths.get(uploadDir);


        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }

        return "/uploads/" + fileName;
    }

    public void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                Path file = Paths.get(uploadDir + fileName);
                Files.deleteIfExists(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
