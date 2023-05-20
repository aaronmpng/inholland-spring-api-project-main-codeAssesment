package io.inholland.groep4.api.model.DTO;

import io.inholland.groep4.api.model.User;
import io.inholland.groep4.api.model.UserAccount;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserAccountDTO {

    @Schema(example = "current", required = true, description = "")
    @NotNull
    private UserAccount.AccountTypeEnum accountType;

    @Schema(example = "1", required = true, description = "")
    @NotNull
    private User owner;

    @Schema(example = "100", required = true, description = "")
    @NotNull
    private Double lowerLimit;

    @Schema(example = "active", required = true, description = "")
    @NotNull
    private UserAccount.AccountStatusEnum accountStatus;
}
