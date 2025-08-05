package io.github.ksh1024.icon_generator.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class GenerateIconCache {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String inputText;

    @Lob // Large Object: 대용량 데이터를 저장할 때 사용
    @Column(nullable = false, columnDefinition = "LONGBLOB")
    private byte[] imageData; // 이미지 데이터를 바이트 배열로 저장

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDnt;

    public GenerateIconCache(String inputText, byte[] imageData) {
        this.inputText = inputText;
        this.imageData = imageData;
    }
}
