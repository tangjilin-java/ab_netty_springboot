package ab.tjl;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Author:TangJiLin
 * @Description: 启动类
 * @Date: Created in 2020/4/19 20:54
 * @Modified By:
 */
@SpringBootApplication
// 扫描mybatis mapper包路径
@MapperScan(basePackages="ab.tjl.mapper")
// 扫描 所有需要的包, 包含一些自用的工具类包 所在的路径
@ComponentScan(basePackages= {"ab.tjl", "org.n3r.idworker"})
public class Application {

    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}