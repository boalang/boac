package boa.datagen.paper;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class PaperMetadata {
	
	public static void main(String[] args) throws IOException, ParseException {
		String path = "/Users/hyj/git/BoaData/DataGenPaperJson/covid-19/dataset/metadata.csv";
		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		
//		String[] targets = new String
		
		List<CSVRecord> res = searchAllInCol(records, "doi", "10.1016/S0140-6736(20)30251-8");
		
		for (CSVRecord record : res)
			System.out.println(record.get("has_full_text") + " " + record.get("sha"));
		
//		HashMap<String, String> map = new HashMap<>();
//		System.out.println(map.get(null));
		
//		String input = "1998 Jan";
//		// Format of the date defined in the input String
//		DateFormat df = new SimpleDateFormat("yyyy MMM");
//		DateFormat out = new SimpleDateFormat("MM/dd/yyyy");
//		Date date = null;
//		long l = -1;
//		date = df.parse(input);
//		l = date.getTime() * 1000;
//		System.out.println(out.format(l / 1000));
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
