package org.selenide.examples.testng;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * The MavenDependencyScanner class provides methods to scan for throwable classes in a given package.
 */
public class MavenDependencyScanner {

    /**
     * The main method of the MavenDependencyScanner class.
     * It demonstrates how to find and print throwable classes in a package.
     *
     * @param args The command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        List<Class<?>> throwableClasses = findThrowableClasses("*", Throwable.class);
        for (Class<?> clazz : throwableClasses) {
            if (AssertionError.class.isAssignableFrom(clazz)) {
                System.out.println("------------------" + clazz.getName());
            }
            System.out.println(clazz.getName());
        }
    }

    /**
     * Finds throwable classes in the specified package that are assignable from the given instance.
     *
     * @param packageName The name of the package to scan.
     * @param instance    The class used as a reference to find matching classes.
     * @return A list of classes that are throwable and assignable from the given instance.
     */
    public static List<Class<?>> findThrowableClasses(String packageName, Class<?> instance) {
        List<Class<?>> throwableClasses = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AssignableTypeFilter(instance));
        for (BeanDefinition bd : scanner.findCandidateComponents(packageName)) {
            try {
                Class<?> clazz = Class.forName(bd.getBeanClassName());
                if (!Modifier.isAbstract(clazz.getModifiers())) {
                    throwableClasses.add(clazz);
                }
            } catch (NoClassDefFoundError | ClassNotFoundException ignored) {
            }
        }
        
        return throwableClasses;
    }
}
