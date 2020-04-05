package boa.functions.nlp;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;

import boa.functions.FunctionSpec;

public class BoaNLPNoiseRemoval {

	@FunctionSpec(name = "no_nascii", returnType = "array of string", formalParameters = { "array of string" })
	public static String[] nonASCIIi(String[] words) {
		List<String> list = Arrays.asList(words).stream().filter(word -> isASCII(word)).collect(Collectors.toList());
		return list.toArray(new String[list.size()]);
	}

	@FunctionSpec(name = "is_num", returnType = "bool", formalParameters = { "string" })
	public static boolean isNumber(String str) {
		return NumberUtils.isNumber(str);
	}

	@FunctionSpec(name = "is_digits", returnType = "bool", formalParameters = { "string" })
	public static boolean isDigits(String str) {
		return NumberUtils.isDigits(str);
	}

	@FunctionSpec(name = "is_ascii", returnType = "bool", formalParameters = { "string" })
	public static boolean isASCII(String str) {
		if (str.equals(""))
			return false;
		return Charset.forName("US-ASCII").newEncoder().canEncode(str);
	}

	@FunctionSpec(name = "no_punct", returnType = "string", formalParameters = { "string" })
	public static String noPunct(String str) {
		return str.replaceAll("\\p{Punct}", "");
	}

}
