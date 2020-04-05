package boa.functions.nlp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import boa.functions.FunctionSpec;
import opennlp.tools.tokenize.SimpleTokenizer;

public class BoaNLPTokenizer {

	public final static SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;

	@FunctionSpec(name = "tokenize", returnType = "array of string", formalParameters = { "string" })
	public static String[] tokenize(final String text) {
		return simpleTokenizer.tokenize(text);
	}

	@FunctionSpec(name = "get_tokens", returnType = "array of string", formalParameters = { "string" })
	public static String[] getTokens(final String text) {
		return text.trim().toLowerCase().split("\\s+");
	}

	@FunctionSpec(name = "get_tokens", returnType = "array of string", formalParameters = { "string", "set of string" })
	public static String[] getTokens(final String text, final HashSet<String> filter) {
		List<String> list = Arrays.asList(getTokens(text)).stream()
				.filter(line -> !filter.contains(line))
				.collect(Collectors.toList());
		return list.toArray(new String[list.size()]);
	}

}
