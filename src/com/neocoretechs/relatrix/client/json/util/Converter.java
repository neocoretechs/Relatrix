package com.neocoretechs.relatrix.client.json.util;

import java.io.IOException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.json.JSONObject;

import org.json.cbor.CborException;

import com.neocoretechs.relatrix.server.HandlerClassLoader;

/**
 * Converter methods to convert JSONObject to morphic object. Various intermediate methods to acquire morphic class name, constructor, Class, etc.
 * Relies on the setting of a HandlerClassLoader with access to class definition bytes either local or remote via ClientInterface.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public class Converter {
	private static HandlerClassLoader classLoader;
	
	public static void setClassLoader(HandlerClassLoader loader) {
		classLoader = loader;
	}
	/**
	 * Generate morphic object from JSONObject
	 * @param jsono the JSONObject
	 * @return the morphic Object
	 * @throws IOException
	 */
	public static Object getMorphicObject(JSONObject jsono) throws IOException {
		Constructor ctor = getMorphicConstructor(jsono);
		return getMorphicObject(ctor, jsono);
	}
	/**
	 * Generate objects of className from JSONObject
	 * @param className class name from getMorphicClassName
	 * @param jsono the JSONObject that generated the class name
	 * @return The new Object
	 * @throws IOException
	 */
	public static Object getMorphicObject(String className, JSONObject jsono) throws IOException {
		Constructor ctor = getMorphicConstructor(className, jsono);
		return getMorphicObject(ctor, jsono);
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
	/**
	 * Generate a morphic class name from a JSONObject using {@link RelatrixTypeSynthesizer}
	 * @param jsono the JSONObject
	 * @return the morphic class name
	 * @throws IOException
	 */
	public static String getMorphicClassname(JSONObject jsono) {
		// calls extractStructuralTokens, populates fields with JSONObject o
    	return RelatrixTypeSynthesizer.generateMorphicClassName((JSONObject)jsono,RelatrixTypeSynthesizer.morphicClassPrefix);
	}
	/**
	 * Get a morphic class from a JSONObject and class name
	 * @param className the class name from getMorphicClassname
	 * @param jsono the JSONObject that generated the class name
	 * @return the morphic Class
	 */
	public static Class<?> getMorphicClass(String className, JSONObject jsono) {
	 	byte[] ctype = null;
      	Class<?> c;
      	try {
      		c = Class.forName(className, false, classLoader);
      	} catch(ClassNotFoundException cnf) {
	       	ctype = JsonRecordClassGenerator.buildJsonRecordClassBytes(className);   
    		c = classLoader.defineAClass(className, ctype);
      	}
      	return c;
	}
	/**
	 * Get a morphic constructor from a JSONObject using {@link RelatrixTypeSynthesizer} and {@link JsonRecordClassGenerator}
	 * @param jsono the JSONObject
	 * @return the morphic constructor
	 * @throws IOException
	 */
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
	 * Get a morphic constructor from a JSONObject and class name using {@link JsonRecordClassGenerator}
	 * @param className the class name from getMorphicClassname
	 * @param jsono the JSONObject
	 * @return the morphic constructor
	 * @throws IOException
	 */
	public static Constructor getMorphicConstructor(String className, JSONObject jsono) throws IOException {
		// calls extractStructuralTokens, populates fields with JSONObject o
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
	 * Get a morphic constructor from a class
	 * @param c the morphic class
	 * @return the morphic constructor
	 * @throws IOException
	 */
	public static Constructor getMorphicConstructor(Class<?> c) throws IOException {
    	try {
			return c.getConstructor(byte[].class);
		} catch (NoSuchMethodException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the bytes that define the morphic class by acquiring the "cbor" field from the target object
	 * @param morphic the morphic Object with cbor field
	 * @return The CBOR payload
	 * @throws IOException
	 */
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
	/**
	 * Get the morphic object from the byte payload using getJsonObject
	 * @param payload
	 * @return
	 * @throws IOException
	 */
	public static Object getMorphicObject(byte[] payload) throws IOException {
		try {
			JSONObject jsono = getJsonObject(payload);
			return getMorphicObject(jsono);
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the JSONObject from the morphic byte payload using {@link RelatrixTypeSynthesizer#decodeCborPayload}
	 * @param morphic the morphic byte payload
	 * @return the JSONObject
	 * @throws IOException
	 */
	public static JSONObject getJsonObject(byte[] morphic) throws IOException {
		String s = null;
		try {
			s = RelatrixTypeSynthesizer.decodeCborPayload(morphic);
			return new JSONObject(s);
		} catch (IllegalArgumentException | CborException e) {
			throw new IOException(e);
		}
	}
	/**
	 * Get the JSONObject from the morphic Object payload using getMorphicBytes and {@link RelatrixTypeSynthesizer#decodeCborPayload}
	 * @param morphic the morphic Object payload
	 * @return the JSONObject
	 * @throws IOException
	 */
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
	/**
	 * Generate a String representation of the JSONObject that is compatible with a morphic class.
	 * @param payload The JSONObject payload
	 * @return A String value of the payload with fields sorted and normalized
	 * @throws IOException
	 */
	public static String normalizeJson(JSONObject payload ) throws IOException {
		try {
			return RelatrixTypeSynthesizer.decodeCborPayload(RelatrixTypeSynthesizer.encodeCborPayload(payload));
		} catch (CborException e) {
			throw new IOException(e);
		}
	}
}
