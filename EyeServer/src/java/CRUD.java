import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

public class CRUD {
	HTable table;

	public CRUD() throws IOException {
		Configuration conf = HBaseConfiguration.create();
		table = new HTable(conf, "test");
	}

	public void put() throws IOException {
		Put put = new Put(Bytes.toBytes("row3"));
		put.add(Bytes.toBytes("data"), Bytes.toBytes("1"),
				Bytes.toBytes("chungdao"));
		table.put(put);
	}

	public String get() throws IOException {
		Get get = new Get(Bytes.toBytes("row3"));
		get.addColumn(Bytes.toBytes("data"), Bytes.toBytes("1"));
		Result result = table.get(get);
		byte[] val = result.getValue(Bytes.toBytes("data"),
				Bytes.toBytes("1"));
		return Bytes.toString(val);
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		CRUD test = new CRUD();
		System.out.println(test.get());
	}
}
