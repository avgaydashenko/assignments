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

    public static <T, R> R foldl(final Function2<? super R, ? super T, ? extends R> f, R startValue, final Iterable<T> a) {
        R currRes = startValue;
        for (T element : a) {
            currRes = f.apply(currRes, element);
        }
        return currRes;
    }

    public static <T, R> R foldr(final Function2<? super T, ? super R, ? extends R> f, R startValue, final Iterable<T> a) {
        R currRes = startValue;
        ArrayList<T> tmp = new ArrayList<T>();
        for (T element : a) {
            tmp.add(element);
        }
        for (int i = tmp.size() - 1; i >= 0; i--) {
            currRes = f.apply(tmp.get(i), currRes);
        }
        return currRes;
    }
}
