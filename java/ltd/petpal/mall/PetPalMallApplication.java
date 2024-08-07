package ltd.petpal.mall;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 13
 */
@MapperScan("ltd.petpal.mall.dao")
@SpringBootApplication
public class PetPalMallApplication {
    public static void main(String[] args) {
        SpringApplication.run(PetPalMallApplication.class, args);
    }
}
