package com.luoyan.syntax;


import org.slf4j.Logger;    
import org.slf4j.LoggerFactory; 

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.text.*;

/**
 * Hello world!
 *
 */

public class App 
{
    private Logger logger = LoggerFactory.getLogger(App.class);
    private int num;
    public App(int n) {
    	this.num = num;
    }
    public void print() {
    	System.out.println("num = " + this.num);
    }
    public void testLog(){       
        logger.info("this is a test log");       
    } 
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App a = new App(0);
        a.testLog();
        Date d = new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddkkmmss");
        String str=sdf.format(d);
        System.out.println("The date is : "+str);
        
		try {
			/*
			Class<?> clazz = Class.forName("com.luoyan.syntax.App");
			java.lang.reflect.Constructor<?> ctor = clazz.getConstructor(Integer.class);
			Object object = ctor.newInstance(new Object[] { 1 });*/
			Class c;
			c = Class.forName("com.luoyan.syntax.App");
	        Class[] parameterTypes={int.class};
	        java.lang.reflect.Constructor constructor=c.getConstructor(parameterTypes);
	        Object[] parameters={1};
	        App o=(App)constructor.newInstance(parameters);
	        o.print();
	        LogHandlerBolt bolt = new LogHandlerBolt();
	        ILogHandler handler = bolt.getLogHandler("com.luoyan.syntax.HdfsLogHandler", 200);
	        handler.parseInfo(new ArrayList<String>());
	        String str2 = "hellowold";
	        String str3 = "hellowold2";
	        System.out.println("hashcode2 " + str2.hashCode() + " hashcode3 " + str3.hashCode());
	        
	        long n=Long.parseLong("1403489927594");
	        System.out.println("long " + n);
	        
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
