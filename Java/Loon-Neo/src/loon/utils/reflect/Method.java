package loon.utils.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

@SuppressWarnings("rawtypes")
public final class Method {

	private final java.lang.reflect.Method method;

	Method(java.lang.reflect.Method method) {
		this.method = method;
	}

	public String getName() {
		return method.getName();
	}

	public Class<?> getReturnType() {
		return method.getReturnType();
	}

	public Class[] getParameterTypes() {
		return method.getParameterTypes();
	}

	public Class getDeclaringClass() {
		return method.getDeclaringClass();
	}

	public boolean isAccessible() {
		return method.isAccessible();
	}

	public void setAccessible(boolean accessible) {
		method.setAccessible(accessible);
	}

	public boolean isAbstract() {
		return Modifier.isAbstract(method.getModifiers());
	}

	public boolean isDefaultAccess() {
		return !isPrivate() && !isProtected() && !isPublic();
	}

	public boolean isFinal() {
		return Modifier.isFinal(method.getModifiers());
	}

	public boolean isPrivate() {
		return Modifier.isPrivate(method.getModifiers());
	}

	public boolean isProtected() {
		return Modifier.isProtected(method.getModifiers());
	}

	public boolean isPublic() {
		return Modifier.isPublic(method.getModifiers());
	}

	public boolean isNative() {
		return Modifier.isNative(method.getModifiers());
	}

	public boolean isStatic() {
		return Modifier.isStatic(method.getModifiers());
	}

	public boolean isVarArgs() {
		return method.isVarArgs();
	}

	public Object invoke(Object obj, Object... args) throws ReflectionException {
		try {
			return method.invoke(obj, args);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(
					"Illegal argument(s) supplied to method: " + getName(), e);
		} catch (IllegalAccessException e) {
			throw new ReflectionException("Illegal access to method: "
					+ getName(), e);
		} catch (InvocationTargetException e) {
			throw new ReflectionException("Exception occurred in method: "
					+ getName(), e);
		}
	}

}
