package boa.functions.nlp;

import static boa.functions.nlp.BoaNLPNoiseRemoval.*;
import static boa.functions.nlp.BoaNLPStopWords.*;
import static boa.functions.nlp.BoaNLPTokenizer.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import boa.functions.FunctionSpec;

public class BoaNLPIntrinsics {

	public static void main(String[] args) throws IOException {
//		System.out.println(Arrays.asList(preprocess1(text, stopWordsSet)));
		System.out.println(Arrays.asList(preprocess2(text, stopWordsSet)));
	}
	
	/* preprocess for general text */
	public static String[] preprocess1(final String text, final HashSet<String> stopwords) {
		String s = text.trim().toLowerCase()                      // lowercasing
				.replaceAll("\\p{Punct}", " ");                   // remove punctuations
		String[] tokens = tokenize(s);                            // tokenize
		List<String> words = Arrays.asList(tokens).stream()
				.filter(token -> !stopwords.contains(token))      // remove stopwords
				.filter(token -> !isNumber(token))                // remove numbers
				.filter(token -> !isDigits(token))                // remove digits
				.filter(token -> isASCII(token))                  // remove non-ASCII
				.collect(Collectors.toList());
		return words.toArray(new String[words.size()]);
	}
	
	/* preprocess for research paper */
	public static String[] preprocess2(final String text, final HashSet<String> stopwords) {
		// 1st tokenize text
		String[] tokens = text.trim().toLowerCase()               // lowercasing
				.replaceAll("[\\p{Punct}&&[^_-]]+", "")           // remove punctuations except "_" and "-"
				.split("\\s+");                                   // remove whitespace                         
		
		// 2nd filter tokens
		List<String> words = Arrays.asList(tokens).stream()
				.filter(token -> !stopwords.contains(token))      // remove stopwords
				.filter(token -> !isNumber(token))                // remove numbers
				.filter(token -> !isDigits(token))                // remove digits
				.filter(token -> isASCII(token))                  // remove non-ASCII
				.collect(Collectors.toList());
		return words.toArray(new String[words.size()]);
	}

	public static String text = "Why painful the sixteen how minuter looking nor. "
			+ "Subject but why ten earnest husband imagine sixteen brandon. "
			+ "Are unp.leasing. occasional celebrated motionless unaffected conviction out. "
			+ "Evil make to no five they. Stuff at avoid&^*^&* of sense small fully it whose an. "
			+ "Ten scarcely distance moreover handsome age although. As wh好en have find fine "
			+ "or said no mile. He in dispatched@$#@$#@ in imprudence dissimilar be possession "
			+ "unreserved insensible. She evil face fine calm ha,,,,,,ve now. Separate screened"
			+ " he outweigh of distance landlord. bat-SL-CoVZXC21";

	private static HashSet<String> testIds = new HashSet<String>(
			Arrays.asList("a08c87a957ae94e9d23bb572191e2ded2c16414e", "e89563eb94ead6b73b9bc6efde85843cbfb1653b",
					"31165346222e30211f9895693dee83cd824e2232", "b1052d3e68d607d53c9ba567094cac528de60fd1",
					"5722e1b167fe879fc2901ba8ea2fa69fec89a6f7", "62cbd36e69d6db0897bd514bd120772bc8841c1d",
					"d7823cfc64949e723256c9f860fd028055a42730", "f678918637d6949ca22b1d7cd5982309f8ad6dbe",
					"7f3dc96aa00c32144d5f11e368d36271eeaf64ff", "0ffc503972e23c8b6c079724b9b24ff59028d323",
					"84c6fb023cfde66935e2dba83991efefd45b0cc4", "b69be33b29de31a987633b3a382a1bee5f18a954"));

	@FunctionSpec(name = "test_ids", returnType = "set of string")
	public static HashSet<String> testIds() {
		return testIds;
	}

	@FunctionSpec(name = "text", returnType = "string")
	public static String text() {
		return text;
	}

}
