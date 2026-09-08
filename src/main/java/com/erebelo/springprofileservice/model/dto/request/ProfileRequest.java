package com.erebelo.springprofileservice.model.dto.request;

import com.erebelo.springprofileservice.model.enums.ContactType;
import com.erebelo.springprofileservice.model.enums.Gender;
import com.erebelo.springprofileservice.model.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProfileRequest(

        @NotBlank String firstName,

        @NotBlank String lastName,

        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth,

        Integer numberOfDependents,

        BigDecimal estimatedAnnualIncome,

        BigDecimal estimatedNetWorth,

        Gender gender,

        MaritalStatus maritalStatus,

        List<@Valid ProfileContactRequest> contacts,

        ProfileAddressRequest address) {

    private String maskDateOfBirth() {
        if (dateOfBirth != null) {
            return "****-**-" + dateOfBirth.getDayOfMonth();
        }
        return null;
    }

    @Override
    public @NonNull String toString() {
        return "ProfileRequest[" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
                + ", dateOfBirth='" + maskDateOfBirth() + '\'' + ", numberOfDependents=" + numberOfDependents
                + ", estimatedAnnualIncome=" + estimatedAnnualIncome + ", estimatedNetWorth=" + estimatedNetWorth
                + ", gender=" + gender + ", maritalStatus=" + maritalStatus + ", contacts=" + contacts + ", address="
                + address + ']';
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProfileContactRequest(@NotNull ContactType contactType, @NotBlank String contactValue) {
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ProfileAddressRequest(String address, String city, String state, String country, String postalCode) {
    }
}
