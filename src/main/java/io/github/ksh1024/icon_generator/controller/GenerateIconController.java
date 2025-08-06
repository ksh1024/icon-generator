package io.github.ksh1024.icon_generator.controller;

import io.github.ksh1024.icon_generator.service.GenerateIconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class GenerateIconController {
    private final GenerateIconService generateIconService;

    @GetMapping("/")
    public String main() {
        return "index";
    }

    /**
     * 폼에서 '생성하기' 버튼 클릭 시 매핑
     * @param text 사용자가 입력한 텍스트
     * @param model View에 데이터를 전달하는 객체
     * @return "index" -> 데이터를 가지고 index.html을 다시 보여줌
     */
    @PostMapping("/generate")
    public String generateFromForm(@RequestParam String text, Model model) {
        if (text != null && !text.trim().isEmpty()) {
            model.addAttribute("imgUrl", "/img/" + text);
            model.addAttribute("inputText", text);
        }
        return "index";
    }

    /**
     * 이미지 생성 API
     * HTML의 <img> 태그가 호출함
     */
    @GetMapping(value = "/img/{text}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImg(@PathVariable String text) {
        try {
            byte[] imageData = generateIconService.generateIcon(text);
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
          //  throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}