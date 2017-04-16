package com.hifx.mapper;

/**
 * MultiFunction defines the function type that takes two parameters and returns a type
 */
@FunctionalInterface
public interface MultiFunction<A, B, R> {
    R apply(A a, B b);
}
