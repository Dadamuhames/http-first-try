package org.example;

import java.lang.reflect.Method;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.example.annotation.Controller;
import org.example.annotation.GetMethod;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

@Slf4j
public class ControllersLoader {
  public void loadControllers() throws Exception {
    Reflections reflections = new Reflections(new ConfigurationBuilder().forPackage("org.example"));

    Set<String> controllers = reflections.get(Scanners.TypesAnnotated.with(Controller.class));

    for (String classPath : controllers) {

      Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(classPath);

      Object instance = clazz.getDeclaredConstructor().newInstance();

      for (Method method : clazz.getMethods()) {
        if (method.isAnnotationPresent(GetMethod.class)) {
          String path = method.getAnnotation(GetMethod.class).value();

          BeanContainer.controllerMethods.put(String.format("GET[%s]", path), method);

          BeanContainer.controllers.put(clazz.getName(), instance);
        }
      }
    }
  }
}
