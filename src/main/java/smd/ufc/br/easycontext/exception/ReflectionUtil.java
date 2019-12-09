package smd.ufc.br.easycontext.exception;

import java.lang.reflect.Method;

public class ReflectionUtil {

    public static String getMethodsFromClass(Class klass){
        StringBuilder sb = new StringBuilder();

        for(Method method : klass.getDeclaredMethods()){
            sb.append(method.getName());
            sb.append("(");
            int numOfParameters = 0;
            for(Class c : method.getParameterTypes()){
                if(c.getName().equalsIgnoreCase("setAction") ||
                        c.getName().equalsIgnoreCase("setName") ||
                        c.getName().equalsIgnoreCase("build") ||
                        c.getName().equalsIgnoreCase("Builder") ){
                    continue;
                }
                sb.append(c.getName());
                if(numOfParameters > 0 && numOfParameters < method.getParameterTypes().length - 2)
                    sb.append(", ");
                numOfParameters++;
            }
        }
        return sb.toString();
    }
}
