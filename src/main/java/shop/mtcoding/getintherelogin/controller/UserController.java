package shop.mtcoding.getintherelogin.controller;

import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;

import lombok.RequiredArgsConstructor;
import shop.mtcoding.getintherelogin.model.User;
import shop.mtcoding.getintherelogin.model.UserRepository;
import shop.mtcoding.getintherelogin.service.GetEmail;
import shop.mtcoding.getintherelogin.service.GetToken;

@RequiredArgsConstructor
@Controller
public class UserController {
    private final UserRepository userRepository;

    private final HttpSession session;

    private final GetToken getToken;

    private final GetEmail getEmail;

    @GetMapping("/")
    public String main() {
        return "main";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/callback")
    public String callback(String code) throws JsonProcessingException {
        // 1. code 값 존재 유무 확인
        if (code == null || code.isEmpty()) {
            return "redirect:/loginForm";
        }

        String accessToken = getToken.토큰받기(code);
        String email = getEmail.이메일받기(accessToken);
        Long id = getEmail.아이디받기(accessToken);

        // 2. 해당 email로 회원가입되어 있는 user 정보가 있는지 db 조회
        User user = userRepository.findByUsername("kakao_" + id);

        // 3. 존재하지 않을 시, 강제 회원가입 후, 그 user 정보로 session 생성(자동 로그인)
        if (user == null) {
            userRepository.insert("kakao_" + id, UUID.randomUUID().toString(),
                    email, "kakao");
            session.setAttribute("principal", user);
        }

        if (user != null) {
            session.setAttribute("principal", user);
        }

        return "redirect:/";
    }
}
