package io.github.ksh1024.icon_generator.service;

import io.github.ksh1024.icon_generator.entity.GenerateIconCache;
import io.github.ksh1024.icon_generator.repository.GenerateIconCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenerateIconService {
    private final GenerateIconCacheRepository repository;
    private final int GRID_SIZE = 5;
    private final int PIXEL_SIZE = 50;
    private final int IMAGE_SIZE = GRID_SIZE * PIXEL_SIZE;

    @Transactional(readOnly = true) // readOnly=true 옵션으로 성능 최적화
    public List<GenerateIconCache> getAll() {
        return repository.findAll();
    }

    @Transactional
    public byte[] generateIcon(String text, String format) throws NoSuchAlgorithmException, IOException {
        // png 형식 기본
        final String imageFormat = (format != null && (format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("jpeg"))) ? "jpeg" : "png";
        // 1. DB에서 캐시된 이미지가 있는지 먼저 확인
        Optional<GenerateIconCache> cached = repository.findByInputText(text);
        if (cached.isPresent()) {
            System.out.println("캐시에서 이미지를 로드합니다: " + text);
            return cached.get().getImageData();
        }

        System.out.println("새로운 이미지를 생성합니다: " + text);

        // 2. 텍스트를 해시값으로 변환
        byte[] hash = MessageDigest.getInstance("SHA-256").digest(text.getBytes());

        // 3. 해시값을 기반으로 색상 결정
        Color color = new Color(hash[0] & 0xFF, hash[1] & 0xFF, hash[2] & 0xFF);

        // 4. 빈 이미지(캔버스) 생성
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // 5. 배경을 흰색으로 칠하기
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        graphics.setColor(color);

        // 6. 해시값을 기반으로 5x5 그리드에 픽셀 그리기 (대칭 구조)
        for (int i = 0; i < hash.length && i < 15; i++) {
            if ((hash[i] & 0x01) == 0) { // 해시값의 비트가 짝수일 때만 픽셀을 그림
                int x = (i % 3) * PIXEL_SIZE; // 0, 1, 2 열에 그림 (나머지 2열은 대칭)
                int y = (i / 3) * PIXEL_SIZE;
                graphics.fillRect(x, y, PIXEL_SIZE, PIXEL_SIZE);
                // 대칭으로 반대편에도 그리기
                graphics.fillRect(IMAGE_SIZE - PIXEL_SIZE * (x / PIXEL_SIZE + 1), y, PIXEL_SIZE, PIXEL_SIZE);
                System.out.println("x: " + x + ", y: " + y);
            }
        }
        graphics.dispose();

        // 7. 생성된 이미지를 byte 배열로 변환
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, imageFormat, baos);
        byte[] imageData = baos.toByteArray();
        // 8. DB에 새로 생성한 이미지 캐싱
        repository.save(new GenerateIconCache(text, imageData));

        return imageData;
    }

}