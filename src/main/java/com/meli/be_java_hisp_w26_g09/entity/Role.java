package com.meli.be_java_hisp_w26_g09.entity;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {

    SELLER(1, "Seller"),
    CUSTOMER(2, "Customer");

    private Integer idRole;
    private String nameRole;

}
