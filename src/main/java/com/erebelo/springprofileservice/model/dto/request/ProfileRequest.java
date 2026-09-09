package com.erebelo.springprofileservice.model.dto.request;

import com.erebelo.springprofileservice.model.enums.ContactType;
import com.erebelo.springprofileservice.model.enums.Gender;
import com.erebelo.springprofileservice.model.enums.MaritalStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import org.jspecify.annotations.NonNull;

@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public record ProfileRequest(String firstName, String lastName,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth, Integer numberOfDependents,
        BigDecimal estimatedAnnualIncome, BigDecimal estimatedNetWorth, Gender gender, MaritalStatus maritalStatus,
        List<String> emailAddresses, List<ContactRequest> contacts, AddressRequest address) {

    @Override
    public @NonNull String toString() {
        return "ProfileRequest{" + "firstName=" + firstName + ", lastName=" + lastName + ", dateOfBirth="
                + (dateOfBirth != null ? "****-**-" + dateOfBirth.getDayOfMonth() : null) + ", numberOfDependents="
                + numberOfDependents + ", estimatedAnnualIncome=" + estimatedAnnualIncome + ", estimatedNetWorth="
                + estimatedNetWorth + ", gender=" + gender + ", maritalStatus=" + maritalStatus + ", emailAddresses="
                + emailAddresses + ", contacts=" + contacts + ", address=" + address + "}";
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContactRequest(ContactType contactType, String contactValue) {
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AddressRequest(String addressLine1, String addressLine2, String city, String state, String country,
            String postalCode) {
    }
}
