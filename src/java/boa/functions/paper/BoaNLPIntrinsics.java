package boa.functions.paper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import boa.functions.FunctionSpec;

public class BoaNLPIntrinsics {

	@FunctionSpec(name = "get_tokens", returnType = "array of string", formalParameters = { "string" })
	public static String[] getTokens(final String text) {
		return text.trim().toLowerCase().split("\\s+");
	}

	/*
	 * TODO: btw would it be better to have that 2nd function just turn the array
	 * into a list, and then call removeAllon the list and pass in the set? then
	 * convert back to array
	 */
	@FunctionSpec(name = "get_tokens", returnType = "array of string", formalParameters = { "string", "set of string" })
	public static String[] getTokens(final String text, final HashSet<String> filter) {
		List<String> res = new ArrayList<String>();
		for (String word : getTokens(text))
			if (!filter.contains(word))
				res.add(word);
		return res.toArray(new String[res.size()]);
	}
	
	
	

	public static String text = "Why painful the sixteen how minuter looking nor. "
			+ "Subject but why ten earnest husband imagine sixteen brandon. "
			+ "Are unpleasing occasional celebrated motionless unaffected conviction out. "
			+ "Evil make to no five they. Stuff at avoid of sense small fully it whose an. "
			+ "Ten scarcely distance moreover handsome age although. As when have find fine "
			+ "or said no mile. He in dispatched in imprudence dissimilar be possession "
			+ "unreserved insensible. She evil face fine calm have now. Separate screened"
			+ " he outweigh of distance landlord. ";
	
	
	public static void main(String[] args) {
		
	}

}
