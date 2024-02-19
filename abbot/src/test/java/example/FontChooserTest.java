package example;

import java.io.File;

import junit.extensions.abbot.ScriptFixture;
import junit.extensions.abbot.ScriptTestSuite;
import junit.extensions.abbot.TestHelper;
import junit.framework.Test;

/**
 * Collects scripts which test the FontChooser GUI component.
 */

public class FontChooserTest extends ScriptFixture {

    public FontChooserTest(final String filename) {
        super(filename);
    }

    /**
     * Provide a default test suite for this test case.
     */
    public static Test suite() {
        return new ScriptTestSuite(FontChooserTest.class,
                "src/test/resources/example") {
            @Override
            public boolean accept(final File file) {
                return super.accept(file)
                        && !file.getName().equals("fixture.xml");
            }
        };
    }
}
