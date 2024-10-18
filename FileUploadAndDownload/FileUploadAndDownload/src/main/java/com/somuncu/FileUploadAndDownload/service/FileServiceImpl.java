package com.somuncu.FileUploadAndDownload.service;

import com.somuncu.FileUploadAndDownload.dao.FileRepository;
import com.somuncu.FileUploadAndDownload.entity.File;
import com.somuncu.FileUploadAndDownload.exception.ResourceNotFoundException;
import com.somuncu.FileUploadAndDownload.response.SavedFileResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileServiceImpl implements FileService{

    @Autowired
    private FileRepository fileRepository ;

    @Override
    public File getFileById(Long fileId) {

        return this.fileRepository.findById(fileId).orElseThrow(() -> new ResourceNotFoundException("No file with " +fileId + " id"));
    }

    @Override
    public List<SavedFileResponse> saveFile(List<MultipartFile> files) {

        List<SavedFileResponse> savedFileResponses = new ArrayList<>();

        for(MultipartFile fileRunner : files) {

            try{
                File file = new File();
                file.setFileName(fileRunner.getOriginalFilename());
                file.setFileType(fileRunner.getContentType());
                file.setFileContent(new SerialBlob(fileRunner.getBytes()));

                String buildDownloadUrl = "/api/v1/files/file/download/";
                String downloadUrl = buildDownloadUrl + file.getId() ;
                file.setDownloadUrl(downloadUrl);

                File savedFile = this.fileRepository.save(file);

                savedFile.setDownloadUrl(buildDownloadUrl + savedFile.getId());
                this.fileRepository.save(savedFile);

                SavedFileResponse savedFileResponse = new SavedFileResponse();
                savedFileResponse.setFileName(savedFile.getFileName());
                savedFileResponse.setId(savedFile.getId());
                savedFileResponse.setDownloadUrl(savedFile.getDownloadUrl());

                savedFileResponses.add(savedFileResponse);

            }catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }

        }

        return savedFileResponses;

    }

    @Override
    public void deleteFileById(Long fileId) {
        this.fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("No file with " +fileId + " id"));
        this.fileRepository.deleteById(fileId);
    }

    @Override
    public void updateFile(MultipartFile file, Long fileId) {

        File currentFile = this.fileRepository.findById(fileId).orElseThrow(()->new ResourceNotFoundException("No file with " +fileId + " id"));

        try {
            currentFile.setFileName(file.getOriginalFilename());
            currentFile.setFileType(file.getContentType());
            currentFile.setFileContent(new SerialBlob(file.getBytes()));
        }catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

}
