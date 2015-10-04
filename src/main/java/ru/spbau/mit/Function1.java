package ru.spbau.mit;

public abstract class Function1<T, R> {

    public abstract R apply(T arg);

    public <R2> Function1<T, R2> compose(final Function1<? super R, ? extends R2> outer) {
        final Function1<T, R> inner = this;

        return new Function1<T, R2>() {
            @Override
            public R2 apply(T newArg) {
                return outer.apply(inner.apply(newArg));
            }
        };
    }
}
