package com.jshop.user.pojo.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserWebVoReq {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}
