package com.github.lansheng228.javarust;

import java.io.Closeable;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * A struct that contains an array of structs. This is the Java representation
 * of the GreetingSet struct in Rust (see the Rust code).
 */
public class GreetingSet extends Structure implements Closeable {

    public static class ByReference extends GreetingSet implements Structure.ByReference {
    }

    public static class ByValue extends GreetingSet implements Structure.ByValue {
    }

    /**
     * An array of Greetings returned from Rust.
     *
     * Actually, this is a pointer to a bunch of struct instances that are next
     * to each other in memory. We cast it to an array in {@link #getGreetings()}.
     *
     * NB: We need to explicitly specify that the field is a pointer (i.e. we need
     * to use ByReference) because, by default, JNA assumes that struct fields 
     * are not pointers (i.e. if you just say "Greeting", JNA assumes
     * "Greeting.ByValue" here).
     */
    public Greeting.ByReference greetings;
    /**
     * The size of the array from Rust.
     *
     * Because we don't have any way to tell how long the array is, we've got to
     * return the length back separately. The reason we don't have to do this with
     * strings passed back from Rust (which are actually arrays of characters) 
     * is that native strings have a special null character at the end that JNA 
     * uses to tell how long each string is.
     */
    public int numberOfGreetings;

    /**
     * Get the greetings this struct's pointer is pointing to.
     *
     * Here we cast the native array into a Java list to make it more convenient
     * to work with in Java.
     */
    public List<Greeting> getGreetings() {
        Greeting[] array = (Greeting[]) greetings.toArray(numberOfGreetings);
        return Arrays.asList(array);
    }

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
        return Arrays.asList("greetings", "numberOfGreetings");
    }

    /**
     * Send the GreetingSet back to Rust to be dropped.
     *
     * We do this because JNA doesn't free the memory when the object is garbage collected.
     */
    @Override
    public void close() {
        // Turn off "auto-synch". If it is on, JNA will automatically read all fields
        // from the struct's memory and update them on the Java object. This synchronization
        // occurs after every native method call. If it occurs after we drop the struct, JNA
        // will try to read from the freed memory and cause a segmentation fault.
        setAutoSynch(false);
        // Send the struct back to rust for the memory to be freed
        Greetings.INSTANCE.dropGreetingSet(this);
    }
}
