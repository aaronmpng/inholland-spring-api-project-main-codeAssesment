package io.inholland.groep4.api.model.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionDTO {

    @Schema(example = "NL91 ABNA 0417 1643 00", required = true, description = "")
    @NotNull
    private String sender;

    @Schema(example = "NL91 ABNA 0417 1643 00", required = true, description = "")
    @NotNull
    private String receiver;

    @Schema(example = "12345.67", required = true, description = "")
    @NotNull
    private double amount;

    @Schema(example = "Rolex", required = true, description = "")
    @NotNull
    private String description;
}
