package ab.tjl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author:TangJiLin
 * @Description: 启动类
 * @Date: Created in 2020/4/19 20:54
 * @Modified By:
 */
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = {"ab.tjl.mapper"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
