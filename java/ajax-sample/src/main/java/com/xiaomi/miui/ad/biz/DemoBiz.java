package com.xiaomi.miui.ad.biz;

import com.xiaomi.miui.ad.dao.DemoDAO;
import com.xiaomi.miui.ad.model.Demo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by alexfang on 2014/8/7.
 */
@Service
public class DemoBiz {
    @Autowired
    private DemoDAO demoDAO;

    public List<Demo> getAllDemo() {
        return demoDAO.getAllDemos();
    }
}
