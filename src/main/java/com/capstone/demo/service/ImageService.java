package com.capstone.demo.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import com.capstone.demo.exception.FileNotFoundException;

import java.nio.file.Path;
import java.nio.file.Paths;

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
}
