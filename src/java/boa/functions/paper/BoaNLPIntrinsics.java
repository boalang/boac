package boa.functions.paper;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;

import boa.functions.FunctionSpec;
import opennlp.tools.stemmer.PorterStemmer;

public class BoaNLPIntrinsics {

	@FunctionSpec(name = "get_tokens", returnType = "array of string", formalParameters = { "string" })
	public static String[] getTokens(final String text) {
		return text.trim().toLowerCase().split("\\s+");
	}

	@FunctionSpec(name = "get_tokens", returnType = "array of string", formalParameters = { "string", "set of string" })
	public static String[] getTokens(final String text, final HashSet<String> filter) {
		List<String> list = Arrays.asList(getTokens(text)).stream().filter(line -> !filter.contains(line))
				.collect(Collectors.toList());
		return list.toArray(new String[list.size()]);
	}



	// ------------------------- Stemming -------------------------

	public static final PorterStemmer stemmer = new PorterStemmer();

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

	// ------------------------- Noise Removal -------------------------

	@FunctionSpec(name = "nonascii", returnType = "array of string", formalParameters = { "array of string" })
	public static String[] nonASCIIi(String[] words) {
		List<String> list = Arrays.asList(getTokens(text)).stream().filter(word -> isASCII(word))
				.collect(Collectors.toList());
		return list.toArray(new String[list.size()]);
	}

	@FunctionSpec(name = "isnum", returnType = "bool", formalParameters = { "string" })
	public static boolean isNumber(String str) {
		return NumberUtils.isNumber(str);
	}

	@FunctionSpec(name = "isdigits", returnType = "bool", formalParameters = { "string" })
	public static boolean isDigits(String str) {
		return NumberUtils.isDigits(str);
	}

	@FunctionSpec(name = "isascii", returnType = "bool", formalParameters = { "string" })
	public static boolean isASCII(String str) {
		if (str.equals(""))
			return false;
		return Charset.forName("US-ASCII").newEncoder().canEncode(str);
	}

	@FunctionSpec(name = "nopunct", returnType = "string", formalParameters = { "string" })
	public static String noPunct(String str) {
		return str.replaceAll("\\p{Punct}", "");
	}

	public static void main(String[] args) {
		System.out.println(Arrays.asList(nonASCIIi(getTokens(text))));
	}
	
	public static String text = "Why painful the sixteen how minuter looking nor. "
			+ "Subject but why ten earnest husband imagine sixteen brandon. "
			+ "Are unpleasing occasional celebrated motionless unaffected conviction out. "
			+ "Evil make to no five they. Stuff at avoid of sense small fully it whose an. "
			+ "Ten scarcely distance moreover handsome age although. As when have find fine "
			+ "or said no mile. He in dispatched in imprudence dissimilar be possession "
			+ "unreserved insensible. She evil face fine calm have now. Separate screened"
			+ " he outweigh of distance landlord. ";

}
