package com.neocoretechs.relatrix.parallel;

import java.util.Map;

import com.neocoretechs.relatrix.key.IndexResolver;

public record ParallelExecutionContext(IndexResolver resolver, Map<String, Object> ctx) {}

