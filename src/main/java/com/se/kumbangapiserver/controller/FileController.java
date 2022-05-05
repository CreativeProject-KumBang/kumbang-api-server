package com.se.kumbangapiserver.controller;

import com.se.kumbangapiserver.common.ResponseForm;
import com.se.kumbangapiserver.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/api/file/upload")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> uploadFile(List<MultipartFile> files) {

        try {
            List<Long> fileIdList = fileService.saveFile(files);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList(fileIdList)).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

    @DeleteMapping("/api/file/delete")
    @ResponseBody
    public ResponseEntity<ResponseForm<Object>> deleteFile(List<Long> fileIdList) {

        try {
            fileService.deleteFile(fileIdList);
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.TRUE).response(Collections.singletonList("success")).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(ResponseForm.builder().status(Boolean.FALSE).response(Collections.singletonList("fail")).build());
        }
    }

}
