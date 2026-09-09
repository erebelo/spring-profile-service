package com.erebelo.springprofileservice.validation;

import java.util.List;

public interface SoftValidationAware {

    void setSoftValidationFailures(List<SoftValidationFailure> failures);

}
