package io.inholland.groep4.api.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * UserAccount
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-24T18:28:14.004Z[GMT]")

@Entity
public class UserAccount {

    /**
     * Gets or Sets accountType
     */
    public enum AccountTypeEnum {
        CURRENT("current"),

        SAVINGS("savings");

        private String value;

        AccountTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static AccountTypeEnum fromValue(String text) {
            for (AccountTypeEnum b : AccountTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("id")
    @Id
    @GeneratedValue
    private Long id = null;

    @JsonProperty("accountType")
    private AccountTypeEnum accountType = null;

    @JsonProperty("IBAN")
    private String IBAN = null;

    @JsonProperty("owner")
    @ManyToOne
    @JsonBackReference
    private User owner;

    @JsonProperty("accountBalance")
    private Double accountBalance = null;

    @JsonProperty("lowerLimit")
    private Double lowerLimit = null;

    public UserAccount(AccountTypeEnum accountType, String IBAN, User owner, Double accountBalance, Double lowerLimit, AccountStatusEnum accountStatus) {
        this.accountType = accountType;
        this.IBAN = IBAN;
        this.owner = owner;
        this.accountBalance = accountBalance;
        this.lowerLimit = lowerLimit;
        this.accountStatus = accountStatus;
    }

    public UserAccount() {
    }

    /**
     * Gets or Sets accountStatus
     */
    public enum AccountStatusEnum {
        ACTIVE("active"),

        INACTIVE("inactive");

        private String value;

        AccountStatusEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static AccountStatusEnum fromValue(String text) {
            for (AccountStatusEnum b : AccountStatusEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("accountStatus")
    private AccountStatusEnum accountStatus = null;

    public UserAccount accountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
        return this;
    }

    /**
     * Get accountType
     *
     * @return accountType
     **/
    @Schema(example = "current", required = true, description = "")
    @NotNull

    public AccountTypeEnum getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeEnum accountType) {
        this.accountType = accountType;
    }

    public UserAccount IBAN(String IBAN) {
        this.IBAN = IBAN;
        return this;
    }

    /**
     * Get IBAN
     *
     * @return IBAN
     **/
    @Schema(example = "NL91 ABNA 0417 1643 00", required = true, description = "")
    @NotNull

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public UserAccount accountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
        return this;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Get accountBalance
     * minimum: -999
     *
     * @return accountBalance
     **/
    @Schema(example = "1271.56", required = true, description = "")
    @NotNull

    @DecimalMin("-999")
    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public UserAccount lowerLimit(Double lowerLimit) {
        this.lowerLimit = lowerLimit;
        return this;
    }

    /**
     * Get lowerLimit
     * minimum: -1000
     *
     * @return lowerLimit
     **/
    @Schema(example = "0", required = true, description = "")
    @NotNull

    @DecimalMin("-1000")
    public Double getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(Double lowerLimit) {
        if (lowerLimit < 0 ) {
            throw new IllegalArgumentException("Lower limit cannot be negative!");
        }
        this.lowerLimit = lowerLimit;
    }

    public UserAccount accountStatus(AccountStatusEnum accountStatus) {
        this.accountStatus = accountStatus;
        return this;
    }

    /**
     * Get accountStatus
     *
     * @return accountStatus
     **/
    @Schema(example = "active", description = "")

    public AccountStatusEnum getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(AccountStatusEnum accountStatus) {
        this.accountStatus = accountStatus;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAccount userAccount = (UserAccount) o;
        return Objects.equals(this.accountType, userAccount.accountType) &&
                Objects.equals(this.IBAN, userAccount.IBAN) &&
                Objects.equals(this.accountBalance, userAccount.accountBalance) &&
                Objects.equals(this.lowerLimit, userAccount.lowerLimit) &&
                Objects.equals(this.accountStatus, userAccount.accountStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountType, IBAN, accountBalance, lowerLimit, accountStatus);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class UserAccount {\n");

        sb.append("    accountType: ").append(toIndentedString(accountType)).append("\n");
        sb.append("    IBAN: ").append(toIndentedString(IBAN)).append("\n");
        sb.append("    accountBalance: ").append(toIndentedString(accountBalance)).append("\n");
        sb.append("    lowerLimit: ").append(toIndentedString(lowerLimit)).append("\n");
        sb.append("    accountStatus: ").append(toIndentedString(accountStatus)).append("\n");
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
