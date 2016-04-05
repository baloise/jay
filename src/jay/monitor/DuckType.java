package jay.monitor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DuckType implements InvocationHandler {

	public static class Let {
		private Object object;

		public Let(Object object) {
			this.object = object;
		}

		private void checkQuackLike(Object object, Class<?> interfaceClass) throws InvocationTargetException {
			Class<?> candclass = object.getClass();
			StringBuilder sb = new StringBuilder(candclass.getName()+" does not implement\n");
			int initialLength = sb.length();
			for (Method method : interfaceClass.getMethods()) {
				try {
					candclass.getMethod(method.getName(), method.getParameterTypes());
				} catch (NoSuchMethodException e) {
					sb.append(" - "+method+"\n");
				}
			}
			if(sb.length()> initialLength) throw new InvocationTargetException(null, sb.toString());
		}
		
		public <T> T be(Class<T> interfaceClass) throws InvocationTargetException {
			checkQuackLike(object, interfaceClass);
			return implement(object, interfaceClass);
		}
		
		public <T> T wannaBe(Class<T> interfaceClass) {
			return implement(object, interfaceClass);
		}
		
		private <T> T implement(Object object, Class<T> interfaceToImplement) {
			return (T) Proxy.newProxyInstance(interfaceToImplement.getClassLoader(),
					new Class[] { interfaceToImplement}, new DuckType(object));
		}
	}

	public static class Does {
		private Class<? extends Object> candclass;

		public Does(Object object) {
			 candclass = object.getClass();
		}
		
		public boolean quackLike(Class<?> interfaceClass) {
			for (Method method : interfaceClass.getMethods()) {
				try {
					candclass.getMethod(method.getName(), method.getParameterTypes());
				} catch (NoSuchMethodException e) {
					return false;
				}
			}
			return true;
		}
		
	}

	public static Let let(Object object) {
		return new Let(object);
	}
	
	public static Does does(Object object) {
		return new Does(object);
	}

	protected DuckType(Object object) {
		this.object = object;
		this.objectClass = object.getClass();
	}

	protected Object object;
	protected Class objectClass;

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Method realMethod = objectClass.getMethod(method.getName(), method.getParameterTypes());
		if (!realMethod.isAccessible()) {
			realMethod.setAccessible(true);
		}
		return realMethod.invoke(object, args);
	}
}