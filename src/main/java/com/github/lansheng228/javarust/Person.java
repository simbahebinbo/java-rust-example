package com.github.lansheng228.javarust;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * A struct that we pass from Java into Rust.
 *
 * This is the Java representation of the Person struct in Rust.
 */
public class Person extends Structure {

    public static class ByReference extends Person implements Structure.ByReference {
    }

    public static class ByValue extends Person implements Structure.ByValue {
    }

    public String firstName;
    public String lastName;

    /**
     * Specify the order of the struct's fields.
     *
     * The order here needs to match the order of the fields in the Rust code.
     * The astute will notice that the field names only match the field names in the
     * Java class, but not the equivalent Rust struct (the Rust one's are in 
     * snake_case, but could equally have had completely different names).
     * This is because the fields are mapped from the Rust representation to the 
     * Java one by their order (i.e. their relative location in memory), not by their names.
     */
    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("firstName", "lastName");
    }

}
