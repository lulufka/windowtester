package junit.extensions.abbot;

import java.util.ArrayList;

import abbot.Log;
import junit.extensions.RepeatedTest;
import junit.framework.Test;

/**
 * Convenience functions to wrap a given test case such that individual methods may be run with heavy repetition, and
 * default suites run with light repetition.
 */

public class RepeatHelper extends TestHelper {

    private static int repeatCount = 1;

    // no instantiations
    protected RepeatHelper() {
    }

    protected static String[] parseArgs(final String[] args) {
        final ArrayList list = new ArrayList();
        for (int i = 0; i < args.length; i++) {
            try {
                repeatCount = Integer.parseInt(args[i]);
            } catch (final NumberFormatException e) {
                list.add(args[i]);
            }
        }
        return (String[]) list.toArray(new String[list.size()]);
    }

    public static void runTests(
            String[] args,
            final Class testClass) {
        args = Log.init(args);
        args = parseArgs(args);
        args = TestHelper.parseArgs(args);
        try {
            Test test = collectTests(args, testClass);
            if (repeatCount > 1) {
                test = new RepeatedTest(test, repeatCount);
            }
            runTest(test);
        } catch (final Exception e) {
            System.err.println(e.getMessage());
            System.exit(-2);
        }
    }
}
