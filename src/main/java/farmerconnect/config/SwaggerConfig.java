package farmerconnect.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI().info(
                new Info()
                        .title("🌾 Farmer Connect APIs — Direct Farmer-to-Buyer Platform")
                        .description("""
Farmer Connect is a location-based platform that enables farmers to list their products and allows buyers to discover nearby farm produce with ease.
Using geolocation search, interactive maps, and product filtering, buyers can quickly find what’s available around them and contact farmers directly — removing middlemen and promoting transparent, local trade.

The platform helps farmers manage product details, availability, images, and location, while buyers can explore listings, view product information, bookmark favorites, and navigate to farm locations through integrated map links.

### </> Tech Stack
- Spring Boot, React, MySQL, AWS RDS, S3, Render, Vercel

### 🔗 Links
---
- [Live Application](https://farmer-connect-web.vercel.app/)
- [Backend Repository](https://github.com/anurag-tarai/krushak-sanyog-backend)
- [Frontend Repository](https://github.com/anurag-tarai/krushak-sanyog-frontend)


### 👤 Contact
---
**Author:** Anurag Tarai | 📧 [anurag.tarai01@gmail.com](anurag.tarai01@gmail.com)  | 🔗 [LinkedIn](https://linkedin.com/in/anurag-tarai)
""")
                        .version("1.0.0")
        );
    }
}
