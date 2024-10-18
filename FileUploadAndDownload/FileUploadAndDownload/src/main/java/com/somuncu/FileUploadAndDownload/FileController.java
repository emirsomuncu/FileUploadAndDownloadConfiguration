package com.somuncu.FileUploadAndDownload;

import com.somuncu.FileUploadAndDownload.entity.File;
import com.somuncu.FileUploadAndDownload.response.ApiResponse;
import com.somuncu.FileUploadAndDownload.response.SavedFileResponse;
import com.somuncu.FileUploadAndDownload.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("${api.prefix}/files")
public class FileController {

    @Autowired
    private FileService fileService ;

    @PostMapping("/file/upload")
    public ResponseEntity<ApiResponse> saveFile(@RequestParam List<MultipartFile> files) {

        try{
            List<SavedFileResponse> savedFileResponses = this.fileService.saveFile(files);
            return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Uplaod successfull" , savedFileResponses));
        }catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Upload Failed" , null));
        }
    }

    @GetMapping("/file/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws SQLException {
        File file = this.fileService.getFileById(id);
        ByteArrayResource resource = new ByteArrayResource(file.getFileContent().getBytes(1, (int) file.getFileContent().length()));
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.parseMediaType(file.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION , "attachment; filename=\"" +file.getFileName() + "\"")
                .body(resource) ;
    }

    @PutMapping("/image/{fileId}/update")
    public ResponseEntity<ApiResponse> updateFile(@PathVariable Long fileId , @RequestBody MultipartFile file) {

        try {
            File currentFile = fileService.getFileById(fileId);
            if(currentFile != null) {
                fileService.updateFile(file , fileId);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Update success" , null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Update failed!" , INTERNAL_SERVER_ERROR));
    }

    @DeleteMapping("/image/{fileId}/delete")
    public ResponseEntity<ApiResponse> deleteFile(@PathVariable Long fileId ) {

        try {
            File image = fileService.getFileById(fileId);
            if(image != null) {
                fileService.deleteFileById(fileId);
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Delete success" , null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("Delete failed!" , INTERNAL_SERVER_ERROR));

    }

}
