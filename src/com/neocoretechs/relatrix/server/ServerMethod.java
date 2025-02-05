package com.neocoretechs.relatrix.server;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * Designates a static method in a class as eligible for reflection and inclusion in {@link ServerInvokeMethod} processing
 * for client side calls.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2025
 *
 */
public @interface ServerMethod {

}
