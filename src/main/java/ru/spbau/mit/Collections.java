package ru.spbau.mit;

import java.util.ArrayList;

public class Collections {
    public static <T, R> Iterable<R> map(final Function1<? super T, ? extends R> f, final Iterable<T> a) {
        ArrayList<R> res = new ArrayList<R>();
        for (T element : a) {
            res.add(f.apply(element));
        }
        return res;
    }

    public static <T> Iterable<T> filter(final Predicate<? super T> p, final Iterable<T> a) {
        ArrayList<T> res = new ArrayList<T>();
        for (T element : a) {
            if (p.apply(element)) {
                res.add(element);
            }
        }
        return res;
    }

    public static <T> Iterable<T> takeWhile(final Predicate<? super T> p, final Iterable<T> a) {
        ArrayList<T> res = new ArrayList<T>();
        for (T element : a) {
            if (p.apply(element)) {
                res.add(element);
            }
            else {
                break;
            }
        }
        return res;
    }

    public static <T> Iterable<T> takeUnless(final Predicate<? super T> p, final Iterable<T> a) {
        ArrayList<T> res = new ArrayList<T>();
        for (T element : a) {
            if (!p.apply(element)) {
                res.add(element);
            }
            else {
                break;
            }
        }
        return res;
    }

    public static <T> T foldl(final Function2<T, T, T> f, T startValue, final Iterable<T> a) {
        T currRes = startValue;
        for (T element : a) {
            currRes = f.apply(currRes, element);
        }
        return currRes;
    }

    public static <T> T foldr(final Function2<T, T, T> f, T startValue, final Iterable<T> a) {
        T currRes = startValue;
        ArrayList<T> tmp = new ArrayList<T>();
        for (T element : a) {
            tmp.add(element);
        }
        for (int i = tmp.size() - 1; i >= 0; i--) {
            currRes = f.apply(currRes, tmp.get(i));
        }
        return currRes;
    }
}
