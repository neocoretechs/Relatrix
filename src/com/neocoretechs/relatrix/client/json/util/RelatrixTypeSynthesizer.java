package com.neocoretechs.relatrix.client.json.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.cbor.CborBuilder;
import org.json.cbor.CborDecoder;
import org.json.cbor.CborEncoder;
import org.json.cbor.CborException;
import org.json.cbor.builder.MapBuilder;
import org.json.cbor.model.DataItem;

import com.neocoretechs.relatrix.RelatrixKVJson;
import com.neocoretechs.relatrix.server.HandlerClassLoader;

/**
 * Class to generate hashed class names from JSON field names to create ersatz 
 * database differentiators.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public class RelatrixTypeSynthesizer {
    private static boolean DEBUG = false;
    public static List<Object> elements = new ArrayList<>();
    public static List<String> structuralTokens = new ArrayList<>();
    private static Object mutex = new Object();
	/**
     * Generates a deterministic class name based on the unique structure of an ad-hoc node.
     * @param node The JSON object holding the data fields
     * @param classPrefix The constant string that will prefix the final generated class name
     */
    public static String generateMorphicClassName(JSONObject node, String classPrefix) {
    	synchronized(mutex) {
    		structuralTokens.clear();
    		elements.clear();
    		extractStructuralTokens("", node, structuralTokens, elements);

    		// Create the canonical signature string
    		String canonicalSignature = String.join(";", structuralTokens);

    		// Hash the signature using a standard deterministic fingerprint
    		String structureHash = computeFastHash(canonicalSignature);

    		// Return a clean, safe Java class name identifier
    		return classPrefix + "_" + structureHash;
    	}
    }
    /**
     * CborBuilder creates the payload
     * @param node The JSONObject that has the payload
     * @return The byte array that has the payload
     * @throws CborException If parsing fails
     */
    public static byte[] generateCborPayload(JSONObject node) throws CborException {
    	synchronized(mutex) {
    		structuralTokens.clear();
    		elements.clear();
    		extractStructuralTokens("", node, structuralTokens, elements);
    		CborBuilder cb = new CborBuilder();
    		return generateMorphicPayload(cb);
    	}
    }
    /**
     * Recursively traverse the node structure  populating the tokens and values lists
     * @param path the aggregate path of field names which increases on fieldnames of recursive depth
     * @param node
     * @param tokens
     * @param values
     */
    private static void extractStructuralTokens(String path, JSONObject node, List<String> tokens, List<Object> values) {
    	String[] names = JSONObject.getNames(node);
    	Arrays.sort(names);
    	for(int i = 0; i < names.length; i++) {
    		String fieldName = names[i];
    		Object o = node.get(fieldName);
            	if(DEBUG )
            		System.out.println("Fieldname="+fieldName);
            	values.add(o);
                String currentPath = path.isEmpty() ? fieldName : path + "." + fieldName;
                tokens.add(currentPath);
                // If it's a leaf node, capture the path and type precedence
                if (!(o instanceof JSONObject)) {
                	if(o instanceof JSONArray) {
                	    // For arrays, tokenize based on the array type itself
                		if(DEBUG)
                			System.out.println("array"+path + "[]:" + node.getMapType());
                		JSONArray ja = (JSONArray)o;
                		for(int j = 0; j < ja.length(); j++)
                			extractStructuralTokens(currentPath,ja.getJSONObject(j),tokens,values);
                		//System.out.println(Arrays.toString(ja.toList().toArray()));
                        //tokens.add(path + "[]:" + node.getMapType());
                	} else {
                		if(DEBUG)
                			System.out.println("element="+currentPath + ":" + o);	
                		//tokens.add(currentPath + ":" + eit.getValue());
                	}
                } else {
                    // Recursively walk nested objects or arrays to handle composed morphisms
                    extractStructuralTokens(currentPath, (JSONObject) o, tokens, values);
                }
            }
    }
    /**
     * Create the byte array final payload from the list of tokens and elements using the supplied builder
     * @param structuralTokens List of field names
     * @param elements List of field objects
     * @param cb Cbor builder
     * @return The constructed byte array
     * @throws CborException
     */
    public static byte[] generateMorphicPayload(CborBuilder cb) throws CborException {
    	synchronized(mutex) {
    		buildCBOR(structuralTokens, elements, cb);
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		new CborEncoder(baos).encode(cb.build());
    		return baos.toByteArray();
    	}
    }
    /**
     * Used by generateMorphicPayload 
     * @param tokens
     * @param elements
     * @param cbuild
     */
    private static void buildCBOR(List<String> tokens, List<Object> elements, CborBuilder cbuild) {
   		MapBuilder<CborBuilder> mb = cbuild.addMap();
    	for(int i = 0; i < tokens.size(); i++) {
    		String fieldName = tokens.get(i);
    		Object o = elements.get(i);
            	if(DEBUG )
            		System.out.println("Build Fieldname="+fieldName+" element="+String.valueOf(o));
            // If it's a not a leaf node, capture the path and type precedence
            if(!fieldName.contains("."))
            	mb.put(fieldName,String.valueOf(o));
        }
    	cbuild = mb.end();
    }
    /**
     * Decode the byte array of CBOR data back into a valid JSON string
     * @param encodedBytes
     * @return
     * @throws CborException
     */
    private static String decode(byte[] encodedBytes) throws CborException {
    	ByteArrayInputStream bais = new ByteArrayInputStream(encodedBytes);
    	List<DataItem> dataItems = new CborDecoder(bais).decode();
    	if(DEBUG)
    		System.out.println("DataItems:"+dataItems.size());
    	StringBuilder sb = new StringBuilder();
    	for(DataItem dataItem : dataItems) {
    		if(DEBUG)
    			System.out.println(dataItem);
    		sb.append(dataItem.toString());
    	}
    	return sb.toString();
    }
    /**
     * Compute the SHA-256 hash from the JSON input string
     * @param input
     * @return
     */
    private static String computeFastHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            
            // Take the first 8 bytes (16 hex chars) to keep class names concise yet highly unique
            for (int i = 0; i < 8; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString().toUpperCase();
        } catch (Exception e) {
            throw new RuntimeException("Critical failure generating Relatrix tablespace hash", e);
        }
    }
    /**
     * <pre>
     * {"timestamp":1779155492301,"LeftImage":[{
	 * "count":1,
	 * "detections":[
	 * {
	 * "name":"refrigerator",
	 * "probability":0.41232753,
	 * "bbox":{"xmin":104,"ymin":12,"xmax":223,"ymax":561}
	 * }
	 * ]
	 * }
	 * ],
	 * "RightImage":[{
	 * "count":0,
	 * "detections":[
	 * ]
	 *}
	 *]}
	 * </pre>
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
    	String x = "{\"timestamp\":1779166000301,\"LeftImage\":[{ \"count\":1,\"detections\":[ {\"name\":\"refrigerator\",\"probability\":0.41232753,\"bbox\":{\"xmin\":104,\"ymin\":12,\"xmax\":223,\"ymax\":561} } ] } ], \"RightImage\":[{\"count\":0, \"detections\":[ ] } ]}";
    	JSONObject jo = new JSONObject(x);
    	HandlerClassLoader hcl = new HandlerClassLoader();
    	long tim = System.nanoTime();
    	String className = generateMorphicClassName(jo,"Relatrix_");
       	byte[] b = JsonRecordClassGenerator.buildJsonRecordClassBytes(className);   	
      	Class<?> c;
      	try {
      		c = Class.forName(className, false, hcl);
      	} catch(ClassNotFoundException cnf) {
    		c = hcl.defineAClass(className, b);
      	}
      	CborBuilder cb = new CborBuilder();
    	byte[] encodedBytes = generateMorphicPayload(cb);
    	Constructor ctor = c.getConstructor(byte[].class);
    	Object o = ctor.newInstance(encodedBytes);
        System.out.println("nanos="+(System.nanoTime()-tim));
        System.out.println(c);
    	System.out.println(Arrays.toString(encodedBytes)+" length="+encodedBytes.length);
    	decode(encodedBytes);
    	// try another instance
    	String y = "{\"timestamp\":1779166000302,\"LeftImage\":[{ \"count\":1,\"detections\":[ {\"name\":\"refrigerator\",\"probability\":0.41232753,\"bbox\":{\"xmin\":104,\"ymin\":12,\"xmax\":223,\"ymax\":561} } ] } ], \"RightImage\":[{\"count\":0, \"detections\":[ ] } ]}";
    	JSONObject jo2 = new JSONObject(y);
    	Object o2 = ctor.newInstance(generateCborPayload(jo2));
    	System.out.println("equals="+o.equals(o2));
    	System.out.println("compareTo="+((Comparable)o).compareTo((Comparable)o2));
    	jo2 = new JSONObject(x);
    	o2 = ctor.newInstance(generateCborPayload(jo2));
      	System.out.println("equals="+o.equals(o2));
    	System.out.println("compareTo="+((Comparable)o).compareTo((Comparable)o2));
    	String z = "{\"timestamp\":1779166000300,\"LeftImage\":[{ \"count\":1,\"detections\":[ {\"name\":\"refrigerator\",\"probability\":0.41232753,\"bbox\":{\"xmin\":104,\"ymin\":12,\"xmax\":223,\"ymax\":561} } ] } ], \"RightImage\":[{\"count\":0, \"detections\":[ ] } ]}";
    	jo2 = new JSONObject(z);
    	o2 = ctor.newInstance(generateCborPayload(jo2));
      	System.out.println("equals="+o.equals(o2));
    	System.out.println("compareTo="+((Comparable)o).compareTo((Comparable)o2));
    	//File f = new File("C:/Users/jg/workspace/relatrix/build/test.ser");
    	long stime = System.nanoTime();
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	ObjectOutputStream out = new ObjectOutputStream(baos/*new FileOutputStream(f)*/);
    	out.writeObject(o);
    	out.flush();
    	out.close();
    	byte[] ser = baos.toByteArray();
    	System.out.println("serialized size="+ser.length+" time="+(System.nanoTime()-stime));
    	stime = System.nanoTime();
    	//ObjectInputStream in = new ObjectInputStream(new FileInputStream(f)) {
    	ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(ser)) {
             @Override
             protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
                 String name = desc.getName();
                 try {
                     return Class.forName(name, true, hcl);
                 } catch (ClassNotFoundException e) {
                     return super.resolveClass(desc);
                 }
             }
             @Override
             protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
                 try {
                     return Class.forName(interfaces[0], true, hcl).getClass();
                 } catch (Exception ex) {
                     return super.resolveProxyClass(interfaces);
                 }
             }
         };
    	Object or = in.readObject();
    	System.out.println("deser time="+(System.nanoTime()-stime));
    	System.out.println("equals="+o.equals(or));
    	System.out.println("compareTo="+((Comparable)o).compareTo((Comparable)or));
    	System.out.println(RelatrixKVJson.getData((Comparable) or));
    }
}

