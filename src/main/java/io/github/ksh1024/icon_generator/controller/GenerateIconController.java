package io.github.ksh1024.icon_generator.controller;

import io.github.ksh1024.icon_generator.service.GenerateIconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class GenerateIconController {
    private final GenerateIconService generateIconService;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    /**
     * 이미지 생성 API
     */
    @GetMapping(value = "/generate/{text}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImg(@PathVariable String text) {
        try {
            byte[] imgData = generateIconService.generateIcon(text);
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            if(text.equals("error")) { // test용
                throw new Exception();
            }
            return new ResponseEntity<>(imgData, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}