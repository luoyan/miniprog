package com.xiaonei.rose.gettingStarted.controllers;

import com.xiaonei.rose.gettingStarted.dao.TestDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
@Service
public class TestService {
    @Autowired
    private TestDAO testDAO;

    public Test getTest() {
        return testDAO.getTest();
    }
}
