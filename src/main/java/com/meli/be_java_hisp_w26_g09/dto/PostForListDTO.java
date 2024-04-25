package com.meli.be_java_hisp_w26_g09.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostForListDTO {
    private Integer userId;
    private Integer postId;
    private Date date;
    private ProductDTO product;
    private Integer category;
    private Double price;

}
