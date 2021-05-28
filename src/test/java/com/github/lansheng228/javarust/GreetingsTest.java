package com.github.lansheng228.javarust;

import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

public class GreetingsTest {
    private Greetings library;

    @BeforeEach
    public void setUp() {
        library = Greetings.INSTANCE;
    }

    @Test
    public void setValue() {
        Greeting greeting = new Greeting();
        greeting.setText("world");
        System.out.println(greeting.getText());
    }

    @Test
    public void shouldAcceptStringParameterFromJavaToRust() {
        //Printed in the console
        library.printGreeting("World");
    }

    @Test
    public void shouldAcceptStringFromJavaToRustAndReturnAnotherOne() {
        String greeting = library.renderGreeting("World");
        assertThat(greeting, is("Hello, World!"));
    }

    @Test
    public void shouldAcceptAStructFromJavaToRust() {
        Person john = new Person();
        john.firstName = "John";
        john.lastName = "Smith";
        String greeting = library.greet(john);
        assertThat(greeting, is("Hello, John Smith!"));
    }

    @Test
    public void shouldGetAStructFromRustByValue() {
        // Using try-with-resources so that memory gets cleaned up. See Greeting.close()
        try (Greeting greeting = library.getGreetingByValue()) {
            assertThat(greeting.getText(), is("Hello from Rust!"));
        }
    }

    @Test
    public void shouldGetAStructFromRustByReference() {
        try (Greeting greeting = library.getGreetingByReference()) {
            assertThat(greeting.getText(), is("Hello from Rust!"));
        }
    }

    @Test
    public void shouldGetAStringFromRustInACallback() {
        final List<String> greetings = new LinkedList<>();
        library.callMeBack(greetings::add);
        assertThat(greetings, contains("Hello there!"));
    }

    @Test
    public void shouldGetListOfStringsFromRustInACallback() {
        final List<Greeting> greetings = new LinkedList<>();
        library.sendGreetings(greetingSet -> greetings.addAll(greetingSet.getGreetings()));

        List<String> greetingStrings = new LinkedList<>();
        for (Greeting greeting : greetings) {
            greetingStrings.add(greeting.getText());
        }

        assertThat(greetingStrings, contains("Hello!", "Hello again!"));
    }

    @Test
    public void shouldGetAStructFromRustContainingAnArrayOfStructs() {
        try (GreetingSet result = library.renderGreetings()) {
            List<String> greetings = new LinkedList<>();
            for (Greeting greeting : result.getGreetings()) {
                greetings.add(greeting.getText());
            }

            assertThat(greetings, contains("Hello!", "Hello again!"));
        }
    }
}
