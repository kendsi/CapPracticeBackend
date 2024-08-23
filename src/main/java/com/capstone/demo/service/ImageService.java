package com.capstone.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.capstone.demo.exception.FileNotFoundException;
import com.capstone.demo.exception.ImageStorageException;
import com.capstone.demo.exception.InvalidFileFormatException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

@Service
public class ImageService {
    
    public Resource loadImage(String filename) {
        String userHome = System.getProperty("user.home");
        String dirPath = userHome + "/images";

        try {
            Path filePath = Paths.get(dirPath, filename);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Image not found: " + dirPath + filename);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("Image not found: " + dirPath + filename);
        }
    }

    public void deleteImages(List<String> imagePaths) {
        if (imagePaths != null && !imagePaths.isEmpty()) {
            for (String imagePath : imagePaths) {
                String userHome = System.getProperty("user.home");
                String filePath = userHome + "/" + imagePath;
                
                File imageFile = new File(filePath);
                if (imageFile.exists()) {
                    if (!imageFile.delete()) {
                        throw new ImageStorageException("Failed to delete image file: " + filePath);
                    }
                }
            }
        }
    }

    public List<String> uploadImages(List<MultipartFile> files) {
        
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");
        List<String> imagePaths = new ArrayList<>();

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {

                    String originalFilename = file.getOriginalFilename();
                    String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase() : "";
                    
                    if(!allowedExtensions.contains(fileExtension)) {
                        throw new InvalidFileFormatException("Invalid file format: " + fileExtension);
                    }

                    try {
                        String fileName = UUID.randomUUID().toString() + "." + fileExtension;
                        String userHome = System.getProperty("user.home");

                        String dirPath = userHome + "/images";

                        File dir = new File(dirPath);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }

                        String filePath = dirPath + "/" + fileName;

                        file.transferTo(new File(filePath));
                        imagePaths.add("images/" + fileName);
                    } catch (IOException e) {
                        throw new ImageStorageException("Failed to save image file");
                    }
                }
            }
        }
        return imagePaths;
    }
}
