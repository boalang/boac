package boa.datagen.paper;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import boa.types.Toplevel.Metadata;

public class ParagraphMetadata {

	private HashMap<String, List<CSVRecord>> pmcMap = new HashMap<String, List<CSVRecord>>();
	private HashMap<String, List<CSVRecord>> pubmedMap = new HashMap<String, List<CSVRecord>>();
	private HashMap<String, List<CSVRecord>> doiMap = new HashMap<String, List<CSVRecord>>();

	public ParagraphMetadata(String path) throws IOException {
		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
			String pmcId = record.get("PMCID");
			String pubmedId = record.get("Pubmed ID");
			String doi = record.get("DOI");

			if (isValid(pmcId)) {
				String key = "PMC" + pmcId;
				if (!pmcMap.containsKey(key))
					pmcMap.put(key, new ArrayList<CSVRecord>());
				pmcMap.get(key).add(record);
			}

			if (isValid(pubmedId)) {
				if (!pubmedMap.containsKey(pubmedId))
					pubmedMap.put(pubmedId, new ArrayList<CSVRecord>());
				pubmedMap.get(pubmedId).add(record);
			}

			if (isValid(doi)) {
				String key = "https://doi.org/" + doi;
				if (!doiMap.containsKey(key))
					doiMap.put(key, new ArrayList<CSVRecord>());
				doiMap.get(key).add(record);
			}
		}
	}

	private boolean isValid(String id) {
		return !id.equals("") && !id.equals("0");
	}

	public List<CSVRecord> getRecords(Metadata metadata) {
		if (pmcMap.containsKey(metadata.getPmcId())) {
			return pmcMap.get(metadata.getPmcId());
		}
		if (pubmedMap.containsKey(metadata.getPubmedId())) {
			return pubmedMap.get(metadata.getPubmedId());
		}
		if (doiMap.containsKey(metadata.getDoiUrl())) {
			return doiMap.get(metadata.getPmcId());
		}
		return null;
	}

}
