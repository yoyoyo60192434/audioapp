package com.example.audioapp;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    private final String AUDIO_DIR = "audio";

    public void saveFile(MultipartFile file) throws IOException {
        Path path = Paths.get(AUDIO_DIR);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        Path filePath = path.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath);
    }

    public void deleteFile(String filename) throws IOException {
        Path filePath = Paths.get(AUDIO_DIR, filename);
        Files.deleteIfExists(filePath);
    }
}
