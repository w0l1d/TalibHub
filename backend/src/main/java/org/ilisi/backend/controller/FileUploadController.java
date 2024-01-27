package org.ilisi.backend.controller;

import org.ilisi.backend.exception.StorageFileNotFoundException;
import org.ilisi.backend.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/public/files")
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles() {
        throw new RuntimeException("Not implemented yet bcs of no need for now");
    }

    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        if (file == null)
            throw new StorageFileNotFoundException("File not found " + filename);

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=\"%s\"", file.getFilename())
        ).body(file);
    }

    @PostMapping("/")
    public ResponseEntity<Map<String, Object>> handleFileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "replaceFile", required = false) String replaceFile) {

        String filename = replaceFile != null && !replaceFile.isBlank()
                ? storageService.overwrite(file, replaceFile) : storageService.store(file);

        return ResponseEntity.ok().body(Map.of(
                "filename", filename,
                "uri", String.format("/public/files/%s", filename),
                "message", "File uploaded successfully"
        ));
    }


}