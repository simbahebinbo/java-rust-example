package com.github.lansheng228.javarust;

import java.io.Closeable;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Structure;

/**
 * A struct that we return from Rust to Java.
 *
 * This is the Java representation of the Greeting struct in Rust.
 */
public class Greeting extends Structure implements Closeable {

    public static class ByReference extends Greeting implements Structure.ByReference {
    }

    public static class ByValue extends Greeting implements Structure.ByValue {
    }

    public String text;

    public String getText() {
        return text;
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("text");
    }

    @Override
    public void close() {
        // Turn off "auto-synch". If it is on, JNA will automatically read all fields
        // from the struct's memory and update them on the Java object. This synchronization
        // occurs after every native method call. If it occurs after we drop the struct, JNA
        // will try to read from the freed memory and cause a segmentation fault.
        setAutoSynch(false);
        // Send the struct back to rust for the memory to be freed
        Greetings.INSTANCE.dropGreeting(this);
    }
}
