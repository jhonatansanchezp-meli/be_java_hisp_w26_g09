package com.meli.be_java_hisp_w26_g09.entity;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    public static final Integer ID_CUSTOMER = 1;
    public static final Integer ID_SELLER = 2;

    private Integer idRole;
    private String nameRole;

}
