package com.example.app_clickup.payload;

import com.sun.istack.NotNull;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotNull
    private String fullName;

    @NotNull
    private String email;

    @NotNull
    private String password;
}
