package boa.functions.paper;

import boa.functions.FunctionSpec;
import opennlp.tools.stemmer.PorterStemmer;

public class BoaNLPStemmer {

	public final static PorterStemmer stemmer = new PorterStemmer();

	@FunctionSpec(name = "stem", returnType = "string", formalParameters = { "string" })
	public static String stem(String word) {
		return stemmer.stem(word);
	}

	@FunctionSpec(name = "stem", returnType = "array of string", formalParameters = { "array of string" })
	public static String[] stem(String[] words) {
		for (int i = 0; i < words.length; i++)
			words[i] = stemmer.stem(words[i]);
		return words;
	}

}
