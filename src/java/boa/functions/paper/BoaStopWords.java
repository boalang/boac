package boa.functions.paper;

import java.util.Arrays;
import java.util.HashSet;

import boa.functions.FunctionSpec;

public class BoaStopWords {

	private static final String[] stopWords = new String[] { "i", ".", "#", "|", "me", "my", "myself", "we", "our",
			"ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she",
			"her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what",
			"which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been",
			"being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if",
			"or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between",
			"into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out",
			"on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why",
			"how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "https://doi.", "D", "such",
			"no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just",
			"don", "should", "using", "show", "result", "large", "also", "iv", "previously", "shown", "et", "al", ",",
			"al.", "al.," };

	private static final String[] testPaperIds = new String[] { "a08c87a957ae94e9d23bb572191e2ded2c16414e",
			"e89563eb94ead6b73b9bc6efde85843cbfb1653b", "31165346222e30211f9895693dee83cd824e2232",
			"b1052d3e68d607d53c9ba567094cac528de60fd1", "5722e1b167fe879fc2901ba8ea2fa69fec89a6f7",
			"62cbd36e69d6db0897bd514bd120772bc8841c1d", "d7823cfc64949e723256c9f860fd028055a42730",
			"f678918637d6949ca22b1d7cd5982309f8ad6dbe", "7f3dc96aa00c32144d5f11e368d36271eeaf64ff",
			"0ffc503972e23c8b6c079724b9b24ff59028d323", "84c6fb023cfde66935e2dba83991efefd45b0cc4",
			"b69be33b29de31a987633b3a382a1bee5f18a954" };

	private static HashSet<String> stopWordsSet;
	private static HashSet<String> testIds;

	static {
		stopWordsSet = new HashSet<String>(Arrays.asList(stopWords));
		testIds = new HashSet<String>(Arrays.asList(testPaperIds));
	}

	@FunctionSpec(name = "stop_words", returnType = "set of string")
	public static HashSet<String> stopWords() {
		return stopWordsSet;
	}

	@FunctionSpec(name = "update_stop_words", returnType = "set of string", formalParameters = { "string..." })
	public static HashSet<String> updateStopWords(String... words) {
		stopWordsSet.addAll(Arrays.asList(words));
		return stopWordsSet;
	}

	@FunctionSpec(name = "test_ids", returnType = "set of string")
	public static HashSet<String> testIds() {
		return testIds;
	}

}
