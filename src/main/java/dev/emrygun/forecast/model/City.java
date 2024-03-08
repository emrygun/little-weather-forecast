package dev.emrygun.forecast.model;

import jakarta.validation.constraints.NotEmpty;

public record City(
        @NotEmpty String name,
        String state,
        String country
) { }
