package fi.eyecloud.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class SObjectSerializer extends Serializer<SObject>{

	@Override
	public SObject read(Kryo arg0, Input arg1, Class<SObject> arg2) {
		// TODO Auto-generated method stub
		SObject s = (SObject) arg0.readClassAndObject(arg1);
		return s;
	}

	@Override
	public void write(Kryo arg0, Output arg1, SObject arg2) {
		// TODO Auto-generated method stub
		arg0.writeClassAndObject(arg1, arg2);
	}


}
