package com.meli.be_java_hisp_w26_g09.entity;

public enum Role {

    SELLER(1, "Seller"),
    CUSTOMER(2, "Customer");

    private Integer idRole;
    private String nameRole;

    Role(Integer idRole, String nameRole) {
        this.idRole = idRole;
        this.nameRole = nameRole;
    }

    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String nameRole) {
        this.nameRole = nameRole;
    }
}
