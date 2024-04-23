package com.meli.be_java_hisp_w26_g09.dto;

import com.meli.be_java_hisp_w26_g09.entity.Product;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDTO implements Serializable {
    private Integer userId;
    private Date date;
    private ProductDTO product;
    private Integer category;
    private Double price;
    private Boolean has_promo;
    private Double discount;
}
