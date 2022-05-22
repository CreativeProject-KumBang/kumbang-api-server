package com.se.kumbangapiserver.service;

import com.se.kumbangapiserver.dto.FilesDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<FilesDTO> saveFile(List<MultipartFile> files);

    void deleteFile(String fileId);

}
