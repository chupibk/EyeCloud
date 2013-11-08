package fi.eyecloud.lib;

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
		int fSize = arg1.readInt();
		int sSize = arg1.readInt();
		
		for (int i=0; i < fSize; i++){
			sqo.fObjects.add((FObject)arg0.readClassAndObject(arg1));
		}
		
		for (int i=0; i < sSize; i++){
			sqo.sObjects.add((SObject)arg0.readClassAndObject(arg1));
		}		
		
		return sqo;
	}

	@Override
	public void write(Kryo arg0, Output arg1, SqObject arg2) {
		// TODO Auto-generated method stub
		arg1.writeInt(arg2.intention);
		arg1.writeInt(arg2.fObjects.size());
		arg1.writeInt(arg2.sObjects.size());		
		for (int i=0; i < arg2.fObjects.size(); i++){
			arg0.writeClassAndObject(arg1, arg2.fObjects.get(i));
		}
		
		for (int i=0; i < arg2.sObjects.size(); i++){
			arg0.writeClassAndObject(arg1, arg2.sObjects.get(i));
		}		
	}

}
