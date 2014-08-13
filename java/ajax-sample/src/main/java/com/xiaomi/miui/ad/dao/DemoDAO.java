package com.xiaomi.miui.ad.dao;

import com.xiaomi.miui.ad.model.Demo;
import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;

import java.util.List;

/**
 * Created by alexfang on 2014/8/7.
 */
@DAO
public interface DemoDAO {
    String TABLE_NAME = "demo";
    String COLUMN = "id,ip,keyword";

    @SQL("select " + COLUMN + " from " + TABLE_NAME)
    public List<Demo> getAllDemos();

}
