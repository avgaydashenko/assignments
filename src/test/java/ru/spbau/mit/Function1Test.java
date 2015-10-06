package ru.spbau.mit;

import org.junit.Test;
import static org.junit.Assert.*;

public class Function1Test {

    private static Function1<Integer, Integer> addTen =
        new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer num) {
                return num + 10;
            }
        };

    private static Function1<Integer, Integer> multTwo =
        new Function1<Integer, Integer>() {
            @Override
            public Integer apply(Integer num) {
                return num * 2;
            }
        };

    abstract class Animal {
        public abstract String makeSound();
    }

    class Cat extends Animal {
        public String makeSound() {
            return "meow";
        }
    }

    class Dog extends Animal {
        public String makeSound() {
            return "bark";
        }
    }

    private static Function1<Animal, String> animalSay =
        new Function1<Animal, String>() {
            @Override
            public String apply(Animal arg) {
                return arg.makeSound();
            }
        };

    @Test
    public void testApply() {
        assertEquals(10, (long)addTen.apply(0));
        assertEquals(20, (long)addTen.apply(10));

        assertEquals(10, (long)multTwo.apply(5));
        assertEquals(20, (long)multTwo.apply(10));

        assertEquals("meow", animalSay.apply(new Cat()));
        assertEquals("bark", animalSay.apply(new Dog()));
    }

    @Test
    public void testCompose() {
        assertEquals(30, (long)addTen.compose(multTwo).apply(5));
        assertEquals(20, (long)multTwo.compose(addTen).apply(5));
        assertEquals(20, (long)multTwo.compose(multTwo).apply(5));
    }
}