package org.example;

import java.lang.reflect.Method;
import java.util.Set;
import org.example.annotation.Controller;
import org.example.annotation.GetMethod;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

public class ControllersLoader {
  public void loadControllers() {
    Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.example"));

    Set<String> controllers = reflections.get(Scanners.TypesAnnotated.with(Controller.class));

    for (String classPath : controllers) {
      try {
        Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(classPath);

        Object instance = clazz.getDeclaredConstructor().newInstance();

        for (Method method : clazz.getMethods()) {
          if (method.isAnnotationPresent(GetMethod.class)) {
            String path = method.getAnnotation(GetMethod.class).value();

            BeanContainer.controllerMethods.put(String.format("GET[%s]", path), method);

            BeanContainer.controllers.put(clazz.getName(), instance);
          }
        }

      } catch (Exception exception) {
        exception.printStackTrace();
      }
    }
  }
}
