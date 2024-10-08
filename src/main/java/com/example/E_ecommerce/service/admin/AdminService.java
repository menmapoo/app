package com.example.E_ecommerce.service.admin;

import com.example.E_ecommerce.dto.CategoryDTO;
import com.example.E_ecommerce.dto.ProductDTO;
import com.example.E_ecommerce.entities.Category;
import com.example.E_ecommerce.entities.Product;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    Category createCategory(CategoryDTO categoryDTO);

    List<CategoryDTO> getAllCategories();

    Product postProduct(Long categoryId, ProductDTO productDTO)throws IOException;

    List<ProductDTO> getAllproducts();

    void deleteProduct(Long id);

    ProductDTO getProductById(Long id);

    ProductDTO updateProduct(Long categoryId,Long productId, ProductDTO productDTO) throws IOException;

}
