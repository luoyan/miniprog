package com.luoyan.syntax;

import org.slf4j.Logger;    
import org.slf4j.LoggerFactory; 
/**
 * Hello world!
 *
 */
public class App 
{
    private Logger logger = LoggerFactory.getLogger(App.class);
    public void testLog(){       
        logger.info("this is a test log");       
    } 
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        App a = new App();
        a.testLog();
    }
}
