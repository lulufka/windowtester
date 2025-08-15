package com.windowtester.internal.debug;

public interface IStatus {

  String getMessage();

  Throwable getException();

  IStatus[] getChildren();
}
