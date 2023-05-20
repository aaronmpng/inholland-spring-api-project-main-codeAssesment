package io.inholland.groep4.api.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserDTO {

    @Schema(example = "johndoe", required = true, description = "")
    @NotNull
    private String username;

    @Schema(example = "helloworld", required = true, description = "")
    @NotNull
    private String password;

    @Schema(example = "john", required = true, description = "")
    @NotNull
    private String firstName;

    @Schema(example = "doe", required = true, description = "")
    @NotNull
    private String lastName;

    @Schema(example = "johndoe@email.com", required = true, description = "")
    @NotNull
    private String email;

    @Schema(example = "01/01/1990", required = true, description = "")
    @NotNull
    private String birthdate;
}
