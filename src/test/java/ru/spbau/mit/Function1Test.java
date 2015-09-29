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
        assertTrue(10 == addTen.apply(0));
        assertTrue(20 == addTen.apply(10));

        assertTrue(10 == multTwo.apply(5));
        assertTrue(20 == multTwo.apply(10));

        assertTrue("meow" == animalSay.apply(new Cat()));
        assertTrue("bark" == animalSay.apply(new Dog()));
    }

    @Test
    public void testCompose() {
        assertTrue(30 == addTen.compose(multTwo).apply(5));
        assertTrue(20 == multTwo.compose(addTen).apply(5));
        assertTrue(20 == multTwo.compose(multTwo).apply(5));
    }
}