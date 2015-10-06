package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.Math;

public class Function2Test {

    private static Function2<Integer, Integer, Integer> add =
        new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer num1, Integer num2) {
                return num1 + num2;
            }
        };

    private static Function2<Integer, Integer, Integer> mod =
        new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer apply(Integer num1, Integer num2) {
                return num1 % num2;
            }
        };

    private static Function1<Integer, Integer> multTwo =
        new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer num) {
                return num * 2;
            }
        };

    @Test
    public void testApply() {
        assertEquals(10, (long)add.apply(0, 10));
        assertEquals(20, (long)add.apply(10, 10));
   }

    @Test
    public void testCompose() {
        assertEquals(20, (long)add.compose(multTwo).apply(0, 10));
        assertEquals(40, (long)add.compose(multTwo).apply(10, 10));
    }

    @Test
    public void testBind1() {
        assertEquals(0, (long)mod.bind1(10).apply(5));
        assertEquals(5, (long)mod.bind1(5).apply(10));
    }

    @Test
    public void testBind2() {
        assertEquals(5, (long)mod.bind2(10).apply(5));
        assertEquals(0, (long)mod.bind2(5).apply(10));
    }

    @Test
    public void testCurry() {
        assertEquals(5, (long)mod.curry().apply(5).apply(10));
        assertEquals(0, (long)mod.curry().apply(10).apply(5));
    }
}