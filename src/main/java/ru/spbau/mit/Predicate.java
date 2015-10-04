package ru.spbau.mit;

public abstract class Predicate<T> extends Function1<T, Boolean> {

    public static final Predicate<Object> ALWAYS_TRUE =
        new Predicate<Object>() {
            @Override
            public Boolean apply(Object arg) {
                return true;
            }
        };

    public static final Predicate<Object> ALWAYS_FALSE =
        new Predicate<Object>() {
            @Override
            public Boolean apply(Object arg) {
                return false;
            }
        };

    public Predicate<T> and(final Predicate<? super T> right) {
        final Predicate<T> left = this;

        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return (left.apply(arg) && right.apply(arg));
            }
        };
    }

    public Predicate<T> or(final Predicate<? super T> right) {
        final Predicate<T> left = this;

        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return (left.apply(arg) || right.apply(arg));
            }
        };
    }

    public Predicate<T> not() {
        final Predicate<T> currPred = this;

        return new Predicate<T>() {
            @Override
            public Boolean apply(T arg) {
                return !(currPred.apply(arg));
            }
        };
    }
}