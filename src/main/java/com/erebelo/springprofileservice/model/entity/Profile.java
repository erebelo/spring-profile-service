package com.erebelo.springprofileservice.model.entity;

import com.erebelo.springprofileservice.model.enums.ContactType;
import com.erebelo.springprofileservice.model.enums.Gender;
import com.erebelo.springprofileservice.model.enums.MaritalStatus;
import com.erebelo.springprofileservice.validation.SoftValidation;
import com.erebelo.springprofileservice.validation.SoftValidationAware;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "profiles")
public class Profile extends BaseEntity implements SoftValidationAware {

    @Id
    private String id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @SoftValidation
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @SoftValidation
    @Min(value = 0)
    @Max(value = 20)
    private Integer numberOfDependents;

    @SoftValidation
    @DecimalMin(value = "0")
    @DecimalMax(value = "9999999.99")
    private BigDecimal estimatedAnnualIncome;

    @SoftValidation
    @DecimalMin(value = "-9999999.99")
    @DecimalMax(value = "9999999.99")
    private BigDecimal estimatedNetWorth;

    private Gender gender;

    @SoftValidation
    @NotNull
    private MaritalStatus maritalStatus;

    @SoftValidation
    @NotEmpty
    private List<@Valid ProfileContact> contacts;

    @SoftValidation
    @Valid
    @NotNull
    private ProfileAddress address;

    private List<SoftValidationFailure> softValidationFailures;

    @ToString.Include(name = "dateOfBirth", rank = 1)
    public String maskDateOfBirth() {
        if (dateOfBirth != null) {
            return "****-**-" + dateOfBirth.getDayOfMonth();
        }
        return null;
    }

    @Data
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileContact {

        @SoftValidation
        @NotNull
        private ContactType contactType;

        @NotBlank
        private String contactValue;

    }

    @Data
    @Builder(toBuilder = true)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileAddress {

        @SoftValidation
        @NotBlank
        private String addressLine1;

        private String addressLine2;

        @SoftValidation
        @NotBlank
        private String city;

        @SoftValidation
        @NotBlank
        private String state;

        @SoftValidation
        @NotBlank
        private String country;

        @NotBlank
        private String postalCode;

    }
}
