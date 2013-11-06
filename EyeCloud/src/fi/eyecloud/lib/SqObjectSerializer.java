package fi.eyecloud.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class SqObjectSerializer extends Serializer<SqObject>{

	@Override
	public SqObject read(Kryo arg0, Input arg1, Class<SqObject> arg2) {
		// TODO Auto-generated method stub
		SqObject sqo = arg0.readObject(arg1, arg2);
		return sqo;
	}

	@Override
	public void write(Kryo arg0, Output arg1, SqObject arg2) {
		// TODO Auto-generated method stub
		arg0.writeObject(arg1, arg2);
	}

}
