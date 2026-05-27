package com.neocoretechs.relatrix.client.json.util;

import java.lang.classfile.ClassFile;
import java.io.FileOutputStream;

import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.constant.ConstantDescs;
import java.lang.reflect.AccessFlag;
import java.util.Arrays;

import org.json.JSONObject;

import com.neocoretechs.relatrix.server.HandlerClassLoader;

/**
 * Use the java.lang.classfile JDK25 tooling to generate a class from the hashed fields of a JSON payload.<p>
 * The class will implement Serializable and COmparable interfaces to faciltate storage and indexing int he RocksDb/RockSack/Relatrix
 * subsystems. The bytes will be stored in a CBOR representation.
 * @author Jonathan Groff Copyright (C) NeoCoreTechs 2026
 */
public final class JsonRecordClassGenerator {
	public static final String generatedJsonClassPrefix = "com.neocoretechs.relatrix.Relatrix_";
    /**
     * Build class bytes for className suing JDK25 ClassDesc java.lang.classfile tooling.
     * <pre>
     * - implements Serializable and Comparable
     * - fields: private final long hash; private final byte[] cbor;
     * - constructor: (long, byte[])
     * - compareTo(JsonRecord): Long.compare(hash, other.hash) then ByteUtils.unsignedCompare(cbor, other.cbor)
     * - equals(Object) and hashCode()
     * - serialVersionUID set in clinit
     * </pre>
     * @param className The name of the generated class
     */
    public static byte[] buildJsonRecordClassBytes(String className) {
        ClassDesc thisClass = ClassDesc.of(className);
        ClassDesc objectClass = ClassDesc.of(Object.class.getName());
        ClassDesc serializable = ClassDesc.of(java.io.Serializable.class.getName());
        ClassDesc comparable = ClassDesc.of(java.lang.Comparable.class.getName());
        ClassDesc longClass = ClassDesc.of(long.class.getName());
        ClassDesc byteArray = ClassDesc.ofDescriptor(byte[].class.getName());
        ClassDesc arraysClass = ClassDesc.of(Arrays.class.getName());
        ClassDesc longBox = ClassDesc.of(Long.class.getName());
        ClassDesc object = ClassDesc.of(java.lang.Object.class.getName());
        ClassDesc byteUtils = ClassDesc.of("com.neocoretechs.relatrix.client.json.util.ByteUtils"); // runtime helper
        ClassDesc longDesc = ClassDesc.ofDescriptor("J"); // primitive long descriptor
        
        ClassFile cf = ClassFile.of();
        byte[] bytes = cf.build(thisClass, classBuilder -> {
            classBuilder.withFlags(AccessFlag.PUBLIC)
                        .withVersion(61, 0) // adjust if you target a different major version
                        .withInterfaceSymbols(serializable,comparable)
                        .withField("cbor", byteArray, 0x0001 | 0x0010)
                        // serialVersionUID as private static final long; we'll initialize it in <clinit>
                        .withField("serialVersionUID", longDesc, 0x0002 | 0x0010 | 0x0008)
                        // class initializer <clinit> to set serialVersionUID
                        .withMethod("<clinit>", MethodTypeDesc.of(ConstantDescs.CD_void),
                        	    0x0008, // ACC_STATIC
                        	    clinit -> clinit.withCode(code -> {
                        	        long svuid = 0x9E3779B97F4A7C15L;
                        	        code.loadConstant(svuid); // pushes primitive long (ldc2_w)
                        	        code.putstatic(thisClass, "serialVersionUID", longDesc);
                        	        code.return_();
                        	    }))
                        // constructor: public ctor(byte[] cbor)
                        .withMethod("<init>",
                                    MethodTypeDesc.of(ConstantDescs.CD_void, ClassDesc.ofDescriptor(byte[].class.getName())),
                                    0x0001, //public
                                    ctor -> ctor.withCode(code -> {
                                        // call super()
                                        code.aload(0)
                                            .invokespecial(objectClass, "<init>", MethodTypeDesc.of(ConstantDescs.CD_void));
                                        // this.cbor = cbor; (byte[] param at local index 3)
                                        code.aload(0)
                                            .aload(1)
                                            .putfield(thisClass, "cbor", byteArray);
                                        code.return_();
                                    }))

                        // compareTo(Object other): public int compareTo(Object)
                        .withMethod("compareTo",
                                    MethodTypeDesc.of(ConstantDescs.CD_int, object),
                                    0x0001, // public
                                    method -> method.withCode(code -> {
                                        code.aload(0)
                                            .getfield(thisClass, "cbor", byteArray);
                                        code.aload(1);
                                        code.checkcast(thisClass);   // <-- actual cast instruction
                                        code.astore(2);
                                        code.aload(2)
                                            .getfield(thisClass, "cbor", byteArray);
                                        code.invokestatic(byteUtils, "unsignedCompare",
                                                           MethodTypeDesc.of(ConstantDescs.CD_int, ClassDesc.ofDescriptor(byte[].class.getName()), ClassDesc.ofDescriptor(byte[].class.getName())));
                                        code.ireturn();
                                    }))
                        // equals(Object o): public boolean equals(Object)
                        .withMethod("equals",
                        		 MethodTypeDesc.of(ConstantDescs.CD_boolean, object),
                        		 0x0001,
                                        // return Arrays.equals(this.cbor, other.cbor);
                        		 method -> method.withCode(code -> {
                                        code.aload(0).getfield(thisClass, "cbor", byteArray);
                                        code.aload(1);
                                        code.checkcast(thisClass);   // <-- actual cast instruction
                                        code.astore(2);
                                        code.aload(2)
                                            .getfield(thisClass, "cbor", byteArray);
                                        code.invokestatic(byteUtils, "equalsBoolean",
                                                       MethodTypeDesc.of(ConstantDescs.CD_boolean, ClassDesc.ofDescriptor(byte[].class.getName()), ClassDesc.ofDescriptor(byte[].class.getName())));
                                        code.ireturn();
                        		 }))
                        // hashCode(): public int hashCode()
                        .withMethod("hashCode",
                                    MethodTypeDesc.of(ConstantDescs.CD_int, thisClass),
                                    0x0001, // public,
                                    method -> method.withCode(code -> {
                                        //int h1 = Arrays.hashCode(cbor);
                                        code.aload(0)
                                            .getfield(thisClass, "cbor", byteArray);
                                        code.invokestatic(arraysClass, "hashCode",
                                                           MethodTypeDesc.of(ConstantDescs.CD_int, ClassDesc.ofDescriptor(byte[].class.getName())));
                                        code.istore(1); // h1 in local 1

                                        // return 31 * h1;
                                        code.iload(1)
                                            .bipush(31)
                                            .imul()
                                            .iload(1)
                                            .iadd()
                                            .ireturn();
                                    }));
        });
        return bytes;
    }
    public static void main(String[] args) throws Exception {
       	String x = "{\"timestamp\":1779166000301,\"LeftImage\":[{ \"count\":1,\"detections\":[ {\"name\":\"refrigerator\",\"probability\":0.41232753,\"bbox\":{\"xmin\":104,\"ymin\":12,\"xmax\":223,\"ymax\":561} } ] } ], \"RightImage\":[{\"count\":0, \"detections\":[ ] } ]}";
    	HandlerClassLoader hcl = new HandlerClassLoader();
    	long tim = System.nanoTime();
    	JSONObject j = new JSONObject(x);
    	String className = RelatrixTypeSynthesizer.generateMorphicClassName(j,JsonRecordClassGenerator.generatedJsonClassPrefix);
    	byte[] b = JsonRecordClassGenerator.buildJsonRecordClassBytes(className);
		Class<?> c = Class.forName(className, false, hcl);
		if (c == null)
			c = hcl.defineAClass(className, b);
    	System.out.println("nanos="+(System.nanoTime()-tim));
    	System.out.println(c);
    	String name = "C:/Users/jg/workspace/Relatrix/build/"+className.replace(".", "/")+".class";
    	FileOutputStream f = new FileOutputStream(name);
    	f.write(b);
    	f.flush();
    	f.close();
    	c = hcl.loadClass(className);
    	c.getDeclaredConstructors();
    }
}

