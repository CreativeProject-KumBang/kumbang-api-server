package com.se.kumbangapiserver.service.impl;

import com.se.kumbangapiserver.domain.file.Files;
import com.se.kumbangapiserver.domain.file.FileRepository;
import com.se.kumbangapiserver.dto.FilesDTO;
import com.se.kumbangapiserver.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${external.static.url.inbound}")
    private String filePath;

    @Override
    @Transactional
    public List<FilesDTO> saveFile(List<MultipartFile> files) {
        List<FilesDTO> filePks = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                String fileId = UUID.randomUUID().toString();
                String originalFileName = file.getOriginalFilename();
                String fileExtension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf(".") + 1) : null;
                originalFileName = originalFileName != null ? originalFileName.substring(0, originalFileName.lastIndexOf(".")) : null;
                Long fileSize = file.getSize();
                String dbPath = "/image/" + fileId + "." + fileExtension;

                File newFile = new File(filePath, fileId + "." + fileExtension);
                log.info("File path: " + newFile.getAbsolutePath());
                if (!newFile.exists()) {
                    boolean mkdirs = newFile.mkdirs();
                    if (!mkdirs) {
                        throw new IOException("Failed to create directory.");
                    }
                }

                file.transferTo(newFile);

                Files savedFile = Files.builder()
                        .name(originalFileName)
                        .path(dbPath)
                        .size(fileSize)
                        .type(fileExtension)
                        .build();

                Files save = fileRepository.save(savedFile);
                filePks.add(save.toDTO());

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return filePks;
    }

    @Override
    @Transactional
    public void deleteFile(String fileId) {
        Files file = fileRepository.findById(Long.valueOf(fileId)).orElseThrow(() -> new RuntimeException("File not found."));
        File fileToDelete = new File(file.getPath());
        if (fileToDelete.exists()) {
            boolean delete = fileToDelete.delete();
            if (!delete) {
                throw new RuntimeException("Failed to delete file.");
            }
        }
        fileRepository.delete(file);
    }
}