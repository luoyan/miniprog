package com.xiaonei.rose.gettingStarted.controllers;
import net.paoding.rose.web.ControllerErrorHandler;
import net.paoding.rose.web.Invocation;
public class ErrorHandler implements ControllerErrorHandler {
    public Object onError(Invocation inv, Throwable ex) throws Throwable {

        // TODO logger.error("handle err:", ex);

        return "@error";
    }
}
