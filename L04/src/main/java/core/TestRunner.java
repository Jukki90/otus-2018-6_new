package core;

import annotations.After;
import annotations.Before;
import annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner {
    public static void run(Class clazz, Object... args) {
        List<Method> methods = getMethodsAnnotatedWith(clazz, Test.class);
        methods.forEach(method -> {
            try {
                Object testObj = instantiateClass(clazz, args);
                startAnnotatedMethods(testObj, Before.class);
                clazz.getMethod(method.getName()).invoke(testObj);
                startAnnotatedMethods(testObj, After.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private static <T> T instantiateClass(Class<T> type, Object... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (args.length == 0) {
            return type.getDeclaredConstructor().newInstance();
        } else {
            Class<?>[] classes = toClasses(args);
            return type.getDeclaredConstructor(classes).newInstance(args);
        }
    }

    private static Class<?>[] toClasses(Object[] args) {
        return Arrays.stream(args).map(Object::getClass).toArray(Class[]::new);
    }


    private static void startAnnotatedMethods(Object object, Class<? extends Annotation> annotation) {
        List<Method> methods = getMethodsAnnotatedWith(object.getClass(), annotation);
        methods.forEach(method -> {
            try {
                object.getClass().getMethod(method.getName()).invoke(object);
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
