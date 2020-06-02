package com.example.Java14SpringBoot.records;

import com.example.Java14SpringBoot.model.Product;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public record ProductDTO(
        @NotBlank String name,
        @Max(1) @Min(0)int status
) {

    public Product toEntity() {
        return new Product(null, this.name, this.status);
    }
}
