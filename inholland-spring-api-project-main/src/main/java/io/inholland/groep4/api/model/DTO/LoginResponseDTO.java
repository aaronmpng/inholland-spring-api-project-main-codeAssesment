package io.inholland.groep4.api.model.DTO;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * AuthenticateUserItem
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-24T18:28:14.004Z[GMT]")

public class LoginResponseDTO {
    @JsonProperty("token")
    private String token = null;

    public LoginResponseDTO token(String token) {
        this.token = token;
        return this;
    }

    /**
     * Get token
     *
     * @return token
     **/
    @Schema(example = "d290f1ee-6c54-4b01-90e6-d701748f0851", required = true, description = "")
    @NotNull

    @Valid
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LoginResponseDTO authenticateUserItem = (LoginResponseDTO) o;
        return Objects.equals(this.token, authenticateUserItem.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AuthenticateUserItem {\n");

        sb.append("    token: ").append(toIndentedString(token)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
