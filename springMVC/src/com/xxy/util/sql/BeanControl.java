package com.xxy.util.sql;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.Modifier;

import com.xxy.util.SmallMethods;

/**
 * 类的反射机制处理
 * @author xingyuan
 * @date 2016-4-13
 * <!------------------>
 */
public class BeanControl {
	
	/**
	 * 将class类解析后返回该类中的所有变量名及属性(以数组的形式返回)<br>
	 * e.g. name-String
	 * @author xingyuan
	 * @date 2016-3-7
	 * <!------------------>
	 * @param cls
	 * @return String[]
	 */
	public static String[] getAttr(Class<?> cls){
		String [] str = null;
		for(;cls != Object.class;cls = cls.getSuperclass()){
			Field[] fs = cls.getDeclaredFields();
			str = new String[fs.length];
			int i = 0;
			for(Field f : fs){
				String s = f.getName()+"-"+f.getType().getSimpleName();
				str[i] = s;
				i++;
			}
		}
		return str;
	}
	
	/**
	 * 将class类解析后返回该类中的所有变量名及属性(以List的形式返回)<br>
	 * 仅取private属相的变量<br>
	 * e.g. name-String
	 * @author xingyuan
	 * @date 2016-3-7
	 * <!------------------>
	 * @param cls
	 * @return List<String>
	 */
	public static List<String> getAttrList(Class<?> cls){
		List<String> l = new ArrayList<String>();
		for(;cls != Object.class;cls = cls.getSuperclass()){
			Field[] fs = cls.getDeclaredFields();
			for(Field f : fs){
				String modifiers = Modifier.toString(f.getModifiers());
				if(modifiers.startsWith("private")){
					String s = f.getName()+"-"+f.getType().getSimpleName();
					l.add(s);
				}
			}
		}
		return l;
	}
	
	/**
	 * 拿到类所有(私有)参数的名与值,并放入map中
	 * <br>map中key值是参数名 value为Object类型
	 * @author xingyuan
	 * @date 2016-4-13
	 * <!------------------>
	 * @param cla
	 * @param obj
	 * @param list 通过getAttrList(cla)这个方法获取
	 * @return
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException Map<String,Object>
	 */
	public static Map<String,Object> getClaMap(Class<?> cla,Object obj,List<String> list) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		System.out.println("dispose the bean:"+cla);
		 
		Map<String,Object> map = new HashMap<String, Object>();
		
		for(String str : list){
			String sname = str.substring(0, str.indexOf("-"));
			//String stype = str.substring(str.indexOf("-")+1, str.length());
			
			//将字段名首字母大写
			String sname2 = SmallMethods.replaceFirst(sname).trim();
			//执行get方法
			Method m = obj.getClass().getMethod("get"+sname2);
			if(m.invoke(obj) != null){
				map.put(sname, m.invoke(obj));
			}
		}
		return map;
	}

}
