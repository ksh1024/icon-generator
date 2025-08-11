package io.github.ksh1024.icon_generator.controller;

import io.github.ksh1024.icon_generator.service.GenerateIconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;

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
            byte[] imgData = generateIconService.generateIcon(text, "png");
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

    /**
     * 이미지 다운로드 API
     */
    @GetMapping("/download/{text}")
    public ResponseEntity<byte[]> downloadIcon(@PathVariable String text, @RequestParam(defaultValue = "png") String format) {
        try {
            byte[] imageData = generateIconService.generateIcon(text, format);

            String fileExtension = format.equalsIgnoreCase("jpg") ? "jpg" : "png";
            String originalFilename = text + "_identicon." + fileExtension;

            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename(originalFilename, StandardCharsets.UTF_8)
                    .build();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("image/" + fileExtension));
            headers.setContentDisposition(contentDisposition);

            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}