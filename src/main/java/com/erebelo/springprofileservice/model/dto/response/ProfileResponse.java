package com.erebelo.springprofileservice.model.dto.response;

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
public record ProfileResponse(String id, String firstName, String lastName,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate dateOfBirth, Integer numberOfDependents,
        BigDecimal estimatedAnnualIncome, BigDecimal estimatedNetWorth, Gender gender, MaritalStatus maritalStatus,
        List<ContactResponse> contacts, AddressResponse address) {

    @Override
    public @NonNull String toString() {
        return "ProfileResponse{" + "id=" + id + ", firstName=" + firstName + ", lastName=" + lastName
                + ", dateOfBirth=" + (dateOfBirth != null ? "****-**-" + dateOfBirth.getDayOfMonth() : null)
                + ", numberOfDependents=" + numberOfDependents + ", estimatedAnnualIncome=" + estimatedAnnualIncome
                + ", estimatedNetWorth=" + estimatedNetWorth + ", gender=" + gender + ", maritalStatus=" + maritalStatus
                + ", contacts=" + contacts + ", address=" + address + '}';
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ContactResponse(ContactType contactType, String contactValue) {
    }

    @Builder(toBuilder = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record AddressResponse(String addressLine1, String addressLine2, String city, String state, String country,
            String postalCode) {
    }
}
