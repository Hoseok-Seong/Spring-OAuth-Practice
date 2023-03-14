package shop.mtcoding.getintherelogin.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProperties {
    private Long id;
    private String connected_at;
    @JsonIgnoreProperties(ignoreUnknown = true)
    private kakao_account kakao_account;

    @Getter
    @Setter
    public class kakao_account {
        private boolean has_email;
        private boolean email_needs_agreement;
        private boolean is_email_valid;
        private boolean is_email_verified;
        private String email;
    }
}
