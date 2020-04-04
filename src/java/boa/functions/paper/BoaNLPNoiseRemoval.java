package boa.functions.paper;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;

import boa.functions.FunctionSpec;

public class BoaNLPNoiseRemoval {

	@FunctionSpec(name = "nonascii", returnType = "array of string", formalParameters = { "array of string" })
	public static String[] nonASCIIi(String[] words) {
		List<String> list = Arrays.asList(words).stream().filter(word -> isASCII(word)).collect(Collectors.toList());
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

}
