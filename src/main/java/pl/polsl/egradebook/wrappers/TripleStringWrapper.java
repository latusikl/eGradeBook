package pl.polsl.egradebook.wrappers;

import javax.validation.constraints.NotBlank;


/**
 * Simple class for wrapping two strings into object.
 * Required for usage in Thymleaf form.
 */
public class TripleStringWrapper extends DoubleStringWrapper {

    @NotBlank
    private String parameter3;

    public String getParameter3() {
        return parameter3;
    }

    public void setParameter3(final String parameter3) {
        this.parameter3 = parameter3;
    }
}
