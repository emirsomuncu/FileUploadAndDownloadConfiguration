package com.somuncu.FileUploadAndDownload.dao;

import com.somuncu.FileUploadAndDownload.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;



public interface FileRepository extends JpaRepository<File, Long> {
}
