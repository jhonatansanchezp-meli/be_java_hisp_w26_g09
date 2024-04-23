package com.meli.be_java_hisp_w26_g09.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO implements Serializable {
    private Integer userId;
    private String userName;
    private Boolean seller;
}
