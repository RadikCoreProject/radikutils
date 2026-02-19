package org.radikutils.plets;

public class Duplet<T, P> implements Nplet<T, P> {

    private final T t;
    private final P p;

    public Duplet(T type, P parametrize) {
        this.t = type;
        this.p = parametrize;
    }

    public T getType() {
        return this.t;
    }

    public P getParametrize() {
        return this.p;
    }
}
