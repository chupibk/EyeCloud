/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * ++++
 *
 * @author samsalman
 */
public class DataClass {

    private static Configuration conf = null;

    static {
        conf = HBaseConfiguration.create();
    }

    public void get_DataHbase(long loopStarter, long loopruner, String userId, String tablename, String rowkey, ArrayList<String> ArrayRD_Column, ArrayList<String> ArrayRD_Value, ArrayList<String> ArrayRD_Time) throws IOException {
        HTable table = null;
        try {

            table = new HTable(conf, tablename);
            List<Get> Rowlist = new ArrayList<Get>();
            for (long a = loopStarter; a <= loopruner - 1; a++) {
                Rowlist.add(new Get(Bytes.toBytes(userId + ":" + rowkey + ":" + a)));
            }
            int hold_a = 0, hold_b = 0;
            Result[] result = table.get(Rowlist);
            for (Result r : result) {
                for (KeyValue kv : r.raw()) {
                    if (Bytes.toString(kv.getQualifier()).equals("GazePointXLeft")
                            || Bytes.toString(kv.getQualifier()).equals("GazePointYLeft")
                            || Bytes.toString(kv.getQualifier()).equals("GazePointXRight")
                            || Bytes.toString(kv.getQualifier()).equals("GazePointYRight")
                            || Bytes.toString(kv.getQualifier()).equals("StimuliName")
                            || Bytes.toString(kv.getQualifier()).equals("ValidityRight")
                            || Bytes.toString(kv.getQualifier()).equals("ValidityLeft")
                            || Bytes.toString(kv.getQualifier()).equals("DistanceLeft")) {
                        if (!ArrayRD_Column.contains(new String(kv.getQualifier()))) {
                            ArrayRD_Column.add(new String(kv.getQualifier()));
                            hold_a++;
                        } else if (hold_b == 0) {
                            ArrayRD_Value.add("/");
                            hold_b = 1;
                        } else {
                            if (hold_a == hold_b) {
                                ArrayRD_Value.add("/");
                                hold_b = 0;
                            }
                            hold_b++;
                        }
                        ArrayRD_Value.add(new String(kv.getValue()));
                    } else if (Bytes.toString(kv.getQualifier()).equals("Timestamp")) {
                        ArrayRD_Time.add(new String(kv.getValue()));
                    }


                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (table != null) {
                table.close();
            }
        }

    }

    public String get_MapFile(String rowkey) {
        String holdvalue = null;
        try {

            HTable table = new HTable(conf, "RawData");
            Get get = new Get(rowkey.getBytes());

            Result rs = table.get(get);
            for (KeyValue kv : rs.raw()) {
                holdvalue = new String(kv.getValue());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return holdvalue;

    }

    public void get_DataHbase_common(long loopStarter, long loopruner, String flag, String userId, String tablename, String rowkey, ArrayList<String> ArrayRD_Column, ArrayList<String> ArrayRD_Value) throws IOException {
        HTable table = null;
        try {
            if (flag.equals("ok")) // if its not a lable data
            {
            } else {
                loopStarter = 0;
                String NosRow = get_MapFile(rowkey);
                loopruner = Integer.valueOf(NosRow);
            }
            table = new HTable(conf, tablename);
            List<Get> Rowlist = new ArrayList<Get>();
            for (long a = loopStarter; a <= loopruner - 1; a++) {
                Rowlist.add(new Get(Bytes.toBytes(userId + ":" + rowkey + ":" + a)));
            }
            int hold_a = 0, hold_b = 0;
            Result[] result = table.get(Rowlist);
            for (Result r : result) {
                for (KeyValue kv : r.raw()) {

                    if (!ArrayRD_Column.contains(new String(kv.getQualifier()))) {
                        ArrayRD_Column.add(new String(kv.getQualifier()));
                        hold_a++;
                    } else if (hold_b == 0) {
                        ArrayRD_Value.add("/");
                        hold_b = 1;
                    } else {
                        if (hold_a == hold_b) {
                            ArrayRD_Value.add("/");
                            hold_b = 0;
                        }
                        hold_b++;
                    }
                    ArrayRD_Value.add(new String(kv.getValue()));
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (table != null) {
                table.close();
            }
        }

    }
}
