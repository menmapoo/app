package com.example.E_ecommerce.service.admin;

import com.example.E_ecommerce.dto.CategoryDTO;
import com.example.E_ecommerce.dto.ProductDTO;
import com.example.E_ecommerce.entities.Category;
import com.example.E_ecommerce.entities.Product;
import com.example.E_ecommerce.repository.CategoryRepository;
import com.example.E_ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProductRepository productRepository;

    @Override
    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream().map(Category::getCategoryDTO).collect(Collectors.toList());
    }

    @Override
    public Product postProduct(Long categoryId, ProductDTO productDTO) throws IOException {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (optionalCategory.isPresent()) {
            Product product = new Product();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setImage(productDTO.getImage().getBytes());
            product.setCategory(optionalCategory.get());
            return productRepository.save(product);
        }
        return null;
    }

    @Override
    public List<ProductDTO> getAllproducts() {
        return productRepository.findAll().stream().map(Product::getProductDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty())
            throw new IllegalArgumentException("Product with id "+id+" not found");
        productRepository.deleteById(id);

    }

    @Override
    public ProductDTO getProductById(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            return product.getProductDTO();
        }
        return null;
    }

    @Override
    public ProductDTO updateProduct(Long categoryId, Long productId, ProductDTO productDTO) throws IOException {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (optionalCategory.isPresent() && optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setCategory(optionalCategory.get());

            // Check if the image is provided and not empty
            if (productDTO.getImage() != null && !productDTO.getImage().isEmpty()) {
                // Convert MultipartFile to byte array
                product.setImage(productDTO.getImage().getBytes());
            }

            // Save updated product
            Product updatedProduct = productRepository.save(product);

            // Convert updated product to DTO
            ProductDTO updatedProductDto = new ProductDTO();
            updatedProductDto.setId(updatedProduct.getId());
            updatedProductDto.setName(updatedProduct.getName());
            updatedProductDto.setDescription(updatedProduct.getDescription());
            updatedProductDto.setPrice(updatedProduct.getPrice());

            return updatedProductDto;
        }

        return null;
    }
}
