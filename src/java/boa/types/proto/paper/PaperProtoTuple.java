package boa.types.proto.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boa.types.BoaProtoList;
import boa.types.BoaProtoTuple;
import boa.types.BoaString;
import boa.types.BoaType;

public class PaperProtoTuple extends BoaProtoTuple {
	private final static List<BoaType> members = new ArrayList<BoaType>();
	private final static Map<String, Integer> names = new HashMap<String, Integer>();

	static {
		int counter = 0;

		names.put("id", counter++);
		members.add(new BoaString());

		names.put("metadata", counter++);
		members.add(new MetadataProtoTuple());

		names.put("abstract", counter++);
		members.add(new BoaProtoList(new ParagraphProtoTuple()));

		names.put("body_text", counter++);
		members.add(new BoaProtoList(new SectionProtoTuple()));

		names.put("bib_entries", counter++);
		members.add(new BoaProtoList(new ReferenceProtoTuple()));

		names.put("ref_entries", counter++);
		members.add(new BoaProtoList(new ReferenceProtoTuple()));
	}

	public PaperProtoTuple() {
		super(members, names);
	}

	@Override
	public String toJavaType() {
		return "boa.types.Toplevel.Paper";
	}

}
