package boa.types.proto.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boa.types.BoaProtoList;
import boa.types.BoaProtoTuple;
import boa.types.BoaString;
import boa.types.BoaType;

public class SectionProtoTuple extends BoaProtoTuple {
	private final static List<BoaType> members = new ArrayList<BoaType>();
	private final static Map<String, Integer> names = new HashMap<String, Integer>();

	static {
		int counter = 0;

		names.put("title", counter++);
		members.add(new BoaString());

		names.put("body", counter++);
		members.add(new BoaProtoList(new ParagraphProtoTuple()));
	}

	public SectionProtoTuple() {
		super(members, names);
	}

	@Override
	public String toJavaType() {
		return "boa.types.Toplevel.Section";
	}

}
