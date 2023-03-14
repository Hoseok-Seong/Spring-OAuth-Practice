package shop.mtcoding.getintherelogin.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import shop.mtcoding.getintherelogin.dto.TokenProperties;
import shop.mtcoding.getintherelogin.dto.UserProperties;
import shop.mtcoding.getintherelogin.model.User;
import shop.mtcoding.getintherelogin.model.UserRepository;
import shop.mtcoding.getintherelogin.util.Fetch;
import shop.mtcoding.getintherelogin.util.RandomPasswordGenerator;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession session;

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/callback")
    public @ResponseBody String callback(String code) {
        // 1. code 값 존재 유무 확인
        if (code == null || code.isEmpty()) {
            return "bad request";
        }

        // 토큰 받기
        // 2. code 값 카카오 전달 => access token 받기
        String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";

        // body 객체
        MultiValueMap<String, String> xForm = new LinkedMultiValueMap<>();
        xForm.add("grant_type", "authorization_code");
        xForm.add("client_id", "700486e5a5009af20ef26bca6cec84a9");
        xForm.add("redirect_uri", "http://localhost:8080/callback"); // 2차 검증
        xForm.add("code", code); // 핵심

        // 이메일 받기
        // 3. access token으로 카카오의 유저의 resource 접근 가능해짐
        String tokenResponseBody = Fetch.kakaoToken(kakaoTokenUrl, HttpMethod.POST, xForm).getBody();
        // System.out.println(responseBody);

        // 4. access token 파싱
        ObjectMapper om = new ObjectMapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        TokenProperties tp = null;

        String infoResponseBody = "";
        try {
            tp = om.readValue(tokenResponseBody, TokenProperties.class);
            String kakaoInfoUrl = "https://kapi.kakao.com/v2/user/me";
            String accessToken = tp.getAccess_token();
            infoResponseBody = Fetch.kakaoInfo(kakaoInfoUrl, HttpMethod.POST, accessToken).getBody();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 5. access token으로 email 정보 받기
        System.out.println(infoResponseBody);
        ObjectMapper om2 = new ObjectMapper();
        om2.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        UserProperties tp2 = null;
        String email = null;
        try {
            tp2 = om2.readValue(infoResponseBody, UserProperties.class);
            email = tp2.getKakao_account().getEmail();
            System.out.println(email);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 로그인
        // 6. 해당 email로 회원가입되어 있는 user 정보가 있는지 db 조회
        User user = userRepository.findByEmail(email);
        // 7. 존재하지 않을 시, 강제 회원가입 후, 그 user 정보로 session 생성(자동 로그인)

        if (user == null) {
            userRepository.insert("kakao_" + email, RandomPasswordGenerator.generateRandomPassword(), email, "kakao");
        }

        User principal = userRepository.findByEmail(email);

        session.setAttribute("principal", principal);

        // 8. 존재할 시, user 정보로 session 생성(자동 로그인)
        if (session.getAttribute("principal") == null) {
            System.out.println("로그인 실패");
        }

        return "인증 완료";
    }
}
