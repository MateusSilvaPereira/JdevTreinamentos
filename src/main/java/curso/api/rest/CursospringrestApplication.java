package curso.api.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"curso.api.rest.model"})
@ComponentScan(basePackages = {"curso.*"})
@EnableJpaRepositories(basePackages = {"curso.api.rest.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@AutoConfiguration
@EnableAutoConfiguration
public class CursospringrestApplication implements WebMvcConfigurer{

    public static void main(String[] args) {
        SpringApplication.run(CursospringrestApplication.class, args);
        
        System.out.println(new BCryptPasswordEncoder().encode("123"));
    }
    
    /*Mapeamento Global Que Refletem Em Todo o Sistema*/
    @CrossOrigin
    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	
    	registry.addMapping("/usuario/**")
    	.allowedMethods("*") 	//("POST", "DELETE")
    	.allowedOrigins("*");  	//("www.com.cliente40.com.br", "www.com.cliente80@.com.br", "localhost:8080");
    							// Liberando apenas requisições post  para o usuário servidor 40
    	
    }

}
