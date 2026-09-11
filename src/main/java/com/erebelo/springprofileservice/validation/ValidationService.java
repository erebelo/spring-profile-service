package com.erebelo.springprofileservice.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ElementKind;
import jakarta.validation.Path;
import jakarta.validation.Validator;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
            // No SoftValidationAware means everything is hard validation
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
        Path.Node leafNode = getLeafNode(violation.getPropertyPath());

        if (leafNode == null) {
            return false;
        }

        // TYPE_USE: e.g. List<@SoftValidation @NotBlank String> emailAddresses
        if (leafNode.getKind() == ElementKind.CONTAINER_ELEMENT) {
            return isSoftContainerElement(violation);
        }

        if (leafNode.getKind() != ElementKind.PROPERTY) {
            return false;
        }

        Object leafBean = violation.getLeafBean();

        if (leafBean == null || leafNode.getName() == null) {
            return false;
        }

        String propertyName = leafNode.getName();

        // FIELD: e.g. @SoftValidation @NotBlank String firstName
        Field field = findField(leafBean.getClass(), propertyName);

        if (field != null && field.isAnnotationPresent(SoftValidation.class)) {
            return true;
        }

        // METHOD: e.g. @SoftValidation @AssertTrue boolean isAtLeast18YearsOld()
        return isSoftValidationMethod(leafBean.getClass(), propertyName);
    }

    private boolean isSoftValidationMethod(Class<?> type, String propertyName) {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
                if (!property.getName().equals(propertyName)) {
                    continue;
                }

                Method readMethod = property.getReadMethod();

                return readMethod != null && readMethod.isAnnotationPresent(SoftValidation.class);
            }
        } catch (IntrospectionException ignored) {
            return false;
        }

        return false;
    }

    private boolean isSoftContainerElement(ConstraintViolation<?> violation) {
        String fieldName = getFirstFieldName(violation.getPropertyPath());

        if (fieldName == null || violation.getRootBean() == null) {
            return false;
        }

        Field field = findField(violation.getRootBean().getClass(), fieldName);

        if (field == null) {
            return false;
        }

        AnnotatedType type = field.getAnnotatedType();

        if (!(type instanceof AnnotatedParameterizedType parameterizedType)) {
            return false;
        }

        AnnotatedType elementType = parameterizedType.getAnnotatedActualTypeArguments()[0];

        return elementType.isAnnotationPresent(SoftValidation.class);
    }

    private Path.Node getLeafNode(Path path) {
        Path.Node leafNode = null;

        for (Path.Node node : path) {
            leafNode = node;
        }

        return leafNode;
    }

    private String getFirstFieldName(Path path) {
        for (Path.Node node : path) {
            if (node.getKind() == ElementKind.PROPERTY) {
                return node.getName();
            }
        }

        return null;
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
        return SoftValidationFailure.builder()
                .field(violation.getPropertyPath().toString().replace(".<list element>", ""))
                .message(violation.getMessage()).build();
    }
}
