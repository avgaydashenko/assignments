package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CollectionsTest {

    private static final ArrayList<Integer> a =
        new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3));

    private static Predicate<Integer> dividedThree =
        new Predicate<Integer>() {
            @Override
            public Boolean apply(Integer num) {
                return (num % 3 == 0);
            }
        };

    private static Function1<Integer, Integer> addTen =
        new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer num) {
                return num + 10;
            }
        };

    private static Function2<Integer, Integer, Integer> add =
        new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer num1, Integer num2) {
                return num1 + num2;
            }
        };

    private static final Integer TEST_VALUE = 4;

    private static final Collections MY_COLLECTIONS = new Collections();

    @Test
    public void testMap() {
        ArrayList<Integer> test = (ArrayList<Integer>)MY_COLLECTIONS.map(addTen, a);
        for (int i = 0; i < test.size(); i++) {
            assertTrue(test.get(i) == a.get(i) + 10);
        }
    }

    @Test
    public void testFilter() {
        ArrayList<Integer> test = (ArrayList<Integer>)MY_COLLECTIONS.filter(dividedThree, a);
        assertTrue(test.get(0) == 0);
        assertTrue(test.get(1) == 3);
        assertTrue(test.size() == 2);
    }

    @Test
    public void testTakeWhile() {
        ArrayList<Integer> test = (ArrayList<Integer>)MY_COLLECTIONS.takeWhile(dividedThree, a);
        assertTrue(test.get(0) == 0);
        assertTrue(test.size() == 1);
    }

    @Test
    public void testTakeUnless() {
        ArrayList<Integer> test = (ArrayList<Integer>)MY_COLLECTIONS.takeUnless(dividedThree, a);
        assertTrue(test.size() == 0);
    }

    @Test
    public void testFoldl() {
        Integer test = MY_COLLECTIONS.foldl(add, TEST_VALUE, a);
        assertTrue(test == 10);
    }

    @Test
    public void testFoldr() {
        Integer test = MY_COLLECTIONS.foldr(add, TEST_VALUE, a);
        assertTrue(test == 10);
    }
}