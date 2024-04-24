package com.meli.be_java_hisp_w26_g09.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum Role {

    SELLER(1, "Seller"),
    CUSTOMER(2, "Customer");

    private Integer idRole;
    private String nameRole;

    @JsonCreator
    public static Role fromString(String name) {
        for (Role role : Role.values()) {
            if (role.nameRole.equalsIgnoreCase(name)) {
                return role;
            }
        }
        throw new IllegalArgumentException("No enum constant " + Role.class + " with name " + name);
    }

    @JsonValue
    public String getNameRole() {
        return nameRole;
    }
}
