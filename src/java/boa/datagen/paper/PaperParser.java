package boa.datagen.paper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import boa.datagen.util.FileIO;
import boa.types.Toplevel.Affiliation;
import boa.types.Toplevel.Author;
import boa.types.Toplevel.Citation;
import boa.types.Toplevel.Location;
import boa.types.Toplevel.Metadata;
import boa.types.Toplevel.Paper;
import boa.types.Toplevel.Paragraph;
import boa.types.Toplevel.Reference;
import boa.types.Toplevel.Section;
import boa.types.Toplevel.Paragraph.ParagraphKind;
import boa.types.Toplevel.Reference.ReferenceType;

public class PaperParser {

	public static void main(String[] args) {
//		String base = "/Users/yijiahuang/git/BoaData/PaperInputJson/covid-19/dataset/biorxiv_medrxiv/biorxiv_medrxiv/";
//		String path = base + "0b282573f5c63c943021c10ca39a1ed21acfb429.json";
//		String content = FileIO.readFileContents(new File(path));
//		Gson parser = new Gson();
//		// parse json file
//		JsonObject jo = null;
//		try {
//			jo = parser.fromJson(content, JsonElement.class).getAsJsonObject();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		Paper paper = getPaper(jo);
//		System.out.println(paper);
	}

	public static Paper getPaper(JsonObject jo) {
		Paper.Builder pb = Paper.newBuilder();
		if (jo.has("paper_id"))
			pb.setId(getString(jo, "paper_id"));
		if (jo.has("metadata"))
			pb.setMetadata(getMetadata(jo.get("metadata").getAsJsonObject()));
		if (jo.has("abstract"))
			for (JsonElement je : jo.get("abstract").getAsJsonArray())
				pb.addAbstract(getParagraph(je.getAsJsonObject()));
		if (jo.has("abstract"))
			for (JsonElement je : jo.get("abstract").getAsJsonArray())
				pb.addAbstract(getParagraph(je.getAsJsonObject()));
		if (jo.has("body_text"))
			pb.addAllBodyText(getSections(jo.get("body_text").getAsJsonArray()));
		if (jo.has("bib_entries"))
			pb.addAllBibEntries(getReferences(jo.get("bib_entries").getAsJsonObject()));
		if (jo.has("ref_entries"))
			pb.addAllRefEntries(getReferences(jo.get("ref_entries").getAsJsonObject()));
		return pb.build();
	}

	private static Metadata getMetadata(JsonObject jo) {
		Metadata.Builder mb = Metadata.newBuilder();
		if (jo.has("title"))
			mb.setTitle(getString(jo, "title"));
		if (jo.has("authors"))
			for (JsonElement je : jo.get("authors").getAsJsonArray())
				mb.addAuthors(getAuthor(je.getAsJsonObject()));
		return mb.build();
	}

	private static Author getAuthor(JsonObject jo) {
		Author.Builder ab = Author.newBuilder();
		if (jo.has("first"))
			ab.setFirst(getString(jo, "first"));
		if (jo.has("middle"))
			for (JsonElement middle : jo.get("middle").getAsJsonArray())
				ab.addMiddle(middle.getAsString());
		if (jo.has("last"))
			ab.setLast(getString(jo, "last"));
		if (jo.has("suffix"))
			ab.setSuffix(getString(jo, "suffix"));
		if (jo.has("affiliation"))
			ab.setAffiliation(getAffiliation(jo.get("affiliation").getAsJsonObject()));
		if (jo.has("email"))
			ab.setEmail(getString(jo, "email"));
		return ab.build();
	}

	private static Affiliation getAffiliation(JsonObject jo) {
		Affiliation.Builder ab = Affiliation.newBuilder();
		if (jo.has("laboratory"))
			ab.setLaboratory(getString(jo, "laboratory"));
		if (jo.has("institution"))
			ab.setInstitution(getString(jo, "institution"));
		if (jo.has("location"))
			ab.setLocation(getLocation(jo.get("location").getAsJsonObject()));
		return ab.build();
	}

	private static Location getLocation(JsonObject jo) {
		Location.Builder lb = Location.newBuilder();
		if (jo.has("addrLine"))
			lb.setAddrLine(getString(jo, "addrLine"));
		if (jo.has("postCode"))
			lb.setPostCode(getString(jo, "postCode"));
		if (jo.has("settlement"))
			lb.setSettlement(getString(jo, "settlement"));
		if (jo.has("country"))
			lb.setCountry(getString(jo, "country"));
		if (jo.has("region"))
			lb.setRegion(getString(jo, "region"));
		return lb.build();
	}

	private static List<Section> getSections(JsonArray ja) {
		List<Section> secs = new ArrayList<Section>();
		Section.Builder sb = Section.newBuilder();
		for (JsonElement je : ja) {
			JsonObject cur = je.getAsJsonObject();
			if (cur.has("section")) {
				String section = getString(cur, "section");
				if (sb.hasTitle()) {
					if (!section.equals(sb.getTitle())) {
						secs.add(sb.build());
						sb = Section.newBuilder().setTitle(section);
					}
				} else {
					sb.setTitle(section);
				}
				sb.addBody(getParagraph(cur));
			} else {
				System.err.println("no section");
			}
		}
		secs.add(sb.build());
		return secs;
	}

	private static Paragraph getParagraph(JsonObject jo) {
		Paragraph.Builder pb = Paragraph.newBuilder();
		if (jo.has("text"))
			pb.setText(getString(jo, "text"));
		if (jo.has("cite_spans"))
			for (JsonElement je : jo.get("cite_spans").getAsJsonArray())
				pb.addCiteSpans(getCitation(je.getAsJsonObject()));
		if (jo.has("ref_spans"))
			for (JsonElement je : jo.get("ref_spans").getAsJsonArray())
				pb.addRefSpans(getCitation(je.getAsJsonObject()));
		pb.setKind(ParagraphKind.OTHER); // TODO update paragraph kind
		return pb.build();
	}

	private static Citation getCitation(JsonObject jo) {
		Citation.Builder cb = Citation.newBuilder();
		if (jo.has("text"))
			cb.setText(getString(jo, "text"));
		if (jo.has("ref_id"))
			cb.setRefId(getString(jo, "ref_id"));
		return cb.build();
	}

	private static List<Reference> getReferences(JsonObject jsonObject) {
		List<Reference> refs = new ArrayList<Reference>();
		for (Entry<String, JsonElement> e : jsonObject.entrySet()) {
			Reference.Builder rb = Reference.newBuilder();
			String id = e.getKey();
			JsonObject jo = e.getValue().getAsJsonObject();
			rb.setRefId(id);
			if (jo.has("title"))
				rb.setTitle(getString(jo, "title"));
			if (jo.has("authors"))
				for (JsonElement je : jo.get("authors").getAsJsonArray())
					rb.addAuthors(getAuthor(je.getAsJsonObject()));
			if (jo.has("year"))
				rb.setYear(getString(jo, "year"));
			if (jo.has("venue"))
				rb.setVenue(getString(jo, "venue"));
			if (jo.has("volume"))
				rb.setVolume(getString(jo, "volume"));
			if (jo.has("issn"))
				rb.setIssn(getString(jo, "issn"));
			if (jo.has("pages"))
				rb.setPages(getString(jo, "pages"));
			if (jo.has("text"))
				rb.setText(getString(jo, "text"));
			if (jo.has("type"))
				if (getString(jo, "type").equals("table"))
					rb.setType(ReferenceType.TABLE);
				else
					rb.setType(ReferenceType.FIGURE);
			else
				rb.setType(ReferenceType.BIB);
			refs.add(rb.build());
		}
		return refs;
	}

	private static String getString(JsonObject jo, String member) {
		JsonElement je = jo.get(member);
		if (je instanceof JsonNull)
			return je.toString();
		return je.getAsString();
	}

}
