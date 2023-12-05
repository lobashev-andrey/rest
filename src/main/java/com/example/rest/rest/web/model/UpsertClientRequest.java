package com.example.rest.rest.web.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpsertClientRequest {

    @NotBlank(message = "Имя клиента должно быть заполнено")
    @Size(min = 3, max = 30, message = "Имя клиента может быть не меньше {min} и не больше {max}!")
    private String name;
}
