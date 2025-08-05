package io.github.ksh1024.icon_generator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Spring Boot는 실행 시, 메인 애플리케이션 클래스
 * (IdenticonGeneratorApplication.java)가 있는 패키지와
 * 그 하위 패키지들만 스캔해서 @RestController 같은 어노테이션을 찾습니다
 */
@SpringBootApplication
@EnableJpaAuditing
public class IconGeneratorApplication {
	public static void main(String[] args) {
		System.setProperty("java.awt.headless", "true");
		SpringApplication.run(IconGeneratorApplication.class, args);
 	}

}
