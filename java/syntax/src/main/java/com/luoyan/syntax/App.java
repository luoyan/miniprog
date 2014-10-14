package com.luoyan.syntax;


import org.slf4j.Logger;    
import org.slf4j.LoggerFactory; 

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    class C {
    	public C(IBase b, int a) {
    		b.foo(a);
    	}
    }
    
	private void testInterface() {
		Base b = new Base();
		C c = new C(b, 1);
		
	}
	
	public static Date getDateWithoutTime(final Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return getDateWithoutTime(c);
	}

	public static Date getDateWithoutTime(final Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public void testDate() throws ParseException {
		String dateStr = "2014-12-01";
        //Date date = getDateWithoutTime(
        //        new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(dateStr));
        Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
        System.out.println("month " + cal.get(Calendar.MONTH) + " old " + date.getMonth());
		cal.set(Calendar.MONTH, (cal.get(Calendar.MONTH)+1));
        System.out.println("month " + cal.get(Calendar.MONTH) + " old " + date.getMonth());
        Date date2 = cal.getTime();
        System.out.println("date " + date.toString() + " date2 " + date2.toString());
	}
	
	public static void testStringBuffer() {
		StringBuffer recommendContentBuffer = new StringBuffer();
		recommendContentBuffer.append("hello");
		System.out.println("recommendContentBuffer " + recommendContentBuffer.toString());
		recommendContentBuffer.append("world");
		System.out.println("recommendContentBuffer " + recommendContentBuffer.toString());
	}
	
    public static void date2string() throws ParseException {
        String dateStr = "2014-07-09";

        Date date = getDateWithoutTime(
                new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(dateStr));
        
        System.out.println("date " + date.toString());
        System.out.println("format " + new SimpleDateFormat("yyyy-MM-dd HH:mm:SS.s").format(date));
    }
    
    public static void testEnumType() {
    	for (EnvironmentType env : EnvironmentType.values()) {
    		System.out.println("env " + env.name());
    	}
    }
    
    public static void main( String[] args ) throws ParseException
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
	        System.out.println("A " + ConstEnum.A);
	        date2string();
	        a.testDate();
	        testEnumType();
	        testStringBuffer();
	        a.testInterface();
	        int nA = 6;
	        int nB = 8;
	        System.out.println(nA + "\t" + nB);
	        boolean b = false;
	        System.out.println("false " + b);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
