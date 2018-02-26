package etalier.spark.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by Administrator on 2018/1/17 0017.
 */
//工具类可以使用单例模式
public class HBaseUtils {

    HBaseAdmin admin=null;
    Configuration configuration=null;

    private HBaseUtils(){
        //Hbase-site.xml
        configuration=new Configuration();
        configuration.set("hbase.zookeeper,quorum","localhost:2181");
        configuration.set("hbase.rootdir","hdfs://localhost:8020/hbase");

        try {
            admin=new HBaseAdmin(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static HBaseUtils instance=null;

    public static synchronized HBaseUtils getInstance(){
        if(instance==null){
            instance=new HBaseUtils();
        }
        return instance;
    }

    public HTable getTable(String tableName){
        HTable table=null;
        try {
            table=new HTable(configuration,tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return table;
    }

    public void put(String tableName,String rowKey,String cf,String column,String value){
        HTable table=getTable(tableName);
        Put put=new Put(Bytes.toBytes(rowKey));
        put.add(Bytes.toBytes(cf),Bytes.toBytes(column),Bytes.toBytes(value));
        try {
            table.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
