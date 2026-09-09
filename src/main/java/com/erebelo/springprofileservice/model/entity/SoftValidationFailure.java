package com.erebelo.springprofileservice.model.entity;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder(toBuilder = true)
public record SoftValidationFailure(@NotBlank String field, @NotBlank String message) {
}
