package com.erebelo.springprofileservice.validation;

import com.erebelo.springprofileservice.model.entity.SoftValidationFailure;
import java.util.List;

public interface SoftValidationAware {

    void setSoftValidationFailures(List<SoftValidationFailure> failures);

}
