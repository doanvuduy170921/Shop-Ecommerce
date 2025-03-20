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

    private final String uploadDir = "src/main/resources/static/upload";

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

//    public void deleteFile(String filePath) {
//        if (filePath != null && !filePath.isEmpty()) {
//            try {
//                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
//                Path file = Paths.get(uploadDir + fileName);
//                Files.deleteIfExists(file);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                // Lấy tên file từ URL path
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

                // Xây dựng đường dẫn đầy đủ đến file vật lý
                Path file = Paths.get(uploadDir).resolve(fileName);

                System.out.println("Attempting to delete file: " + file.toString());

                if (Files.exists(file)) {
                    Files.delete(file);
                    System.out.println("File deleted successfully: " + fileName);
                } else {
                    System.out.println("File not found: " + file.toString());
                }
            } catch (IOException e) {
                System.err.println("Error deleting file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
