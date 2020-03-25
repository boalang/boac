package boa.types.proto.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boa.types.BoaProtoTuple;
import boa.types.BoaString;
import boa.types.BoaType;

public class LocationProtoTuple extends BoaProtoTuple {
	private final static List<BoaType> members = new ArrayList<BoaType>();
	private final static Map<String, Integer> names = new HashMap<String, Integer>();

	static {
		int counter = 0;

		names.put("addrLine", counter++);
		members.add(new BoaString());

		names.put("postCode", counter++);
		members.add(new BoaString());

		names.put("settlement", counter++);
		members.add(new BoaString());

		names.put("country", counter++);
		members.add(new BoaString());
		
		names.put("region", counter++);
		members.add(new BoaString());
	}

	public LocationProtoTuple() {
		super(members, names);
	}

	@Override
	public String toJavaType() {
		return "boa.types.Toplevel.Location";
	}

}
