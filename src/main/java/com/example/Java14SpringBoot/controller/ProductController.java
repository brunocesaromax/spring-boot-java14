package com.example.Java14SpringBoot.controller;

import com.example.Java14SpringBoot.enumerate.ProductStatus;
import com.example.Java14SpringBoot.model.Product;
import com.example.Java14SpringBoot.records.ProductDTO;
import com.example.Java14SpringBoot.records.ProductRecord;
import com.example.Java14SpringBoot.repository.ProductRepository;
import com.example.Java14SpringBoot.service.ProductService;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;

    //Usando uma record como um tipo temporário
    record Response(@JsonProperty List<Product>list, @JsonProperty int total) {
    }

    public ProductController(ProductRepository productRepository, ProductService productService) {
        this.productRepository = productRepository;
        this.productService = productService;
    }

    @GetMapping
    public Response findAll() {
        var list = productRepository.findAll();
        return new Response(list, list.size());
    }

    @GetMapping("{id}")
    public @ResponseBody
    ProductRecord findById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping//Pode se utilizar records como dtos
    public ProductRecord create(@Valid @RequestBody ProductDTO productDTO) {
//        return productRepository.save(productDTO.toEntity());
        var status = switch (productDTO.status()) {
            case 1 -> ProductStatus.ACTIVE;
            case 0 -> ProductStatus.INACTIVE;
            default -> throw new IllegalArgumentException("Opção inválida!");
        };

        return productService.create(productDTO.name(), status);
    }
}
