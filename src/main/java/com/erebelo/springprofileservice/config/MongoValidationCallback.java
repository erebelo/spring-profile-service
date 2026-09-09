package com.erebelo.springprofileservice.config;

import com.erebelo.springprofileservice.validation.ValidationService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;
import org.springframework.stereotype.Component;

/**
 * Validates MongoDB domain objects before they are converted and persisted.
 * Uses {@link ValidationService} to apply hard and soft validation, returning
 * hard violations and storing soft validation failures in the entity. Applies
 * to repository and MongoTemplate save/insert operations, but not to
 * update/upsert operations using Update or AggregationUpdate.
 */
@Component
@RequiredArgsConstructor
public class MongoValidationCallback implements BeforeConvertCallback<@NonNull Object> {

    private final ValidationService validationService;

    @Override
    public Object onBeforeConvert(Object entity, @NonNull String collection) {
        Set<ConstraintViolation<Object>> hardViolations = validationService.validate(entity);

        if (!hardViolations.isEmpty()) {
            throw new ConstraintViolationException(hardViolations);
        }

        return entity;
    }
}
