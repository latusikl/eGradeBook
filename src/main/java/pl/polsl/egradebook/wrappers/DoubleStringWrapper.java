package pl.polsl.egradebook.wrappers;


/**
 * Simple class for wrapping two strings into object.
 * Required for usage in Thymleaf form.
 */
public class DoubleStringWrapper {
    private String parameter1;
    private String parameter2;

    public String getParameter1() {
        return parameter1;
    }

    public void setParameter1(final String parameter1) {
        this.parameter1 = parameter1;
    }

    public String getParameter2() {
        return parameter2;
    }

    public void setParameter2(final String parameter2) {
        this.parameter2 = parameter2;
    }
}
