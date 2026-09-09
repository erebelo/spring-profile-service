package com.erebelo.springprofileservice.validation;

import java.util.List;

/**
 * Defines the contract for objects that support soft validation by exposing a
 * {@code softValidationFailures} property that can be populated with validation
 * failures.
 */
public interface SoftValidationAware {

    void setSoftValidationFailures(List<SoftValidationFailure> failures);

}
