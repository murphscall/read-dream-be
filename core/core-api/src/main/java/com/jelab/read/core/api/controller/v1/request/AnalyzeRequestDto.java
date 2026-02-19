package com.jelab.read.core.api.controller.v1.request;

import org.springframework.web.multipart.MultipartFile;

public class AnalyzeRequestDto {

    private MultipartFile imageFile;

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

}
