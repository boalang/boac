package boa.datagen.paper;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import boa.datagen.util.FileIO;
import boa.types.Toplevel.Author;
import boa.types.Toplevel.Metadata;
import boa.types.Toplevel.Paper;
import boa.types.proto.paper.MetadataProtoTuple;

public class temp {

	public static void main(String[] args) {
		String path = "";
		String content = FileIO.readFileContents(new File(path));
		Gson parser = new Gson();
		// parse json file
		JsonObject jo = null;
		try {
			jo = parser.fromJson(content, JsonElement.class).getAsJsonObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		Paper.Builder pb = Paper.newBuilder();
		if (jo.has("paper_id"))
			pb.setId(jo.get("paper_id").getAsString());
		
		if (jo.has("metadata"))
			pb.setMetadata(getMetadata(jo.get("metadata").getAsJsonObject()));
	}

	private static Metadata getMetadata(JsonObject jo) {
		Metadata.Builder mb = Metadata.newBuilder();
		if (jo.has("title"))
			mb.setTitle(jo.get("title").getAsString());
		if (jo.has("authors"))
			for (JsonElement je : jo.get("authors").getAsJsonArray()) {
				Author.Builder ab = Author.newBuilder();
				JsonObject author = je.getAsJsonObject();
				if (author.has("first"))
					ab.setFirst(author.get("first").getAsString());
				if (author.has("middle"))
					ab.setFirst(author.get("middle").getAsString());
				if (author.has("last"))
					ab.setFirst(author.get("last").getAsString());
				if (author.has("suffix"))
					ab.setFirst(author.get("suffix").getAsString());
				if (author.has("affiliation"))
					ab.setFirst(author.get("affiliation").getAsString());
				if (author.has("email"))
					ab.setFirst(author.get("email").getAsString());
			}
		return mb.build();
	}
	
}
