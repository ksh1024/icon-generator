package io.github.ksh1024.icon_generator.controller;

import io.github.ksh1024.icon_generator.entity.GenerateIconCache;
import io.github.ksh1024.icon_generator.service.GenerateIconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

    @GetMapping("/admin/history")
    public ResponseEntity<String> printHistory() {
        System.out.println("\n===== 모든 생성 기록 조회 =====");

        List<GenerateIconCache> historyList = generateIconService.getAll();
        if (historyList.isEmpty()) {
            System.out.println("-> 저장된 기록이 없습니다.");
        } else {
            System.out.println("-> 총 " + historyList.size() + "개의 기록을 조회했습니다.");
            for (GenerateIconCache cache : historyList) {
                System.out.printf("  - ID: %d, Input: '%s', Created: %s%n",
                        cache.getId(),
                        cache.getInputText(),
                        cache.getRegDnt());
            }
        }
        System.out.println("=====================================\n");

        // 브라우저에는 간단한 성공 메시지만 표시
        return ResponseEntity.ok("모든 기록을 서버 콘솔에 출력했습니다.");
    }
}