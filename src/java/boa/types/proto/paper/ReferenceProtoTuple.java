package boa.types.proto.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boa.types.BoaInt;
import boa.types.BoaProtoList;
import boa.types.BoaProtoTuple;
import boa.types.BoaString;
import boa.types.BoaType;
import boa.types.proto.enums.ReferenceTypeProtoMap;

public class ReferenceProtoTuple extends BoaProtoTuple {
	private final static List<BoaType> members = new ArrayList<BoaType>();
	private final static Map<String, Integer> names = new HashMap<String, Integer>();

	static {
		int counter = 0;

		names.put("ref_id", counter++);
		members.add(new BoaString());

		names.put("title", counter++);
		members.add(new BoaString());

		names.put("authors", counter++);
		members.add(new BoaProtoList(new AuthorProtoTuple()));

		names.put("year", counter++);
		members.add(new BoaString());

		names.put("venue", counter++);
		members.add(new BoaString());

		names.put("volume", counter++);
		members.add(new BoaString());

		names.put("issn", counter++);
		members.add(new BoaString());

		names.put("pages", counter++);
		members.add(new BoaString());

		names.put("text", counter++);
		members.add(new BoaString());

		names.put("type", counter++);
		members.add(new ReferenceTypeProtoMap());
	}

	public ReferenceProtoTuple() {
		super(members, names);
	}

	@Override
	public String toJavaType() {
		return "boa.types.Toplevel.Reference";
	}

}
