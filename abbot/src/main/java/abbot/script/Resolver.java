package abbot.script;

import abbot.finder.Hierarchy;
import java.awt.Component;
import java.io.File;
import java.util.Collection;

// TODO: extract reference management
// o hierarchy
// o refs collection
// o name generation

/**
 * Interface to provide a general context in which tests are run. Includes ComponentReferences, current gui hierarchy,
 * properties, and a working directory.
 */
public interface Resolver {
  /**
   * Return the existing reference for the given component, or null if none exists.
   * @param comp component
   * @return the existing reference for the given component, or null if none exists.
   */
  ComponentReference getComponentReference(Component comp);

  /**
   * Add a new component to the existing collection.
   * @param comp component
   * @return reference of the added component
   */
  ComponentReference addComponent(Component comp);

  /**
   * Add a new component reference to the existing collection.
   * @param ref component reference
   */
  void addComponentReference(ComponentReference ref);

  /**
   * Returns a collection of all the existing references.
   * @return a collection of all the existing references
   */
  Collection getComponentReferences();

  /**
   * Return the ComponentReference matching the given id, or null if none exists.
   * @param refid component reference id
   * @return component reference
   */
  ComponentReference getComponentReference(String refid);

  /**
   * Get Hierarchy used by this Resolver.
   * @return hierarchy
   */
  Hierarchy getHierarchy();

  /**
   * Return the class loader for use in this context.
   * @return class loader
   */
  ClassLoader getContextClassLoader();

  /**
   * Provide a working directory context for relative pathnames.
   * @return file
   */
  File getDirectory();

  /**
   * Provide temporary storage of String values.
   * @param name property name
   * @param value property value
   */
  void setProperty(String name, Object value);

  /**
   * Provide retrieval of values from temporary storage.
   * @param name property name
   * @return property value
   */
  Object getProperty(String name);

  /**
   * Provide a human-readable string that describes the given step's context.
   * @param step step
   * @return step value
   */
  // TODO: this belongs in UIContext
  String getContext(Step step);
}
