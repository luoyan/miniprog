package com.luoyan.zookeepertest;
import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.log4j.*;
import java.util.*;
//import ZooKeeper
public class zk_client {
    private ZooKeeper zk;
    private static Logger logger;
    static 
    {
        logger = Logger.getLogger(zk_client.class.getName()); 
        PropertyConfigurator.configure("./log4j.properties");
    }
    public zk_client() {
        try {
            this.zk = new ZooKeeper("zookeeper1:2181", 500000,new Watcher() {
                   // 监控所有被触发的事件
                     public void process(WatchedEvent event) {
                   //dosomething
                   }
                  });
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void test() {
        try {
            Logger logger = Logger.getLogger(zk_client.class.getName()); 
            PropertyConfigurator.configure("./log4j.properties");
            //创建一个Zookeeper实例，第一个参数为目标服务器地址和端口，第二个参数为Session超时时间，第三个为节点变化时的回调方法
            /*ZooKeeper zk = new ZooKeeper("zookeeper1:2181", 500000,new Watcher() {
                   // 监控所有被触发的事件
                     public void process(WatchedEvent event) {
                   //dosomething
                   }
                  });*/
            //创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
            try{
                //zk.delete("/root", -1);
                this.zk.create("/root", "mydata".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            logger.info("create /root");
            Thread.sleep(10000);
            //在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
            this.zk.create("/root/childone","childone".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            logger.info("create /root/childone");
            Thread.sleep(10000);
            //取得/root节点下的子节点名称,返回List<String>
            List<String> children = this.zk.getChildren("/root",true);
            for(Iterator<String> it = children.iterator();it.hasNext();) {
                String child = it.next();
                logger.info("child " + child);
            }

            //取得/root/childone节点下的数据,返回byte[]
            byte[] data_child = this.zk.getData("/root/childone", true, null);
            String data = new String(data_child);
            logger.info("getData /root/childone [" + data + "]");
            
            //修改节点/root/childone下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
            this.zk.setData("/root/childone","childonemodify".getBytes(), -1);
            data_child = this.zk.getData("/root/childone", true, null);
            data = new String(data_child);
            logger.info("getData /root/childone [" + data + "]");

            //删除/root/childone这个节点，第二个参数为版本，－1的话直接删除，无视版本
            this.zk.delete("/root/childone", -1);
            logger.info("delete /root/childone");
                  
            logger.info("create /root/nulldata");
            this.zk.create("/root/nulldata", null, Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            byte[] nulldata_child = this.zk.getData("/root/nulldata", true, null);
            if (nulldata_child == null) {
            	logger.info("/root/nulldata null");
            }
            else {
            	logger.info("/root/nulldata data");
            }
            Thread.sleep(10000);
            this.zk.delete("/root/nulldata", -1);
            logger.info("delete /root/nulldata");
            //关闭session
            this.zk.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void create(String filename) {
        try {
            this.zk.create(filename, "mydata".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void usage() {
        logger.info(" test");
        logger.info(" create filename");
    }
    public static void main(String [] args) {
        zk_client zkc = new zk_client();
        for(int i = 0 ; i < args.length;i++) {
            logger.info(i + " " + args[i]);
        }
        logger.info("length " + args.length);
        if (args.length > 2) {
            usage();
            System.exit(-1);
        }
        if (args.length == 1 && args[0].equals("test"))
            zkc.test();
        else if (args.length == 2 && args[0].equals("create")) {
            String filename = args[1];
            zkc.create(filename);
        } else {
            usage();
            System.exit(-1);
        }
    }
}
