package shop.mtcoding.getintherelogin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/callback")
    public @ResponseBody String callback(String code) {
        if (code != null) {
            System.out.println("인증 완료 " + code);
        }
        return code;
    }
}
