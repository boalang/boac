package boa.types.proto.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boa.types.BoaProtoList;
import boa.types.BoaProtoTuple;
import boa.types.BoaString;
import boa.types.BoaType;

public class AuthorProtoTuple extends BoaProtoTuple {
	private final static List<BoaType> members = new ArrayList<BoaType>();
	private final static Map<String, Integer> names = new HashMap<String, Integer>();

	static {
		int counter = 0;

		names.put("first", counter++);
		members.add(new BoaString());

		names.put("middle", counter++);
		members.add(new BoaString());

		names.put("last", counter++);
		members.add(new BoaString());

		names.put("suffix", counter++);
		members.add(new BoaString());

		names.put("affiliation", counter++);
		members.add(new BoaProtoList(new AffiliationProtoTuple()));

		names.put("email", counter++);
		members.add(new BoaString());
	}

	public AuthorProtoTuple() {
		super(members, names);
	}

	@Override
	public String toJavaType() {
		return "boa.types.Toplevel.Author";
	}

}
