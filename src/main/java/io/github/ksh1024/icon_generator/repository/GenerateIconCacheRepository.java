package io.github.ksh1024.icon_generator.repository;

import io.github.ksh1024.icon_generator.entity.GenerateIconCache;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GenerateIconCacheRepository extends JpaRepository<GenerateIconCache, Long> {
    // 입력된 텍스트로 캐시된 이미지를 찾기
    Optional<GenerateIconCache> findByInputText(String inputText);

}