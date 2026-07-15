package com.neocoretechs.relatrix.client.json.util;

import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONObject;

import org.json.cbor.CborException;

import com.neocoretechs.relatrix.server.HandlerClassLoader;


public class Converter {
	private static HandlerClassLoader classLoader;
	
	public static void setClassLoader(HandlerClassLoader loader) {
		classLoader = loader;
	}
	
	public static Object getMorphicObject(JSONObject jsono) throws IOException {
		Constructor ctor = getMorphicConstructor(jsono);
		return getMorphicObject(ctor, jsono);
	}
	
	public static Constructor getMorphicConstructor(JSONObject jsono) throws IOException {
		// calls extractStructuralTokens, populates fields with JSONObject o
    	String className = RelatrixTypeSynthesizer.generateMorphicClassName((JSONObject)jsono,RelatrixTypeSynthesizer.morphicClassPrefix);
    	byte[] ctype = null;
      	Class<?> c;
      	try {
      		c = Class.forName(className, false, classLoader);
      	} catch(ClassNotFoundException cnf) {
	       	ctype = JsonRecordClassGenerator.buildJsonRecordClassBytes(className);   
    		c = classLoader.defineAClass(className, ctype);
      	}
    	try {
			return c.getConstructor(byte[].class);
		} catch (NoSuchMethodException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Generate objects of same class as getMorphicConstructor
	 * @param ctor
	 * @param jsono
	 * @return
	 * @throws IOException
	 */
	public static Object getMorphicObject(Constructor ctor, JSONObject jsono) throws IOException {
    	try {
			return ctor.newInstance(RelatrixTypeSynthesizer.encodeCborPayload(jsono));
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | CborException e) {
			throw new IOException(e);
		}
	}
	
	public static byte[] getMorphicBytes(Object morphic) throws IOException {
		Field field;
		byte[] b = null;
		String s = null;
		try {
			field = morphic.getClass().getField("cbor");
			return (byte[]) field.get(morphic);
		} catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
			throw new IOException(e);
		}
	}
	
	public static Object getMorphicObject(byte[] payload) throws IOException {
		try {
			JSONObject jsono = getJsonObject(payload);
			return getMorphicObject(jsono);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
	}
	
	public static JSONObject getJsonObject(byte[] morphic) throws IOException {
		String s = null;
		try {
			s = RelatrixTypeSynthesizer.decodeCborPayload(morphic);
			return new JSONObject(s);
		} catch (IllegalArgumentException | CborException e) {
			throw new IOException(e);
		}
	}
	
	public static JSONObject getJsonObject(Object morphic) throws IOException {
		byte[] b = null;
		String s = null;
		try {
			b = getMorphicBytes(morphic);
			s = RelatrixTypeSynthesizer.decodeCborPayload(b);
			return new JSONObject(s);
		} catch (IllegalArgumentException | CborException e) {
			throw new IOException(e);
		}
	}
}
