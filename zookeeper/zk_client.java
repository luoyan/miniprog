import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.log4j.*;
import java.util.*;
//import ZooKeeper
public class zk_client {
    public static void main(String [] args) {
        try {
            Logger logger = Logger.getLogger(zk_client.class.getName()); 
            PropertyConfigurator.configure("./log4j.properties");
            //创建一个Zookeeper实例，第一个参数为目标服务器地址和端口，第二个参数为Session超时时间，第三个为节点变化时的回调方法
            ZooKeeper zk = new ZooKeeper("zookeeper1:2181", 500000,new Watcher() {
                   // 监控所有被触发的事件
                     public void process(WatchedEvent event) {
                   //dosomething
                   }
                  });
            //创建一个节点root，数据是mydata,不进行ACL权限控制，节点为永久性的(即客户端shutdown了也不会消失)
            try{
                //zk.delete("/root", -1);
                zk.create("/root", "mydata".getBytes(),Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
            }
            logger.info("create /root");
            Thread.sleep(10000);
            //在root下面创建一个childone znode,数据为childone,不进行ACL权限控制，节点为永久性的
            zk.create("/root/childone","childone".getBytes(), Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT);
            logger.info("create /root/childone");
            Thread.sleep(10000);
            //取得/root节点下的子节点名称,返回List<String>
            List<String> children = zk.getChildren("/root",true);
            for(Iterator<String> it = children.iterator();it.hasNext();) {
                String child = it.next();
                logger.info("child " + child);
            }

            //取得/root/childone节点下的数据,返回byte[]
            byte[] data_child = zk.getData("/root/childone", true, null);
            logger.info("getData /root/childone [" + data_child + "]");
            
            //修改节点/root/childone下的数据，第三个参数为版本，如果是-1，那会无视被修改的数据版本，直接改掉
            zk.setData("/root/childone","childonemodify".getBytes(), -1);
            data_child = zk.getData("/root/childone", true, null);
            logger.info("getData /root/childone [" + data_child + "]");

            //删除/root/childone这个节点，第二个参数为版本，－1的话直接删除，无视版本
            zk.delete("/root/childone", -1);
            logger.info("delete /root/childone");
                  
            //关闭session
            zk.close();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
