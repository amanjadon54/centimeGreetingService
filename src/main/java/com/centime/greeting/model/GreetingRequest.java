package com.centime.greeting.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Data
@Getter
@AllArgsConstructor
public class GreetingRequest {
    @NotNull
    @JsonProperty(value = "Name", required = true)
    private String name;

    @NotNull
    @JsonProperty(value = "Sirname", required = true)
    private String sirName;
}
