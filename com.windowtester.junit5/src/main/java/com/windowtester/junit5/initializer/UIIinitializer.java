package com.windowtester.junit5.initializer;

public interface UIIinitializer<K> {

  K renderUI();

  void closeUI(K k);
}
