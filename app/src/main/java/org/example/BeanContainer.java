package org.example;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanContainer {
  public static final Map<String, Method> controllerMethods = new HashMap<>();

  public static final Map<String, Object> controllers = new HashMap<>();
}
