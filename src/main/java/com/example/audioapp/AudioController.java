package com.example.audioapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.google.firebase.auth.FirebaseToken;

@Controller
public class AudioController {

    private static final String AUDIO_DIR = "audio";

    @Autowired
    private FileStorageService fileService;

    @Autowired
    private FirebaseTokenVerifier tokenVerifier;

    @GetMapping("/")
    public String index(Model model) {
        File folder = new File(AUDIO_DIR);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".mp3") || name.endsWith(".wav"));
        List<String> audioFiles = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                audioFiles.add(file.getName());
            }
        }
        model.addAttribute("audioFiles", audioFiles);
        return "index";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         @RequestHeader("Authorization") String authorization) {
        try {
            String idToken = authorization.replace("Bearer ", "");
            FirebaseToken decodedToken = tokenVerifier.verifyToken(idToken);
            fileService.saveFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/unauthorized";
        }
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("filename") String filename,
                         @RequestHeader("Authorization") String authorization) {
        try {
            String idToken = authorization.replace("Bearer ", "");
            FirebaseToken decodedToken = tokenVerifier.verifyToken(idToken);
            fileService.deleteFile(filename);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/unauthorized";
        }
        return "redirect:/";
    }
}