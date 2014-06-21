package com.xiaonei.rose.gettingStarted.dao;
import com.xiaonei.rose.gettingStarted.controllers.Test;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.ReturnGeneratedKeys;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import net.paoding.rose.jade.annotation.ShardBy;

import java.util.List;
@DAO
public interface TestDAO {
    @SQL("select id,msg from test01 limit 1")
    public Test getTest();
}
