package ru.spbau.mit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Injector {

    /**
     * Create and initialize object of `rootClassName` class using classes from
     * `implementationClassNames` for concrete dependencies.
     */

    private static Object get(String expectedClassName, List<String> classesList, Map<String, Boolean> classesMap)
            throws ClassNotFoundException, AmbiguousImplementationException, ImplementationNotFoundException,
            InjectionCycleException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class<?> expectedClass = Class.forName(expectedClassName);

        Object result;

        boolean parentFound = false;
        String parentName = null;
        Class<?> parentClass;


        System.out.println("3");


        for (String implClassName : classesList)
            if (expectedClass.isAssignableFrom(Class.forName(implClassName)))
                if (parentFound)
                    throw new AmbiguousImplementationException();
                else {
                    parentFound = true;
                    parentName = implClassName;
                }


        System.out.println("4");

        if (!parentFound)
            throw new ImplementationNotFoundException();
        else {
            parentClass = Class.forName(parentName);
            if (classesMap.containsKey(parentName))
                if (classesMap.get(parentName))
                    result = parentClass;
                else
                    throw new InjectionCycleException();
        }


        System.out.println("5");

        //classesMap.put(parentName, true);

        Constructor<?> constructor = parentClass.getConstructors()[0];

        List<Object> parameters = new ArrayList<>();

        System.out.println("6");

        for (Class<?> implClass : constructor.getParameterTypes())
            parameters.add(get(implClass.getName(), classesList, classesMap));

        System.out.println("7");

        result = constructor.newInstance(parameters.toArray());

        System.out.println("8");

        return result;
    }

    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {

        List<String> classesList = new ArrayList<>();
        Map<String, Boolean> classesMap = new HashMap<String, Boolean>();

        for (String implementationClass : implementationClassNames) classesList.add(implementationClass);
        System.out.println("1");
        classesList.add(rootClassName);
        System.out.println("2");
        return get(rootClassName, classesList, classesMap);
    }
}