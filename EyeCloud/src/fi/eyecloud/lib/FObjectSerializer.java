package fi.eyecloud.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class FObjectSerializer extends Serializer<FObject>{

	@Override
	public FObject read(Kryo arg0, Input arg1, Class<FObject> arg2) {
		// TODO Auto-generated method stub
		FObject f = (FObject) arg0.readClassAndObject(arg1);
		return f;
	}

	@Override
	public void write(Kryo arg0, Output arg1, FObject arg2) {
		// TODO Auto-generated method stub
		arg0.writeClassAndObject(arg1, arg2);
	}

}
