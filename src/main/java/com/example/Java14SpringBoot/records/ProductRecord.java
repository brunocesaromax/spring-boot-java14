package com.example.Java14SpringBoot.records;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductRecord(
        @JsonProperty Long id,
        @JsonProperty String name,
        @JsonProperty int status) {
}
