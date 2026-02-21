package com.jelab.read.core.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jelab.read.core.support.error.CoreException;
import com.jelab.read.core.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

class AnalysisImageTest {

    @Test
    @DisplayName("성공: 유효한 이미지 파일로 객체 생성")
    void create_Success() {
        MockMultipartFile file = new MockMultipartFile("img", "test.png", "image/png", "data".getBytes());
        assertThatCode(() -> new AnalysisImage(file)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("예외: 이미지 형식이 아닌 파일은 예외 발생")
    void create_NotImage_Fail() {
        MockMultipartFile file = new MockMultipartFile("txt", "test.txt", "text/plain", "data".getBytes());
        assertThatThrownBy(() -> new AnalysisImage(file)).isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.INVALID_IMAGE_FILE.getMessage());
    }

    @Test
    @DisplayName("예외: 이미지 크기가 20MB를 초과할 경우 예외 발생")
    void create_SizeExceed_Fail() {
        int twentyMegaBytesPlusOne = 20 * 1024 * 1024 + 1;
        byte[] overSizedData = new byte[twentyMegaBytesPlusOne];

        MockMultipartFile overSizedFile = new MockMultipartFile(
                "image", "heavy_contract.png", "image/png", overSizedData);

        assertThatThrownBy(() -> new AnalysisImage(overSizedFile)).isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.IMAGE_FILE_TOO_LARGE.getMessage());
    }
}