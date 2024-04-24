package com.meli.be_java_hisp_w26_g09.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class User2DTO {
    private Integer user_id;
    private String user_name;
    private List<UserResponseDTO> followers;
}
