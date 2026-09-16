package com.erebelo.springprofileservice.validation;

/**
 * Thread-bound context for managing soft validation states.
 */
public final class SoftValidationContext {

    private static final ThreadLocal<Boolean> ACTIVE = ThreadLocal.withInitial(() -> false);

    private SoftValidationContext() {
    }

    /**
     * Checks if soft validation is active on the current thread.
     */
    public static boolean isActive() {
        return ACTIVE.get();
    }

    /**
     * Activates the soft validation context. MUST be managed inside a
     * try-with-resources block.
     */
    public static Scope activate() {
        ACTIVE.set(true);
        return SoftValidationContext::clear;
    }

    /**
     * Removes the soft validation state from the current thread to prevent memory
     * leaks.
     */
    private static void clear() {
        ACTIVE.remove();
    }

    /**
     * Scoped resource that ensures clean up on close.
     */
    @FunctionalInterface
    public interface Scope extends AutoCloseable {

        @Override
        void close();
    }
}
