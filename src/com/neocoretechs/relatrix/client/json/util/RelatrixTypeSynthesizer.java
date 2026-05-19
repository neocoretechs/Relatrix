package com.neocoretechs.relatrix.client.json.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

public class RelatrixTypeSynthesizer {

    private static boolean DEBUG = true;
	/**
     * Generates a deterministic class name based on the unique structure of an ad-hoc node.
     */
    public static String generateMorphicClassName(JSONObject node, String classPrefix) {
        List<String> structuralTokens = new ArrayList<>();
        extractStructuralTokens("", node, structuralTokens);
        
        // Ensure tokens are absolutely sorted so order doesn't change the hash
        Collections.sort(structuralTokens);
        
        // Create the canonical signature string
        String canonicalSignature = String.join(";", structuralTokens);
        
        // Hash the signature using a standard deterministic fingerprint
        String structureHash = computeFastHash(canonicalSignature);
        
        // Return a clean, safe Java class name identifier
        return classPrefix + "_" + structureHash;
    }

    private static void extractStructuralTokens(String path, JSONObject node, List<String> tokens) {
    	String[] names = JSONObject.getNames(node);
    	for(int i = 0; i < names.length; i++) {
    		String fieldName = names[i];
    		Object o = node.get(fieldName);
            	if(DEBUG )
            		System.out.println("Fieldname="+fieldName);
            	tokens.add(fieldName);
                String currentPath = path.isEmpty() ? fieldName : path + "." + fieldName;   
                // If it's a leaf node, capture the path and type precedence
                if (!(o instanceof JSONObject)) {
                	if(o instanceof JSONArray) {
                	    // For arrays, tokenize based on the array type itself
                		if(DEBUG)
                			System.out.println("array"+path + "[]:" + node.getMapType());
                		JSONArray ja = (JSONArray)o;
                		for(int j = 0; j < ja.length(); j++)
                			extractStructuralTokens(currentPath, ja.getJSONObject(j),tokens);
                		//System.out.println(Arrays.toString(ja.toList().toArray()));
                        //tokens.add(path + "[]:" + node.getMapType());
                	} else {
                		if(DEBUG)
                			System.out.println("element="+currentPath + ":" + o);	
                		//tokens.add(currentPath + ":" + eit.getValue());
                	}
                } else {
                    // Recursively walk nested objects or arrays to handle composed morphisms
                    extractStructuralTokens(currentPath, (JSONObject) o, tokens);
                }
            }
    }
    
    public static String generateMorphicPayload(JSONObject node, String classPrefix, CborBuilder cb) {
        List<String> structuralTokens = new ArrayList<>();
        extractStructuralTokensAndBuildCBOR("", node, structuralTokens, cb);
        
        // Ensure tokens are absolutely sorted so order doesn't change the hash
        Collections.sort(structuralTokens);
        
        // Create the canonical signature string
        String canonicalSignature = String.join(";", structuralTokens);
        
        // Hash the signature using a standard deterministic fingerprint
        String structureHash = computeFastHash(canonicalSignature);
        
        // Return a clean, safe Java class name identifier
        return classPrefix + "_" + structureHash;
    }

    private static void extractStructuralTokensAndBuildCBOR(String path, JSONObject node, List<String> tokens, CborBuilder cbuild) {
    	String[] names = JSONObject.getNames(node);
    	Arrays.sort(names);
    	MapBuilder<CborBuilder> mb = cbuild.addMap();
    	for(int i = 0; i < names.length; i++) {
    		String fieldName = names[i];
    		Object o = node.get(fieldName);
            	if(DEBUG )
            		System.out.println("Fieldname="+fieldName);
            	tokens.add(fieldName);
            	mb.put(fieldName,String.valueOf(o));
                String currentPath = path.isEmpty() ? fieldName : path + "." + fieldName;   
                // If it's a leaf node, capture the path and type precedence
                if (!(o instanceof JSONObject)) {
                	if(o instanceof JSONArray) {
                	    // For arrays, tokenize based on the array type itself
                		if(DEBUG)
                			System.out.println("array"+path + "[]:" + node.getMapType());
                		JSONArray ja = (JSONArray)o;
                		for(int j = 0; j < ja.length(); j++) {
                			extractStructuralTokensAndBuildCBOR(currentPath, ja.getJSONObject(j), tokens, cbuild);
                		}
                		//System.out.println(Arrays.toString(ja.toList().toArray()));
                        //tokens.add(path + "[]:" + node.getMapType());
                	} else {
                		if(DEBUG)
                			System.out.println("element="+currentPath + ":" + o);	
                		//tokens.add(currentPath + ":" + eit.getValue());
                	}
                } else {
                    // Recursively walk nested objects or arrays to handle composed morphisms
                    extractStructuralTokensAndBuildCBOR(currentPath, (JSONObject) o, tokens, cbuild);
                }
            }
    	mb.end();
    }

    private static void decode(byte[] encodedBytes) throws CborException {
    	ByteArrayInputStream bais = new ByteArrayInputStream(encodedBytes);
    	List<DataItem> dataItems = new CborDecoder(bais).decode();
    	for(DataItem dataItem : dataItems) {
    	   System.out.println(dataItem);
    	}
    }
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
    	//System.out.println(generateMorphicClassName(jo,"Relatrix_"));
    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
    	CborBuilder cb = new CborBuilder();
    	System.out.println(generateMorphicPayload(jo,"Relatrix_",cb));
    	new CborEncoder(baos).encode(cb.build());
    	byte[] encodedBytes = baos.toByteArray();
    	System.out.println(Arrays.toString(encodedBytes)+" length="+encodedBytes.length);
    	decode(encodedBytes);
    }
}

