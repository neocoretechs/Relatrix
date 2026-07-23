package com.neocoretechs.relatrix.parallel;

import java.util.Map;

import com.neocoretechs.relatrix.key.IndexResolver;
/**
 * Record that holds the {@link IndexResolver} and a map for any auxiliary data that the user may wish
 * to inject in a parallel execution context, which is how we bind the IndexResolver to arbitrary threads
 * so that we can take a DBKey and resolve it to an actual object instance when necessary.
 * The resolver may function as a server side mechanism or use a client to access a database server.
 * It is used in conjunction with ScopedValue to provide access to resolver from any thread.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public record ParallelExecutionContext(IndexResolver resolver, Map<String, Object> ctx) {}

