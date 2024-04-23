package com.meli.be_java_hisp_w26_g09.entity;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {
    private Integer userId;
    private String userName;
    private Boolean seller;
    private List<Integer> followers;
    private List<Integer> followed;
}
