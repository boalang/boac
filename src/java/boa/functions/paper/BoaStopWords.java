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
	
	private static HashSet<String> stopWordsSet;
	
	static {
		stopWordsSet = new HashSet<String>(Arrays.asList(stopWords));
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

}
