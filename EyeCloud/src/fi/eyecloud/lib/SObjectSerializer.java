package fi.eyecloud.lib;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class SObjectSerializer extends Serializer<SObject>{

	/**
	 * Used for sending
	 * 
	 */
	@Override
	public SObject read(Kryo arg0, Input arg1, Class<SObject> arg2) {
		// TODO Auto-generated method stub
		SObject s = new SObject(arg1.readInt(), arg1.readInt(), arg1.readFloat(), arg1.readFloat(), arg1.readFloat());
		return s;
	}

	@Override
	public void write(Kryo arg0, Output arg1, SObject arg2) {
		// TODO Auto-generated method stub
		arg1.writeInt(arg2.duration);
		arg1.writeInt(arg2.rawNumber);
		arg1.writeFloat(arg2.distance);
		arg1.writeFloat(arg2.velocity);
		arg1.writeFloat(arg2.acceleration);
	}


}
