package com.neocoretechs.relatrix;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Proxy;

import com.neocoretechs.rocksack.DirectByteArrayOutputStream;

public class Serializer {
	
	// ... compare() unchanged except it calls deserializeObject(b, loader)
	public static Object deserializeObject(byte[] obuf, ClassLoader classLoader) throws IOException {
		try (ByteArrayInputStream bais = new ByteArrayInputStream(obuf);
			ObjectInputStream ois = new ClassLoaderObjectInputStream(bais, classLoader)) {
			Object o = ois.readObject();
			System.out.println("Deserialize object:"+o);
			return o;
		} catch (ClassNotFoundException cnf) {
			throw new IOException(cnf.toString() + ":Class Not found, may have been modified beyond version compatibility");
		} catch (IOException ioe) {
			throw new IOException("deserializeObject: " + ioe.toString() + ": from buffer of length " + obuf.length);
		}
	}

	/**
	 * Static method for object to serialized byte conversion.
	 * Uses DirectByteArrayOutputStream, which allows underlying buffer to be retrieved without
	 * copying entire backing store
	 * @param Ob the user object
	 * @return byte buffer containing serialized data
	 * @exception IOException cannot convert
	 */
	public static byte[] serializeObject(Object Ob) throws IOException {
		byte[] retbytes;
		DirectByteArrayOutputStream baos = new DirectByteArrayOutputStream();
		ObjectOutput s = new ObjectOutputStream(baos);
		s.writeObject(Ob);
		s.flush();
		baos.flush();
		retbytes = baos.getBuf();
		s.close();
		baos.close();
		System.out.println("serializeObject len:"+retbytes.length);
		return retbytes;
	}

	// inner static helper
	public static final class ClassLoaderObjectInputStream extends ObjectInputStream {
		private final ClassLoader loader;
		public ClassLoaderObjectInputStream(InputStream in, ClassLoader loader) throws IOException {
			super(in);
			this.loader = loader;
		}
		@Override
		protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
			String name = desc.getName();
			try {
				return Class.forName(name, false, loader);
			} catch (ClassNotFoundException ex) {
				return super.resolveClass(desc);
			}
		}
        @Override
        protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
            Class<?>[] intfs = new Class<?>[interfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                intfs[i] = Class.forName(interfaces[i], false, loader);
            }
            try {
                return Proxy.getProxyClass(loader, intfs);
            } catch (IllegalArgumentException e) {
                return super.resolveProxyClass(interfaces);
            }
        }
	}
}
