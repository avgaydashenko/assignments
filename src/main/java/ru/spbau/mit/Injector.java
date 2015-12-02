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

        for (String implClassName : classesList)
            if (expectedClass.isAssignableFrom(Class.forName(implClassName)))
                if (parentFound)
                    throw new AmbiguousImplementationException();
                else {
                    parentFound = true;
                    parentName = implClassName;
                }

        if (!parentFound)
            throw new ImplementationNotFoundException();
        else {
            parentClass = Class.forName(parentName);
            if (classesMap.containsKey(parentName))
                if (classesMap.get(parentName))
                    return parentClass;
                else
                    throw new InjectionCycleException();
        }

        classesMap.put(parentName, false);

        Constructor<?> constructor = parentClass.getConstructors()[0];

        List<Object> parameters = new ArrayList<>();

        for (Class<?> implClass : constructor.getParameterTypes())
            parameters.add(get(implClass.getName(), classesList, classesMap));

        classesMap.put(parentName, true);

        result = constructor.newInstance(parameters.toArray());

        return result;
    }

    public static Object initialize(String rootClassName, List<String> implementationClassNames) throws Exception {

        List<String> classesList = new ArrayList<>();
        Map<String, Boolean> classesMap = new HashMap<String, Boolean>();

        for (String implementationClass : implementationClassNames) classesList.add(implementationClass);
        classesList.add(rootClassName);
        return get(rootClassName, classesList, classesMap);
    }
}