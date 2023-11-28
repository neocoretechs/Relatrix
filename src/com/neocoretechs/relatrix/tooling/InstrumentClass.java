package com.neocoretechs.relatrix.tooling;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectStreamClass;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import com.neocoretechs.relatrix.CompareAndSerialize;
import com.neocoretechs.relatrix.ComparisonOrderField;
import com.neocoretechs.relatrix.ComparisonOrderMethod;
/**
 * Instrument a class for Relatrix compatibility. Nothing we do changes the base functionality
 * or should affect operation of the original class. We need to ensure that it is Serializable
 * and that it implements a compareTo method to order it in the data store. Note that most
 * Java classes do this by default, but new ad-hoc classes may not. We rely on user adding
 * annotations for CompareAndSerialize and ComparisonOrder(order=n) where n is the order
 * of the key element in the overall collection that will order instances of the class.
 * We will create a compareTo method based on this order.
 * @author groff
 *
 */
public class InstrumentClass {
	private static boolean DEBUG = false;
	private boolean hasAtLeastOneMethod = false; // if we have at least one accessor generate an int in compareTo to hold results
	/**
	 * Does it implement Serializable and Comparable already? Does it extend a Comparable such
	 * that super() needs called on new compareTo? Do instrumented fields implement Comparable?
	 * @param object
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public String process(String javaFile, Object object, boolean callSuperCompareTo) throws IOException, IllegalArgumentException, IllegalAccessException {
        checkIfSerializable(object);
        //initializeObject(object);
        Map<Integer, NameAndType> elements = getFieldOrder(object);
        getMethodOrder(object, elements);
        List<String> compareToElements = generateCompareTo(object, elements, callSuperCompareTo);
        String compareToStatement = generateCompareTo(compareToElements);
        if(DEBUG) {
        	elements.entrySet().stream().forEach(e -> System.out.println(e.getKey() + ":" + e.getValue()));
        	compareToElements.stream().forEach(e -> System.out.println(e));
        	System.out.println(compareToStatement);
        }
        return compareToStatement;
	}

	private void checkIfSerializable(Object object) throws IOException {
        if (Objects.isNull(object)) {
            throw new IOException("Can't serialize a null object");
        }
        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(CompareAndSerialize.class)) {
            throw new IOException("The class " + clazz.getSimpleName() + " is not annotated with CompareAndSerialize");
        }
    }

	static String resolveClass(Class classname) throws ClassNotFoundException {
        //Class<?> cl = Class.forName(classname);
        ObjectStreamClass desc = ObjectStreamClass.lookup(classname);
        if (desc != null) {
            return "\tprivate static final long serialVersionUID = " +
                desc.getSerialVersionUID() + "L;";
        } else {
            return null;
        }
    }
   /* private void initializeObject(Object object) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Init.class)) {
                method.setAccessible(true);
                method.invoke(object);
            }
        }
    }*/
	/**
	 * Establish field order for use in creation of compareTo method (primary key)
	 * based on annotation in class. If the user failed to supply an order use the 
	 * order of appearance of the fields as default. If we encounter a value, it will always supercede any
	 * default value present. Defaults encountered after initial ordering will increment.
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
    private Map<Integer, NameAndType> getFieldOrder(Object object) throws IllegalArgumentException, IllegalAccessException {
    	int defaultOrder = 0;
        Class<?> clazz = object.getClass();
        Map<Integer, NameAndType> elementsMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(ComparisonOrderField.class)) {
               	NameAndType nameAndType = new NameAndType((String) field.getName(), field.getType(), true, field);
            	int order = getKey(field);
            	if(order == 0) {
            		++defaultOrder;
            		order = defaultOrder;
            	} else {
            		defaultOrder = order;
            	}
                elementsMap.put(order, nameAndType);
            }
        }
        return elementsMap;
    }
	/**
	 * Establish method order for use in in creation of compatreTo method for primary key based on 
	 * annotation in class. If the user failed to supply an order use the 
	 * order of appearance of the fields as default. If we encounter a value, it will always supercede any
	 * default value present. Defaults encountered after initial ordering will increment.
	 * @param object
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
    private void getMethodOrder(Object object, Map<Integer, NameAndType> elementsMap) throws IllegalArgumentException, IllegalAccessException {
    	int defaultOrder = elementsMap.size();
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ComparisonOrderMethod.class)) {
            	hasAtLeastOneMethod = true;
                method.setAccessible(true);
               	NameAndType nameAndType = new NameAndType((String) method.getName(), method.getReturnType(), false, method);
            	int order = getKey(method);
            	if(order == 0) {
            		++defaultOrder;
            		order = defaultOrder;
            	} else {
            		defaultOrder = order;
            	}
                elementsMap.put(order, nameAndType);
            }
        }
    }
    
    private int getKey(Field field) {
    	 return field.getAnnotation(ComparisonOrderField.class).order();
    }
    
    private int getKey(Method method) {
   	 return method.getAnnotation(ComparisonOrderMethod.class).order();
    }
    
	private String generateCompareTo(List<String> compareToElements) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t@Override\r\n");
		sb.append("\tpublic int compareTo(Object o) {\r\n");
		if(hasAtLeastOneMethod) {
			sb.append("\t\tint n;\r\n");
		}
		compareToElements.stream().forEach(e -> {
			sb.append(e.toString());
		});
		sb.append("\t\treturn 0;\r\n");
		sb.append("\t}\r\n");
		return sb.toString();
	}
	
    private List<String> generateCompareTo(Object object, Map<Integer, NameAndType> elements, boolean callSuper) {
    	ArrayList<String> compareToComponents = new ArrayList<String>();
    	if(callSuper) {
    		StringBuilder s = new StringBuilder();
	   		s.append("\t\tn = super.compareTo(o);\r\n");
  			s.append("\t\tif(n != 0)");
			s.append("\r\n\t\t\treturn n;\r\n");
			compareToComponents.add(s.toString());
    	}
    	Stream<Map.Entry<Integer, NameAndType>> sorted =
    		    elements.entrySet().stream().sorted(Map.Entry.comparingByKey());
    	sorted.forEach(e ->  {
    		NameAndType key = (NameAndType)e.getValue();
   			StringBuilder s = new StringBuilder();
   			// primitive or object field or method?
    		if(key.isField) {
    			if(key.type.isPrimitive()) {
    				s.append("\t\tif(");
    				s.append(key.name);
    				s.append(" < ");
    				s.append("((");
    				s.append(object.getClass().getSimpleName());
    				s.append(")o).");
    				s.append(key.name);
    				s.append(")");
    				s.append("\r\n\t\t\treturn -1;\r\n");
    				//
    				s.append("\t\tif(");
    				s.append(key.name);
    				s.append(" > ");
    				s.append("((");
    				s.append(object.getClass().getSimpleName());
    				s.append(")o).");
    				s.append(key.name);
    				s.append(")");
    				s.append("\r\n\t\t\treturn 1;\r\n");
    			} else { // object field
    	 			// use of hasAtLeastOneMethod to generate n temp var
    	   			if(!Comparable.class.isAssignableFrom(key.type))
        				throw new RuntimeException("Object Field "+key.name+" must implement Comparable interface");
    	   			s.append("\t\tn = ");
    	   			s.append(key.name);
    	   			s.append(".compareTo(((");
    				s.append(object.getClass().getSimpleName());
    				s.append(")o).");
    				s.append(key.name);
    				s.append(");\r\n");
      				s.append("\t\tif(n != 0)");
    				s.append("\r\n\t\t\treturn n;\r\n");
    				//
    			}
    		} else { // accessor method
    			if(key.type.isPrimitive()) { //accessor method type is returnType
    				s.append("\t\tif(");
    				s.append(key.name);
    				s.append("() < ");
    				s.append("((");
    				s.append(object.getClass().getSimpleName());
    				s.append(")o).");
    				s.append(key.name);
    				s.append("())");
    				s.append("\r\n\t\t\treturn -1;\r\n");
    				//
    				s.append("\t\tif(");
    				s.append(key.name);
    				s.append("() > ");
    				s.append("((");
    				s.append(object.getClass().getSimpleName());
    				s.append(")o).");
    				s.append(key.name);
    				s.append("())");
    				s.append("\r\n\t\t\treturn 1;\r\n");
    			} else { // accessor returns object
      	 			// use of hasAtLeastOneMethod to generate n temp var
    	   			if(!Comparable.class.isAssignableFrom(key.type))
        				throw new RuntimeException("Accessor Object return Field "+key.name+" must implement Comparable interface");
    	   			s.append("\t\tn = ");
    	   			s.append(key.name);
    	   			s.append("()");
    	   			s.append(".compareTo(((");
    				s.append(object.getClass().getSimpleName());
    				s.append(")o).");
    				s.append(key.name);
    	   			s.append("());\r\n");
      				s.append("\t\tif(n != 0)");
    				s.append("\r\n\t\t\treturn n;\r\n");
    			}
    		}
    		compareToComponents.add(s.toString());
    	});
    	return compareToComponents;
    }
    /**
     * Defines the name of the field or method, the class of the field or class of return type of method,
     * whether field or method, and actual reflected Field or Method
     *
     */
    static class NameAndType {
		String name;
    	Class type;
    	boolean isField;
    	AccessibleObject comparor;
       	public NameAndType(String name, Class type, boolean isField, AccessibleObject comparor) {
    			this.name = name;
    			this.type = type;
    			this.isField = isField;
    			this.comparor = comparor;
    	}
       	@Override
       	public String toString() {
       		return "Field name:"+name+" type:"+type+" isField:"+isField+" element:"+comparor;
       	}
    }
    
}
