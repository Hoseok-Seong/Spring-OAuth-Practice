package shop.mtcoding.getintherelogin.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import shop.mtcoding.getintherelogin.dto.TokenProperties;
import shop.mtcoding.getintherelogin.util.Fetch;

@Service
public class GetToken {

    @Transactional
    public String 토큰받기(String code) {
        // 1. code 값 카카오 전달 => access token 받기
        String kakaoTokenUrl = "https://kauth.kakao.com/oauth/token";

        // 2. body 객체 만들기
        MultiValueMap<String, String> xForm = new LinkedMultiValueMap<>();
        xForm.add("grant_type", "authorization_code");
        xForm.add("client_id", "");
        xForm.add("redirect_uri", "http://localhost:8080/callback"); // 2차 검증
        xForm.add("code", code); // 핵심

        // 3. ACCESS 토큰 받기, 카카오의 유저의 resource 접근 가능해짐
        String tokenResponseBody = Fetch.kakaoToken(kakaoTokenUrl, HttpMethod.POST, xForm).getBody();

        // 4. ACCESS 토큰 파싱
        ObjectMapper om = new ObjectMapper();
        om.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        try {
            TokenProperties tp = om.readValue(tokenResponseBody, TokenProperties.class);
            String accessToken = tp.getAccess_token();
            return accessToken;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
