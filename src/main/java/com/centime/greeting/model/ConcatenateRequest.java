package com.centime.greeting.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class ConcatenateRequest {
    private String Name;
    private String Sirname;
}
