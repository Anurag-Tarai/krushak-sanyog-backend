package farmerconnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "farmerconnect")
@SpringBootApplication
public class KrushakSanyogApplication {
	public static void main(String[] args) {
		SpringApplication.run(KrushakSanyogApplication.class, args);
		System.out.println("Application started........................");
	}
}
