package com.jelab.read.core.domain;

import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class AnalysisImage {

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/png", "image/webp",
            "image/heic", "image/heif");

    private static final long MAX_FILE_SIZE = 20 * 1024 * 1024;

    private final MultipartFile file;

    public AnalysisImage(final MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CoreException(ErrorType.INVALID_IMAGE_FILE);
        }
        if (!ALLOWED_IMAGE_TYPES.contains(file.getContentType())) {
            throw new CoreException(ErrorType.INVALID_IMAGE_FILE);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new CoreException(ErrorType.IMAGE_FILE_TOO_LARGE);
        }

        this.file = file;
    }

    public byte[] getBytes() throws IOException {
        return this.file.getBytes();
    }

    public String getContentType() {
        return this.file.getContentType();
    }

    public MultipartFile getFile() {
        return this.file;
    }

}
