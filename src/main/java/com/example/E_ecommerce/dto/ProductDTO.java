package com.example.E_ecommerce.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String description;
    private String price;
    private MultipartFile image;
    private byte[] returnedImage;
    private Long categoryId;
    private String categoryName;
}
