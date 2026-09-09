package com.erebelo.springprofileservice.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Validates entities using Bean Validation and separates violations into hard
 * and soft validation failures.
 * <p>
 * Hard validation failures are returned as constraint violations, while soft
 * validation failures are stored in the entity's {@code softValidationFailures}
 * property.
 */
@Service
@RequiredArgsConstructor
public class ValidationService {

    private final Validator validator;

    /**
     * Validates the entity and separates hard and soft validation failures.
     * <p>
     * When soft validation failures are present, they are added to the entity's
     * {@code softValidationFailures} property.
     *
     * @param entity
     *            entity to validate
     * @return hard validation violations, or an empty set when no hard violations
     *         are present
     */
    public Set<ConstraintViolation<Object>> validate(Object entity) {
        Set<ConstraintViolation<Object>> violations = validator.validate(entity);

        if (violations.isEmpty()) {
            return Set.of();
        }

        if (!(entity instanceof SoftValidationAware softValidationAware)) {
            // No SoftValidationAware means everything is hard-validation.
            return violations;
        }

        Map<Boolean, List<ConstraintViolation<Object>>> classified = violations.stream()
                .collect(Collectors.partitioningBy(this::isSoftValidation));

        List<ConstraintViolation<Object>> hardViolations = classified.get(false);

        if (!hardViolations.isEmpty()) {
            return new HashSet<>(hardViolations);
        }

        List<SoftValidationFailure> failures = classified.get(true).stream().map(this::toFailure).toList();

        softValidationAware.setSoftValidationFailures(failures.isEmpty() ? null : failures);

        return Set.of();
    }

    private boolean isSoftValidation(ConstraintViolation<?> violation) {
        String fieldName = getLeafFieldName(violation.getPropertyPath());

        Object leafBean = violation.getLeafBean();

        if (leafBean == null || fieldName == null) {
            return false;
        }

        Field field = findField(leafBean.getClass(), fieldName);

        return field != null && field.isAnnotationPresent(SoftValidation.class);
    }

    private String getLeafFieldName(Path path) {
        Path.Node leafNode = null;

        for (Path.Node node : path) {
            if (node.getName() != null) {
                leafNode = node;
            }
        }

        return leafNode != null ? leafNode.getName() : null;
    }

    private Field findField(Class<?> type, String fieldName) {
        Class<?> currentType = type;

        while (currentType != null) {
            try {
                return currentType.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                currentType = currentType.getSuperclass();
            }
        }

        return null;
    }

    private SoftValidationFailure toFailure(ConstraintViolation<?> violation) {
        return SoftValidationFailure.builder().field(violation.getPropertyPath().toString())
                .message(violation.getMessage()).build();
    }
}
