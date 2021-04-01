package com.github.lansheng228.javarust.test;

import java.util.Collection;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;


public class CustomMatchers extends Matchers {

    public static Matcher<String> matchesRegex(final String regex) {
        return new TypeSafeMatcher<String>() {

            @Override
            protected boolean matchesSafely(String string) {
                return string.matches(regex);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("String matching regex ").appendValue(regex);
            }
        };
    }

    public static Matcher<Collection<?>> hasSize(final int expectedSize) {
        return new TypeSafeMatcher<Collection<?>>() {

            @Override
            protected boolean matchesSafely(Collection<?> item) {
                return item.size() == expectedSize;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("Collection of size ").appendValue(expectedSize);
            }
        };
    }
}
