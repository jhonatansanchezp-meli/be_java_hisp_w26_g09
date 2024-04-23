package com.meli.be_java_hisp_w26_g09.dto;

import com.meli.be_java_hisp_w26_g09.entity.Role;
import com.meli.be_java_hisp_w26_g09.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private Integer idRole;
    private String nameRole;
}
