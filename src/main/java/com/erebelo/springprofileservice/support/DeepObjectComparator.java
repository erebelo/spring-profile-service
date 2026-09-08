package com.erebelo.springprofileservice.support;

import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.databind.introspect.AnnotatedMember;
import tools.jackson.databind.introspect.JacksonAnnotationIntrospector;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.node.ObjectNode;

/**
 * Simple deep comparator using Jackson.
 *
 * <pre>
 * Supports:
 * - Deep comparison for nested objects and collections.
 * - Polymorphism (runtime type included).
 * - Any POJO (no annotations required).
 * - Spring Data @Transient properties excluded from comparison.
 *
 * Limitations:
 * - Collection order matters (List, Set, Map).
 * - Not suitable for cyclic object graphs.
 * - Equality is based on the configured Jackson serialization view.
 *
 * Comparison semantics:
 * - Equality is based on Jackson's serialization view.
 * - Fields ignored by Jackson (e.g. transient (Java keyword), @JsonIgnore, etc.) are not included in the comparison.
 * - Spring Data @Transient properties are explicitly excluded from comparison.
 * - Runtime types are included in the comparison.
 * </pre>
 */
@Component
public class DeepObjectComparator {

    /**
     * Uses a dedicated ObjectMapper to exclude Spring Data's @Transient properties
     * without modifying the global ObjectMapper configuration.
     */
    private final ObjectMapper mapper;

    public DeepObjectComparator() {
        this.mapper = JsonMapper.builder().findAndAddModules()
                .annotationIntrospector(new SpringDataTransientIntrospector()).build();
    }

    /**
     * Compares two JSON trees for deep equality.
     */
    public boolean deepEquals(JsonNode left, JsonNode right) {
        return left.equals(right);
    }

    /**
     * Converts an object to a JSON tree and includes its runtime class to preserve
     * polymorphism during comparison.
     */
    public JsonNode toTypedTree(Object object) {
        if (object == null) {
            return mapper.nullNode();
        }

        JsonNode tree = mapper.valueToTree(object);

        if (tree.isObject()) {
            ObjectNode objNode = (ObjectNode) tree;
            objNode.put("@class", object.getClass().getName());
        }

        return tree;
    }

    /**
     * Makes Spring Data's @Transient behave like a Jackson ignored property for
     * this comparator only.
     */
    private static final class SpringDataTransientIntrospector extends JacksonAnnotationIntrospector {
        @Override
        public boolean hasIgnoreMarker(MapperConfig<?> config, AnnotatedMember member) {
            return member.hasAnnotation(Transient.class) || super.hasIgnoreMarker(config, member);
        }
    }
}
