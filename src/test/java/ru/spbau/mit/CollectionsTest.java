package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;

public class CollectionsTest {

    private static final ArrayList<Integer> a =
        new ArrayList<Integer>(Arrays.asList(2, 2, 2, 3));

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

    private static Function2<Integer, Integer, Integer> minus =
        new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer num1, Integer num2) {
                return num1 - num2;
            }
        };

    private static final Integer TEST_VALUE = 0;

    @Test
    public void testMap() {
        Iterable<Integer> test = Collections.map(addTen, a);
        Integer i = 0;
        for (Integer element : test) {
            assertEquals((long)element, a.get(i) + 10);
            i++;
        }
    }

    @Test
    public void testFilter() {
        ArrayList<Integer> test = (ArrayList<Integer>)Collections.filter(dividedThree, a);
        assertEquals((long)test.get(0), 3);
        assertEquals(test.size(), 1);
    }

    @Test
    public void testTakeWhile() {
        ArrayList<Integer> test = (ArrayList<Integer>)Collections.takeWhile(dividedThree, a);
        assertEquals(test.size(), 0);
    }

    @Test
    public void testTakeUnless() {
        ArrayList<Integer> test = (ArrayList<Integer>)Collections.takeUnless(dividedThree, a);
        assertEquals(test.size(), 3);
    }

    @Test
    public void testFoldl() {
        Integer test = Collections.foldl(minus, TEST_VALUE, a);
        assertEquals((long)test, -9);
    }

    @Test
    public void testFoldr() {
        Integer test = Collections.foldr(minus, TEST_VALUE, a);
        assertEquals((long)test, -1);
    }
}