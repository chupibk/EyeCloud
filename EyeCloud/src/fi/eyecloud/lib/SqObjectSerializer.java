package fi.eyecloud.lib;

import java.util.List;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class SqObjectSerializer extends Serializer<SqObject>{

	@Override
	public SqObject read(Kryo arg0, Input arg1, Class<SqObject> arg2) {
		// TODO Auto-generated method stub
		SqObject sqo = new SqObject();
		sqo.intention = arg1.readInt();
		sqo.fObjects = (List<FObject>) arg0.readClassAndObject(arg1);
		sqo.sObjects = (List<SObject>) arg0.readClassAndObject(arg1);
		return sqo;
	}

	@Override
	public void write(Kryo arg0, Output arg1, SqObject arg2) {
		// TODO Auto-generated method stub
		arg1.writeInt(arg2.intention);
		arg0.writeClassAndObject(arg1, arg2.fObjects);
		arg0.writeClassAndObject(arg1, arg2.sObjects);
	}

}
