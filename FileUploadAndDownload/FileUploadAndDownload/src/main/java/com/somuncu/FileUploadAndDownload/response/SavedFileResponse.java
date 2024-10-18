package com.somuncu.FileUploadAndDownload.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedFileResponse {

    private Long id;
    private String fileName;
    private String downloadUrl;

}
