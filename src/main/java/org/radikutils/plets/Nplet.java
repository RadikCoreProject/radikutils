package org.radikutils.plets;

import org.jetbrains.annotations.Nullable;

public interface Nplet<T, P> {
    T getType();
    P getParametrize();
}
