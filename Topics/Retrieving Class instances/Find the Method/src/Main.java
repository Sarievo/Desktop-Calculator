import java.lang.reflect.Method;

class MethodFinder {

    public static String findMethod(String methodName, String[] classNames) {
        try {
            for (String name : classNames) {
                Method[] methods = Class.forName(name).getMethods();
                for (Method method : methods) {
                    if (methodName.equals(method.getName())) {
                        return name;
                    }
                }
            }
        } catch (Exception ignored) { }

        return null;  // in case anything happens, we just return null
    }
}
