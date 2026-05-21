package com.neocoretechs.relatrix.client.json.util;

import java.lang.classfile.ClassFile;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.lang.classfile.Attribute;
import java.lang.classfile.CodeModel;
import java.lang.classfile.TypeKind;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.lang.constant.ConstantDescs;
import java.lang.reflect.AccessFlag;
import java.util.Arrays;

public final class JsonRecordClassGenerator {

    /**
     * Build class bytes for com.example.JsonRecord
     * - implements Serializable and Comparable
     * - fields: private final long hash; private final byte[] cbor;
     * - constructor: (long, byte[])
     * - compareTo(JsonRecord): Long.compare(hash, other.hash) then ByteUtils.unsignedCompare(cbor, other.cbor)
     * - equals(Object) and hashCode()
     * - serialVersionUID set in <clinit>
     */
    public static byte[] buildJsonRecordClassBytes() {
        ClassDesc thisClass = ClassDesc.of("com.neocoretechs.relatrix.JsonRecord");
        ClassDesc objectClass = ClassDesc.of(Object.class.getName());
        ClassDesc serializable = ClassDesc.of(java.io.Serializable.class.getName());
        ClassDesc comparable = ClassDesc.of(java.lang.Comparable.class.getName());
        ClassDesc longClass = ClassDesc.of(long.class.getName());
        ClassDesc byteArray = ClassDesc.ofDescriptor(byte[].class.getName());
        ClassDesc arraysClass = ClassDesc.of(Arrays.class.getName());
        ClassDesc longBox = ClassDesc.of(Long.class.getName());
        ClassDesc byteUtils = ClassDesc.of("com.neocoretechs.relatrix.client.json.util.ByteUtils"); // runtime helper

        ClassFile cf = ClassFile.of();
        byte[] bytes = cf.build(thisClass, classBuilder -> {
            classBuilder.withFlags(AccessFlag.PUBLIC)
                        .withVersion(61, 0) // adjust if you target a different major version
                        .withInterfaceSymbols(serializable)
                        .withInterfaceSymbols(comparable)

                        // fields
                        .withField("hash", longClass,  0x0002 | 0x0010)
                        .withField("cbor", byteArray, 0x0002 | 0x0010)
                        // serialVersionUID as private static final long; we'll initialize it in <clinit>
                        .withField("serialVersionUID", longClass, 0x0002 | 0x0010 | 0x0008)

                        // class initializer <clinit> to set serialVersionUID
                        .withMethod("<clinit>", MethodTypeDesc.of(ConstantDescs.CD_void),
                        		0x0008, //static
                                    clinit -> clinit.withCode(code -> {
                                        // push long constant (example value); choose your own stable value if desired
                                        long svuid = 0x9E3779B97F4A7C15L; // example stable constant
                                        code.ldc(svuid) // ldc2_w for long
                                            .putstatic(thisClass, "serialVersionUID", longClass)
                                            .return_();
                                    }))

                        // constructor: public JsonRecord(long hash, byte[] cbor)
                        .withMethod("<init>",
                                    MethodTypeDesc.of(ConstantDescs.CD_void, ConstantDescs.CD_long, ClassDesc.ofDescriptor(byte[].class.getName())),
                                    0x0001, //public
                                    ctor -> ctor.withCode(code -> {
                                        // call super()
                                        code.aload(0)
                                            .invokespecial(objectClass, "<init>", MethodTypeDesc.of(ConstantDescs.CD_void));
                                        // this.hash = hash; (long occupies locals 1 & 2)
                                        code.aload(0)
                                            .lload(1)
                                            .putfield(thisClass, "hash", longClass);
                                        // this.cbor = cbor; (byte[] param at local index 3)
                                        code.aload(0)
                                            .aload(3)
                                            .putfield(thisClass, "cbor", byteArray);
                                        code.return_();
                                    }))

                        // compareTo(JsonRecord other): public int compareTo(JsonRecord)
                        .withMethod("compareTo",
                                    MethodTypeDesc.of(ConstantDescs.CD_int, thisClass),
                                    0x0001, // public
                                    method -> method.withCode(code -> {
                                        // int cmp = Long.compare(this.hash, other.hash);
                                        //code.aload(0)
                                         //   .getfield(thisClass, "hash", longClass);
                                        //code.aload(1)
                                        //    .getfield(thisClass, "hash", longClass);
                                        //code.invokestatic(longBox, "compare",
                                        //                   MethodTypeDesc.of(ConstantDescs.CD_int, ConstantDescs.CD_long, ConstantDescs.CD_long));
                                        //code.istore(2); // cmp in local 2

                                        // if (cmp != 0) return cmp;
                                        //code.iload(2);//.ifne((label -> {code.iload(2).ireturn();}));
                                        
                                        // else return ByteUtils.unsignedCompare(this.cbor, other.cbor);
                                        code.aload(0)
                                            .getfield(thisClass, "cbor", byteArray);
                                        code.aload(1)
                                            .getfield(thisClass, "cbor", byteArray);
                                        code.invokestatic(byteUtils, "unsignedCompare",
                                                           MethodTypeDesc.of(ConstantDescs.CD_int, ClassDesc.ofDescriptor(byte[].class.getName()), ClassDesc.ofDescriptor(byte[].class.getName())));
                                        code.ireturn();
                                    }))
                        // equals(Object o): public boolean equals(Object)
                        .withMethod("equals",
                        		 MethodTypeDesc.of(ConstantDescs.CD_boolean, thisClass),
                        		 0x0001,
                                        // return Arrays.equals(this.cbor, other.cbor);
                        		 method -> method.withCode(code -> {
                                        code.aload(0).getfield(thisClass, "cbor", byteArray);
                                        code.aload(2)
                                            .getfield(thisClass, "cbor", byteArray);
                                        code.invokestatic(arraysClass, "equals",
                                                           MethodTypeDesc.of(ConstantDescs.CD_boolean, ClassDesc.ofDescriptor(byte[].class.getName()), 
                                                        		   ClassDesc.ofDescriptor(byte[].class.getName())));
                                        code.return_(TypeKind.BOOLEAN);}))
                        // hashCode(): public int hashCode()
                        .withMethod("hashCode",
                                    MethodTypeDesc.of(ConstantDescs.CD_int),
                                    0x0001, // public,
                                    method -> method.withCode(code -> {
                                        // int h1 = Long.hashCode(this.hash);
                                        //code.aload(0)
                                        //    .getfield(thisClass, "hash", longClass);
                                       // code.invokestatic(longBox, "hashCode",
                                       //                    MethodTypeDesc.of(ConstantDescs.CD_int, ConstantDescs.CD_long));
                                       // code.istore(1); // h1 in local 1

                                        // int h2 = Arrays.hashCode(this.cbor);
                                        code.aload(0)
                                            .getfield(thisClass, "cbor", byteArray);
                                        code.invokestatic(arraysClass, "hashCode",
                                                           MethodTypeDesc.of(ConstantDescs.CD_int, ClassDesc.ofDescriptor(byte[].class.getName())));
                                        code.istore(2); // h2 in local 2

                                        // return 31 * h1 + h2;
                                        code.iload(1)
                                            .bipush(31)
                                            .imul()
                                            .iload(2)
                                            .iadd()
                                            .ireturn();
                                    }));
        });
        return bytes;
    }
    public static void main(String[] args) throws Exception {
    	byte[] b = JsonRecordClassGenerator.buildJsonRecordClassBytes();
    	FileOutputStream f = new FileOutputStream("C:/Users/jg/workspace/Relatrix/build/JsonRecord.class");
    	f.write(b);
    	f.flush();
    	f.close();
    }
}

