package org.radikutils.plets;

public class Triplet<T, P, C> implements Nplet<T, P> {

    private T t;
    private P p;
    private C c;

    public Triplet(T type, P parametrize, C count) {
        this.t = type;
        this.p = parametrize;
        this.c = count;
    }

    public T getType() {
        return this.t;
    }

    public P getParametrize() {
        return this.p;
    }

    public C getCount() {
        return this.c;
    }

    public void setTriplet(T type, P parametrize, C count) {
        this.t = type;
        this.p = parametrize;
        this.c = count;
    }
}
