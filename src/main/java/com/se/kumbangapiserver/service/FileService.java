package com.se.kumbangapiserver.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    List<Long> saveFile(List<MultipartFile> files);

}
