package com.github.lansheng228.javarust;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * An entry point that says hello from Rust code
 */
public class Main {

    private static String name;

    public static void main(String[] args) {
        List<String> arguments = asList(args);
        if (arguments.isEmpty()) {
            name = "World";
        } else {
            name = arguments.get(0);
        }
        Greetings.INSTANCE.printGreeting(name);
    }

}
