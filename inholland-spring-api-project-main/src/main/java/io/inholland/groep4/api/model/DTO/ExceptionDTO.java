package io.inholland.groep4.api.model.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExceptionDTO {
    private String message;

    public ExceptionDTO(String message) {
        this.message = message;
    }
}
