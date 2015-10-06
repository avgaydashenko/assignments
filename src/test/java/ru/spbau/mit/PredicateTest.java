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

    private static Predicate<Integer> badPred =
        new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer num) {
                fail();
                return (num == 0);
            }
        };

    @Test
    public void testAnd() {
        assertFalse(dividedFive.and(dividedThree).apply(3));
        assertTrue(dividedFive.and(dividedThree).and(Predicate.ALWAYS_TRUE).apply(30));

        assertFalse(dividedFive.and(badPred).apply(3));
    }

    @Test
    public void testOr() {
        assertTrue(dividedFive.or(dividedThree).apply(9));
        assertTrue(dividedFive.or(dividedThree).or(Predicate.ALWAYS_FALSE).apply(5));

        assertTrue(dividedFive.or(badPred).apply(10));
    }

    @Test
    public void testNot() {
        assertTrue(dividedFive.not().apply(9));
        assertTrue(Predicate.ALWAYS_FALSE.not().apply(9));
    }
}