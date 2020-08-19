package leppa.planarartifice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * A class with a collection of methods to make reflection easier to use.
 * @author Leppa
 */
public class ReflectionUtils{
	
	public static Object getPrivateObject(String fieldName, Object instance){
		try{
			Field f = instance.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			return f.get(instance);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Invoke the method methodName on object instance. <BR/> Same as calling instance.methodName, but allows you to use private methods. Don't use this for voids; use invokePrivateMethod instead.
	 * @param methodName - The name of the method
	 * @param instance - The object being used to invoke it. If the method is static, this can be null.
	 * @param args - A list of things passed into the method.
	 */
	public static Object getPrivateMethod(String methodName, Object instance, Object...args){
		try{
			Method f = instance.getClass().getDeclaredMethod(methodName);
			f.setAccessible(true);
			return f.invoke(instance, args);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static void setPrivateObject(String fieldName, Object instance, Object value){
		try{
			Field f = instance.getClass().getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(instance, value);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Invoke the method methodName on object instance. <BR/> Same as calling instance.methodName, but allows you to use private methods.
	 * @param methodName - The name of the method
	 * @param instance - The object being used to invoke it. If the method is static, this can be a new instance of your class.
	 * @param args - A list of things passed into the method.
	 */
	public static void invokePrivateMethod(String methodName, Object instance, Object...args){
		try{
			Method f = instance.getClass().getDeclaredMethod(methodName);
			f.setAccessible(true);
			f.invoke(instance, args);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}