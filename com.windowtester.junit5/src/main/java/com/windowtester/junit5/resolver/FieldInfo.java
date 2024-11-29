package com.windowtester.junit5.resolver;

import java.util.Objects;

public record FieldInfo(String title, Object result) {

  @Override
  public boolean equals(Object o) {
    if (o instanceof FieldInfo fieldInfo) {
      return Objects.equals(title, fieldInfo.title)
          && Objects.equals(result, fieldInfo.result);
    }
    return false;
  }

}
