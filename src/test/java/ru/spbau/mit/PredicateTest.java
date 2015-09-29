package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.*;

public class PredicateTest {

    private static Predicate<Integer> dividedThree =
        new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer num) {
                return (num % 3 == 0);
            }
        };

    private static Predicate<Integer> dividedFive =
        new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer num) {
                return (num % 5 == 0);
            }
        };

    @Test
    public void testAnd() {
        assertTrue(true == dividedFive.and(dividedThree).apply(15));
        assertTrue(true == dividedFive.and(dividedThree).and(Predicate.ALWAYS_TRUE).apply(30));
    }

    @Test
    public void testOr() {
        assertTrue(true == dividedFive.or(dividedThree).apply(9));
        assertTrue(true == dividedFive.or(dividedThree).or(Predicate.ALWAYS_FALSE).apply(5));
    }

    @Test
    public void testNot() {
        assertTrue(true == dividedFive.not().apply(9));
        assertTrue(true == Predicate.ALWAYS_FALSE.not().apply(9));
    }
}