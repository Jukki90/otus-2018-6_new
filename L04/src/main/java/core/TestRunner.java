package core;

import annotations.After;
import annotations.Before;
import annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    public static void run(Object testclass) {
        startAnnotatedMethods(testclass, Before.class);
        startAnnotatedMethods(testclass, Test.class);
        startAnnotatedMethods(testclass, After.class);

    }

    private static void startAnnotatedMethods(Object instance, Class<? extends Annotation> annotation) {
        List<Method> methods = getMethodsAnnotatedWith(instance.getClass(), annotation);
        methods.forEach(method -> {
            try {
                instance.getClass().getMethod(method.getName()).invoke(instance);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private static List<Method> getMethodsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        List<Method> methods = new ArrayList<>();
        List<Method> allMethods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));
        for (final Method method : allMethods) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }
}
