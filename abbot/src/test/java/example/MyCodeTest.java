package example;

import java.io.File;

import junit.extensions.abbot.ScriptFixture;
import junit.extensions.abbot.ScriptTestSuite;
import junit.extensions.abbot.TestHelper;
import junit.framework.Test;

/**
 * Simple example of a ScriptTestSuite.  Selects all scripts of the form MyCode-[0-9]*.xml.
 */
public class MyCodeTest extends ScriptFixture {

    /**
     * Name is the name of a script filename.
     */
    public MyCodeTest(String name) {
        super(name);
    }

    /**
     * Return the set of scripts we want to run.
     */
    public static Test suite() {
        return new ScriptTestSuite(MyCodeTest.class, "src/test/resources/example") {
            /** Determine whether the given script should be included. */
            public boolean accept(File file) {
                String name = file.getName();
                return name.startsWith("MyCode-")
                        && Character.isDigit(name.charAt(7))
                        && name.endsWith(".xml");
            }
        };
    }
}
