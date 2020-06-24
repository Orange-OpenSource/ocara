package com.orange.ocara.utils;

import org.junit.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Tool class based on Spring's ReflectionTestUtils
 *
 * Helps on setting fields in some target object, so as to inject some mocking/stubbing behaviour
 *
 * @see <https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/util/ReflectionTestUtils.html>
 * @see <https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/util/ReflectionUtils.html>
 */
public class ReflectionTestUtils {

    /**
     * Set the {@linkplain Field field} with the given {@code name} on the
     * provided {@code targetObject} to the supplied {@code value}.
     *
     * @param targetObject the target object on which to set the field; never {@code null}
     * @param name the name of the field to set; never {@code null}
     * @param value the value to set
     */
    public static void setField(Object targetObject, String name, Object value) {
        Assert.assertTrue(
                "Either targetObject for the field must be specified",
                targetObject != null);

        Class<?> targetClass = targetObject.getClass();

        Field field = findField(targetClass, name);
        if (field == null) {
            throw new IllegalArgumentException(String.format(
                    "Could not find field '%s' on target object [%s] or target class [%s]", name,
                    targetObject, targetClass));
        }

        makeAccessible(field);
        setField(field, targetObject, value);
    }

    private static final Field[] NO_FIELDS = {};

    private static final Map<Class<?>, Field[]> declaredFieldsCache = new HashMap<>(256);

    /**
     * Attempt to find a {@link Field field} on the supplied {@link Class} with the
     * supplied {@code name} and/or {@link Class type}. Searches all superclasses
     * up to {@link Object}.
     *
     * @param clazz the class to introspect
     * @param name the name of the field (may be {@code null} if type is specified)
     *
     * @return the corresponding Field object, or {@code null} if not found
     */
    private static Field findField(Class<?> clazz, String name) {
        Class<?> searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            Field[] fields = getDeclaredFields(searchType);
            for (Field field : fields) {
                if ((name == null || name.equals(field.getName()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * This variant retrieves {@link Class#getDeclaredFields()} from a local cache
     * in order to avoid the JVM's SecurityManager check and defensive array copying.
     * @param clazz the class to introspect
     * @return the cached array of fields
     * @see Class#getDeclaredFields()
     */
    private static Field[] getDeclaredFields(Class<?> clazz) {
        Field[] result = declaredFieldsCache.get(clazz);
        if (result == null) {
            result = clazz.getDeclaredFields();
            declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
        }
        return result;
    }

    /**
     *
     * @param field - the field to make accessible
     *
     * @see <https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/util/ReflectionUtils.html#makeAccessible-java.lang.reflect.Field->
     */
    private static void makeAccessible(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     *
     * @param     field - the field to set
     * @param     target - the target object on which to set the field
     * @param     value - the value to set (may be null)
     *
     * @see <https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/util/ReflectionUtils.html#setField-java.lang.reflect.Field-java.lang.Object-java.lang.Object->
     */
    private static void setField(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalStateException(
                    "Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }
}
