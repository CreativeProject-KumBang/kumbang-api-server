package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.dto.FilesDTO;
import com.se.kumbangapiserver.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/api/file/upload")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> uploadFile(List<MultipartFile> files) {
        System.out.println(files);
        try {
            List<FilesDTO> fileIdList = fileService.saveFile(files);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(fileIdList)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @DeleteMapping("/api/file/delete/{fileId}")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> deleteFile(@PathVariable String fileId) {
        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList("success")).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }
}
