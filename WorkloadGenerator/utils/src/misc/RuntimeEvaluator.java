package misc;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Aug 25, 2010
 * Time: 11:56:44 AM
 * To change this template use File | Settings | File Templates.
 */
public class RuntimeEvaluator {
    private java.util.Date before;
    private java.util.Date after;

    public void markBefore() {
        before = new Date();
    }

    public void markAfter() {
        after = new Date();
    }

    public long getTimePassedInMillis() {
        return after.getTime() - before.getTime();
    }

    public void printResult() {
        java.util.Date result = new Date(after.getTime() - before.getTime());
        System.out.println("Running time: " + result.getTime());
    }
}
