package com.somuncu.FileUploadAndDownload.service;

import com.somuncu.FileUploadAndDownload.entity.File;
import com.somuncu.FileUploadAndDownload.response.SavedFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    public File getFileById(Long fileId);
    public List<SavedFileResponse> saveFile(List<MultipartFile> files);
    public void deleteFileById(Long fileId);
    public void updateFile(MultipartFile file, Long fileId);

}
