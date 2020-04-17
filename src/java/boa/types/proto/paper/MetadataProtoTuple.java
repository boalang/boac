package boa.types.proto.paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boa.types.BoaProtoList;
import boa.types.BoaProtoTuple;
import boa.types.BoaString;
import boa.types.BoaTime;
import boa.types.BoaType;

public class MetadataProtoTuple extends BoaProtoTuple {
	private final static List<BoaType> members = new ArrayList<BoaType>();
	private final static Map<String, Integer> names = new HashMap<String, Integer>();

	static {
		int counter = 0;

		names.put("title", counter++);
		members.add(new BoaString());

		names.put("doi_url", counter++);
		members.add(new BoaString());

		names.put("source", counter++);
		members.add(new BoaString());

		names.put("pubmed_id", counter++);
		members.add(new BoaString());

		names.put("pmc_id", counter++);
		members.add(new BoaString());

		names.put("publish_time", counter++);
		members.add(new BoaTime());

		names.put("journal", counter++);
		members.add(new BoaString());

		names.put("authors", counter++);
		members.add(new BoaProtoList(new AuthorProtoTuple()));

		names.put("license_type", counter++);
		members.add(new BoaString());
	}

	public MetadataProtoTuple() {
		super(members, names);
	}

	@Override
	public String toJavaType() {
		return "boa.types.Toplevel.Metadata";
	}

}
