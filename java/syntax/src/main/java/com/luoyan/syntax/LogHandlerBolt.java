package com.luoyan.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHandlerBolt {
	private static final Logger LOGGER = LoggerFactory.getLogger("LogHandlerBolt");
	private int taskId = 100;
    public ILogHandler getLogHandler(String className, int taskId) {
        try {
            Class logHandlerClass = Class.forName(className);
            //ILogHandler logHandler = (ILogHandler) logHandlerClass.newInstance();
	        Class[] parameterTypes={int.class};
	        java.lang.reflect.Constructor constructor=logHandlerClass.getConstructor(parameterTypes);
	        Object[] parameters={taskId};
	        ILogHandler logHandler = (ILogHandler)constructor.newInstance(parameters);
            return logHandler;
        } catch (Exception e) {
            System.out.println("error : " + e.toString());
            LOGGER.error("{}", e);
            return null;
        }
    }
}
