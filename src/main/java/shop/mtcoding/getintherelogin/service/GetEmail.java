package shop.mtcoding.getintherelogin.service;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import shop.mtcoding.getintherelogin.dto.OAuthProfile;
import shop.mtcoding.getintherelogin.util.Fetch;

@Service
public class GetEmail {

    @Transactional
    public String 이메일받기(String accessToken) {
        // 1. ACCESS 토큰으로 email 정보 받기
        String kakaoInfoUrl = "https://kapi.kakao.com/v2/user/me";
        String infoResponseBody = Fetch.kakaoInfo(kakaoInfoUrl, HttpMethod.POST, accessToken).getBody();

        ObjectMapper om = new ObjectMapper();

        try {
            OAuthProfile oAuthProfile = om.readValue(infoResponseBody, OAuthProfile.class);
            String email = oAuthProfile.getKakaoAccount().getEmail();
            return email;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Transactional
    public Long 아이디받기(String accessToken) {
        // 1. ACCESS 토큰으로 email 정보 받기
        String kakaoInfoUrl = "https://kapi.kakao.com/v2/user/me";
        String infoResponseBody = Fetch.kakaoInfo(kakaoInfoUrl, HttpMethod.POST, accessToken).getBody();

        ObjectMapper om = new ObjectMapper();

        try {
            OAuthProfile oAuthProfile = om.readValue(infoResponseBody, OAuthProfile.class);
            Long id = oAuthProfile.getId();
            return id;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
