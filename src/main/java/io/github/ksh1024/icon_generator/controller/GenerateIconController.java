package io.github.ksh1024.icon_generator.controller;

import io.github.ksh1024.icon_generator.service.GenerateIconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GenerateIconController {
    private final GenerateIconService generateIconService;

    @GetMapping(value = "/generate/{text}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getIcon(@PathVariable String text) {
        try {
            byte[] imageData = generateIconService.generateIcon(text);

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);

            System.out.println("ok");
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
/**
 *  Java의 이미지 처리 라이브러리(AWT)는 기본적으로 화면(모니터)이 있는 데스크톱 환경에서 작동하도록 설계됨
 * 하지만 Spring Boot 서버는 보통 화면이 없는 환경(서버, Docker 등)에서 실행됨
 * 이렇게 화면이 없는 환경을 헤드리스(headless) 환경이라고 함
 */
}