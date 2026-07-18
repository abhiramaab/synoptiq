package ai.synoptiq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SynoptiqApplication {

	public static void main(String[] args) {
		SpringApplication.run(SynoptiqApplication.class, args);
	}

}
