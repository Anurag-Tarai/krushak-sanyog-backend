package Ecom.SecurityConfig;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
public class AppConfig {
    @Bean
    public SecurityFilterChain springSecurityConfiguration(HttpSecurity http) throws Exception {

        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .cors(cors -> {
                    cors.configurationSource(new CorsConfigurationSource() {
                        @Override
                        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                            CorsConfiguration cfg = new CorsConfiguration();

                            // Combine all allowed origins into a single call
                            cfg.setAllowedOriginPatterns(Arrays.asList(
                                    "*", // Allow all origins (optional if you want it to be open for all)
                                    "https://eccomers96.netlify.app",
                                    "http://localhost:3000",
                                    "http://192.168.1.14:3000", // replace with your phone's IP address
                                    "http://192.168.1.15:3000"
                            ));

                            cfg.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            cfg.setAllowCredentials(true);
                            cfg.setAllowedHeaders(Arrays.asList("*"));
                            cfg.setExposedHeaders(Arrays.asList("Authorization"));

                            return cfg;
                        }

                    });
                })
                .authorizeHttpRequests(auth -> {
                    auth
                            .requestMatchers(HttpMethod.POST, "/ecom/admin/signup").permitAll()
                            .requestMatchers(HttpMethod.POST, "/ecom/customers").permitAll()
                            .requestMatchers(HttpMethod.DELETE, "/ecom/orders/users/**").permitAll()
                            .requestMatchers(HttpMethod.GET, "/health-check","/ecom/signIn", "/ecom/farmer-login",
                                    "/ecom/product-reviews/**",
                                    "/ecom/products/all").permitAll()

                            .requestMatchers(
                                    HttpMethod.POST,
                                    "/ecom/product/**",
                                    "/ecom/order-shippers/**"

                            ).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.POST,
                                    "/ecom/product/**",
                                    "/ecom/product-reviews/**",
                                    "/ecom/customer-addresses/**",
                                    "/ecom/cart/**",
                                    "/ecom/orders/**",
                                    "/ecom/order-shipping/**"
                            ).hasRole("USER")
                            .requestMatchers(
                                    HttpMethod.PUT,
                                    "/ecom/admin/**",
                                    "/ecom/products/**"

                            ).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.PUT,
                                    "/ecom/admin/**",
                                    "/ecom/product-reviews/**",
                                    "/ecom/customer-addresses/update/**",
                                    "/ecom/cart/**", "/ecom/order-shipping/**"

                            ).hasRole("USER")

                            .requestMatchers(
                                    HttpMethod.DELETE,
                                    "/ecom/products/**",
                                    "/ecom/product-reviews/**",
                                    "/ecom/customer-addresses/delete/**",
//                                    "/ecom/orders/users/**",
                                    "/ecom/order-shipping/**",
                                    "/ecom/order-shippers/**"

                            ).hasRole("ADMIN")
                            .requestMatchers(
                                    HttpMethod.DELETE,
                                    "/ecom/cart/remove-product/**"
//                                    "/ecom/orders/users/**"
                            ).hasRole("USER")

                            .requestMatchers(
                                    HttpMethod.GET,

                                    "/ecom/customer-addresses/**",
                                    "/ecom/cart/products/**",
                                    "/ecom/orders/**",
                                    "/ecom/order-shippers",
                                    "/ecom/order-payments/**"

                            ).hasAnyRole("ADMIN", "USER")

                            .requestMatchers("/swagger-ui*/**", "/v3/api-docs/**").permitAll()
                            .anyRequest().authenticated();
                })
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(new JwtTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JwtTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

