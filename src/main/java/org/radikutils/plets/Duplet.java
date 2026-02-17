package org.radikutils.plets;

import org.jetbrains.annotations.NotNull;

public class Duplet<T, P> implements Nplet<T, P> {

    private final T t;
    private final P p;

    public Duplet(@NotNull T type, @NotNull P parametrize) {
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
