package boa.types.proto.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boa.types.BoaProtoTuple;
import boa.types.BoaString;
import boa.types.BoaType;

public class AffiliationProtoTuple extends BoaProtoTuple {
	private final static List<BoaType> members = new ArrayList<BoaType>();
	private final static Map<String, Integer> names = new HashMap<String, Integer>();

	static {
		int counter = 0;

		names.put("laboratory", counter++);
		members.add(new BoaString());

		names.put("institution", counter++);
		members.add(new BoaString());

		names.put("location", counter++);
		members.add(new LocationProtoTuple());

	}

	public AffiliationProtoTuple() {
		super(members, names);
	}

	@Override
	public String toJavaType() {
		return "boa.types.Toplevel.Affiliation";
	}

}
