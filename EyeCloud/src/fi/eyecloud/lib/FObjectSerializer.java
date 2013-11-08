package fi.eyecloud.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class FObjectSerializer extends Serializer<FObject>{

	@Override
	public FObject read(Kryo arg0, Input arg1, Class<FObject> arg2) {
		// TODO Auto-generated method stub
		FObject f = new FObject(arg1.readInt(), arg1.readInt(), arg1.readInt(), arg1.readInt(), arg1.readInt());
		return f;
	}

	@Override
	public void write(Kryo arg0, Output arg1, FObject arg2) {
		// TODO Auto-generated method stub
		arg1.writeInt(arg2.gazeX);
		arg1.writeInt(arg2.gazeY);
		arg1.writeInt(arg2.startTime);
		arg1.writeInt(arg2.duration);
		arg1.writeInt(arg2.keyPress);
	}

}
