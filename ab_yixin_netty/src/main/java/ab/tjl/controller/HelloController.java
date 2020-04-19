package ab.tjl.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:TangJiLin
 * @Description:
 * @Date: Created in 2020/4/19 21:00
 * @Modified By:
 */
@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello yixin";
    }
}
