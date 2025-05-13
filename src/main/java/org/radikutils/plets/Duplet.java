package org.radikutils.plets;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Duplet<T, P> implements Nplet<T, P> {

    private T t = null;
    private P p = null;

    public Duplet(@Nullable T type, @Nullable P parametrize) {
        this.t = type;
        this.p = parametrize;
    }

    public @Nullable T getType() {
        return this.t;
    }

    public @Nullable P getParametrize() {
        return this.p;
    }

    public boolean isEmpty() {
        return this.t == null && this.p == null;
    }

    public void setDuplet(T type, P parametrize) {
        this.t = type;
        this.p = parametrize;
    }
}
