package org.radikutils.plets;

import org.jetbrains.annotations.Nullable;

public class Triplet<T, P, C> implements Nplet<T, P> {

    private T t;
    private P p;
    private C c;

    public Triplet(@Nullable T type, @Nullable P parametrize, C count) {
        this.t = type;
        this.p = parametrize;
        this.c = count;
    }

    public @Nullable T getType() {
        return this.t;
    }

    public @Nullable P getParametrize() {
        return this.p;
    }

    public @Nullable C getCount() {
        return this.c;
    }

    public void setTriplet(T type, P parametrize, C count) {
        this.t = type;
        this.p = parametrize;
        this.c = count;
    }
}
