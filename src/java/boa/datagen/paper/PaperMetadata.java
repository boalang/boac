package boa.datagen.paper;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class PaperMetadata {
	
	public static void main(String[] args) throws IOException, ParseException {
//		String path = "/Users/hyj/git/BoaData/DataGenPaperJson/covid-19/dataset/sections.csv";
//		Reader in = new FileReader(path);
//		Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(in);
//		
//		for (CSVRecord record : records)
//			System.out.println(record.get(1));
		
		
		
		String path = "/Users/hyj/git/BoaData/DataGenPaperJson/covid-19/dataset/metadata.csv";
		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		
//		String[] targets = new String
		
//		List<CSVRecord> res = searchAllInCol(records, "doi", "10.1016/S0140-6736(20)30251-8");
		
		for (CSVRecord record : records)
			if (!record.get("pubmed_id").equals(""))
				System.out.println(record.get("pubmed_id"));
		

	}
	
	public static List<CSVRecord> searchAllInCol(Iterable<CSVRecord> records, String colName, String target) {
		List<CSVRecord> res = new ArrayList<CSVRecord>();
		for (CSVRecord record : records) {
		   if (record.get(colName).equals(target))
			   res.add(record);
		}
		return res;
	}

}
