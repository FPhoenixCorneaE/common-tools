package com.fphoenixcorneae.common.ext

inline fun <reified A, B> runIfNotNull(a: A?, b: B?, block: (A, B) -> Unit) {
    if (a != null && b != null) {
        block(a, b)
    }
}

inline fun <reified A, B, C> runIfNotNull(a: A?, b: B?, c: C?, block: (A, B, C) -> Unit) {
    if (a != null && b != null && c != null) {
        block(a, b, c)
    }
}

@SuppressWarnings("ComplexCondition")
inline fun <reified A, B, C, D> runIfNotNull(
    a: A?,
    b: B?,
    c: C?,
    d: D?,
    block: (A, B, C, D) -> Unit
) {
    if (a != null && b != null && c != null && d != null) {
        block(a, b, c, d)
    }
}