package com.meli.be_java_hisp_w26_g09.entity;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {
    private Integer userId;
    private Date date;
    private Product product;
    private Integer category;
    private Double price;
    private Boolean has_promo;
    private Double discount;
}
