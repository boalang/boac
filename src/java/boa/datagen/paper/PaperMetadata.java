package boa.datagen.paper;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class PaperMetadata {
	
	public static void main(String[] args) throws IOException, ParseException {
		String path = "/Users/yijiahuang/git/BoaData/PaperInputJson/covid-19/dataset/metadata.csv";
		Reader in = new FileReader(path);
		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
		for (CSVRecord record : records) {
		    String source = record.get("source_x");
		    System.out.println(record.get("sha").equals(""));
		    break;
		}
		HashMap<String, String> map = new HashMap<>();
		System.out.println(map.get(null));
		
		String input = "1998 Jan";
		// Format of the date defined in the input String
		DateFormat df = new SimpleDateFormat("yyyy MMM");
		DateFormat out = new SimpleDateFormat("MM/dd/yyyy");
		Date date = null;
		long l = -1;
		date = df.parse(input);
		l = date.getTime() * 1000;
		System.out.println(out.format(l / 1000));
	}
	
	public PaperMetadata(CSVRecord record) {
		
	}

}
