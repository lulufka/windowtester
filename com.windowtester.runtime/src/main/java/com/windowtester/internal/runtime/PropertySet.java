/*******************************************************************************
 *  Copyright (c) 2012 Google, Inc.
 *  All rights reserved. This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Contributors:
 *  Google, Inc. - initial API and implementation
 *  Frederic Gurr - added checked property
 *******************************************************************************/
package com.windowtester.internal.runtime;

import com.windowtester.internal.debug.LogHandler;
import com.windowtester.runtime.IAdaptable;
import com.windowtester.runtime.IUIContext;
import com.windowtester.runtime.condition.HasFocus;
import com.windowtester.runtime.condition.HasFocusCondition;
import com.windowtester.runtime.condition.IsChecked;
import com.windowtester.runtime.condition.IsCheckedCondition;
import com.windowtester.runtime.condition.IsEnabled;
import com.windowtester.runtime.condition.IsEnabledCondition;
import com.windowtester.runtime.condition.IsSelected;
import com.windowtester.runtime.condition.IsSelectedCondition;
import com.windowtester.runtime.condition.IsVisible;
import com.windowtester.runtime.condition.IsVisibleCondition;
import com.windowtester.runtime.locator.ILocator;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PropertySet implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  // for testing
  public static class TestStub extends PropertySet {

    @Serial
    private static final long serialVersionUID = 1L;

    public TestStub() {
      super(null, null);
    }
  }

  public interface IPropertyProvider {

    PropertyMapping[] getProperties(IUIContext ui);
  }

  public static class PropertyMapping implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public static final PropertyMapping ENABLED =
        PropertyMapping.withKey("isEnabled").withName("Is Enabled");
    public static final PropertyMapping SELECTED =
        PropertyMapping.withKey("isSelected").withName("Is Selected");
    public static final PropertyMapping CHECKED =
        PropertyMapping.withKey("isChecked").withName("Is Checked");
    public static final PropertyMapping VISIBLE =
        PropertyMapping.withKey("isVisible").withName("Is Visible");
    public static final PropertyMapping FOCUS =
        PropertyMapping.withKey("hasFocus").withName("Has Focus");
    public static final PropertyMapping TEXT =
        PropertyMapping.withKey("hasText").withName("Has Text");
    public static final PropertyMapping ACTIVE =
        PropertyMapping.withKey("isActive").withName("Is Active");

    private boolean flagged;

    private final String key;
    private String value;
    private String name;

    private boolean isBoolean;

    public PropertyMapping(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }

    public String getValue() {
      return value;
    }

    public boolean isFlagged() {
      return flagged;
    }

    public boolean isBoolean() {
      return isBoolean;
    }

    public static PropertyMapping withKey(String key) {
      return new PropertyMapping(key);
    }

    public String getName() {
      return name;
    }

    public PropertyMapping withName(String name) {
      this.name = name;
      return this;
    }

    public PropertyMapping withValue(boolean value) {
      var fresh = withValue(Boolean.toString(value));
      fresh.isBoolean = true;
      return fresh;
    }

    // note: returns fresh mapping
    public PropertyMapping withValue(String value) {
      var fresh = withKey(key);
      fresh.value = value;
      fresh.name = getName();
      return fresh;
    }

    public String asString() {
      return getKey() + "=" + getValue();
    }

    public static PropertyMapping fromString(String ref) {
      if (ref == null) {
        return null;
      }
      var split = ref.split("=");
      if (split.length != 2) {
        return null;
      }
      boolean value = Boolean.parseBoolean(split[1]);
      return withKey(split[0]).withValue(value);
    }

    public PropertyMapping flag() {
      flagged = true;
      return this;
    }

    public PropertyMapping unflag() {
      flagged = false;
      return this;
    }

    @Override
    public String toString() {
      return "PropertyMapping: " + getKey() + "=" + getValue();
    }
  }

  private final List<PropertyMapping> mappings = new ArrayList<>();

  private final transient IUIContext ui;

  public static PropertySet forLocatorInContext(ILocator locator, IUIContext ui) {
    return new PropertySet(locator, ui);
  }

  private PropertySet(ILocator locator, IUIContext ui) {
    this.ui = ui;
    addContributedProperties(locator, ui);
    addBuiltinProperties(locator);
  }

  private void addContributedProperties(ILocator locator, IUIContext ui) {
    var pp = (IPropertyProvider) adapt(locator, IPropertyProvider.class);
    if (pp == null) {
      return;
    }
    var props = pp.getProperties(ui);
    Collections.addAll(mappings, props);
  }

  public static Object adapt(Object o, Class<?> cls) {
    if (o == null) {
      return null;
    }
    if (o.getClass() == cls) {
      return o;
    }
    if (cls.isAssignableFrom(o.getClass())) {
      return o;
    }
    if (o instanceof IAdaptable adaptable) {
      return adaptable.getAdapter(cls);
    }
    return null;
  }

  private void addBuiltinProperties(ILocator locator) {
    // TODO: rank these...
    if (locator instanceof IsVisible) {
      mappings.add(PropertyMapping.VISIBLE.withValue(isVisible(locator)));
    }
    if (locator instanceof IsEnabled) {
      mappings.add(PropertyMapping.ENABLED.withValue(isEnabled(locator)));
    }
    if (locator instanceof IsSelected) {
      mappings.add(PropertyMapping.SELECTED.withValue(isSelected(locator)));
    }
    if (locator instanceof IsChecked && isCheckStyleBitSet(locator)) {
      mappings.add(PropertyMapping.CHECKED.withValue(isChecked(locator)));
    }
    if (locator instanceof HasFocus) {
      mappings.add(PropertyMapping.FOCUS.withValue(hasFocus(locator)));
    }
  }

  private boolean isVisible(ILocator locator) {
    return new IsVisibleCondition((IsVisible) locator).testUI(getUI());
  }

  private boolean isSelected(ILocator locator) {
    return new IsSelectedCondition((IsSelected) locator).testUI(getUI());
  }

  private boolean isChecked(ILocator locator) {
    return new IsCheckedCondition((IsChecked) locator).testUI(getUI());
  }

  private boolean isCheckStyleBitSet(ILocator locator) {
    return new IsCheckedCondition((IsChecked) locator).testCheckStyleBit(getUI());
  }

  private boolean isEnabled(ILocator locator) {
    return new IsEnabledCondition((IsEnabled) locator).testUI(getUI());
  }

  private boolean hasFocus(ILocator locator) {
    return new HasFocusCondition((HasFocus) locator).testUI(getUI());
  }

  private IUIContext getUI() {
    return ui;
  }

  public PropertySet withMapping(PropertyMapping mapping) {
    mappings.add(mapping);
    return this;
  }

  public int size() {
    return mappings.size();
  }

  public PropertyMapping[] toArray() {
    return mappings.toArray(new PropertyMapping[0]);
  }

  @Override
  public String toString() {
    var builder = new StringBuilder();
    builder.append("PropertySet[");

    var mappings = toArray();
    for (int i = 0; i < mappings.length; i++) {
      builder.append(mappings[i]);
      if (i + 1 < mappings.length) {
        builder.append(", ");
      }
    }
    builder.append("]");
    return builder.toString();
  }

  public PropertySet flagged() {
    var set = new PropertySet(null, ui);
    for (PropertyMapping mapping : mappings) {
      if (mapping.isFlagged()) {
        set.withMapping(mapping);
      }
    }
    return set;
  }

  public boolean isEmpty() {
    return mappings.isEmpty();
  }

  // find the corresponding mapping in this set and flag it
  public void flag(PropertyMapping toFlag) {
    for (PropertyMapping prop : mappings) {
      if (prop.getKey().equals(toFlag.getKey())) {
        prop.flag();
        return;
      }
    }
    LogHandler.log("call to flag: " + toFlag + " ignored --- not contained in set" + mappings);
  }

  public void unflag(PropertyMapping toUnFlag) {
    for (PropertyMapping prop : mappings) {
      if (prop.getKey().equals(toUnFlag.getKey())) {
        prop.unflag();
        return;
      }
    }
    LogHandler.log("call to flag: " + toUnFlag + " ignored --- not contained in set" + mappings);
  }

  public static PropertySet empty() {
    return new PropertySet(null, null);
  }
}
