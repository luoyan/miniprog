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

    private String extendsTypeList(String typeList) {
    	String[] items = typeList.split("\\|");
    	//System.out.println("items.length " + items.length);
    	for (String item : items) {
    		//System.out.println("item " + item);
    	}
    	//if (items.length <= 1)
    	//	return typeList;
    	List<String> joinedTypeListArrayPrev = new ArrayList<String>();
    	List<String> joinedTypeListArray = null;
    	String[] typesPrev = items[0].split(","); 
    	for (String type : typesPrev) {
    		joinedTypeListArrayPrev.add(type);
    	}
    	for (int i = 1; i < items.length; i++) {
    		if (items[i].length() > 0) {
    			joinedTypeListArray = new ArrayList<String>();
    			String[] types = items[i].split(",");
    			/*
    			for (String type : joinedTypeListArrayPrev) {
    				System.out.println("A : " + type);
    			}
    			for (String type : types) {
    				System.out.println("B : " + type);
    			}*/
    			for (int j = 0;j < types.length;j++) {
    				for (String typePrev : joinedTypeListArrayPrev) {
    					String type = typePrev + "|" + types[j];
    					//System.out.println("i = " + i + " " + type);
    					joinedTypeListArray.add(type);
    				}
    			}
    			joinedTypeListArrayPrev = joinedTypeListArray;
    		}
    	}
    	String finalTypeList = "";
    	for (int i = 0; i < joinedTypeListArrayPrev.size();i++) {
    		if (i > 0)
    			finalTypeList = finalTypeList + ",";
    		finalTypeList = finalTypeList + joinedTypeListArrayPrev.get(i);
    	}
    	return finalTypeList;
    }
    
    private void testExtendsTypeList(String typeList) {
        //System.out.println("before extendsTypeList [" + typeList + "]");
        String newTypeList = extendsTypeList(typeList);
        System.out.println("after extendsTypeList [" + typeList + "] -> [" + newTypeList + "]");
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
        String dataStrList="";
        System.out.println("dataStrList len " + dataStrList.length());
        String[] dataList = dataStrList.split("\t");
        System.out.println("dataList len " + dataList.length);
        String typeIdInfo = "total_total_cpd_total|total_total_cpd_total|| ";
		String[] items = typeIdInfo.split("\\|");
        System.out.println("items len " + items.length);
        String imeiStr1 = "";
        String imeiStr2 = "abc";
        System.out.println("imeiStr1 " + imeiStr1.isEmpty());
        System.out.println("imeiStr2 " + imeiStr2.isEmpty());
        
        a.testExtendsTypeList("a");
        a.testExtendsTypeList("a,c|b,d,e");
        a.testExtendsTypeList("a,c|");
        a.testExtendsTypeList("a|b,d|f,g|h,i,j");
        
		try {
			String pvStatType = "FeaturedPosition_0_cn.hourroom.app";
			items = pvStatType.split("_");
			String packageName = pvStatType.substring("FeaturedPosition_".length() + items[1].length() + 1);
			System.out.println("FeaturedPosition_.length() " + "FeaturedPosition_".length());
			System.out.println("items[1].length() " + items[1].length());
			System.out.println("packageName " + packageName);
	        Date newVersionDate = null;
	        Date date = null;
	        try {
				newVersionDate = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse("2014-12-23");
				date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse("2014-12-13");
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        System.out.println(" before " + date.before(newVersionDate));
	        String typeId = "V2_1_a__c_d";//term [0-4] null
	        String[] terms = (typeId + "_End").split("_");
	        System.out.println("terms.length " + terms.length);
	        for (int i = 1;i < terms.length - 1;i++) {
	        	System.out.println("terms [" + (i-1) + "]\t" + terms[i]);
	        	
	        }
	        String positionsStr = "";
	        String[] positionsStrArray = positionsStr.split("\\[|\\]|\\,");
	        System.out.println("positionsStr.length " + positionsStr.length());
	        System.out.println("positionsStrArray.length " + positionsStrArray.length);
			/*
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
	        String[] positionsStrArray = {
	        		"\"\"",
	        		"\"0,16,1,6,22,7,59,58,26,46,15\"",
	        		"[a,b]",
	        		"[a]",
	        		"[]",
	        		"\"a\"",
	        };
	        for (String positionsStr : positionsStrArray) {
	            String[] positions = positionsStr.split("\\[|\\]|\\,");
	            System.out.println("(" + positionsStr + ") positions length " + positions.length);
	        }
	        System.out.println("1 = " + Boolean.parseBoolean("1"));
	        System.out.println("0 = " + Boolean.parseBoolean("0"));
	        System.out.println("true = " + Boolean.parseBoolean("true"));
	        System.out.println("false = " + Boolean.parseBoolean("false"));*/
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
