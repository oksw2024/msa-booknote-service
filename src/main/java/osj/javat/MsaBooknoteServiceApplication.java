package osj.javat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MsaBooknoteServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsaBooknoteServiceApplication.class, args);
	}

}
