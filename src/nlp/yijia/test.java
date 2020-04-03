package yijia;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

public class test {
	public static void main(String[] args) throws IOException {

		String base = System.getProperty("user.dir");
		System.out.println(base);
		String model1 = "en-token.bin";
		String model2 = "en-ner-person.bin";

		// Loading the tokenizer model
//		InputStream inputStreamTokenizer = new FileInputStream(base + "/lib/nlp/model/en-token.bin");
		InputStream inputStreamTokenizer = test.class.getResourceAsStream("/model/" + model1);
		
		
		System.out.println(new test().getClass().getResource("/model/" + model1));
		System.out.println(new test().getClass().getClassLoader().getResource("./model/" + model1));
		
		
		TokenizerModel tokenModel = new TokenizerModel(inputStreamTokenizer);
		
//		// Instantiating the TokenizerME class
		TokenizerME tokenizer = new TokenizerME(tokenModel);

		// Tokenizing the sentence in to a string array
		String sentence = "Mike is senior programming" + " manager and Rama is a clerk both are working"
				+ " at Tutorialspoint";
		String tokens[] = tokenizer.tokenize(sentence);

		// Loading the NER-person model
//		InputStream inputStreamNameFinder = new FileInputStream(base + "/lib/nlp/model/en-ner-person.bin");
		InputStream inputStreamNameFinder = test.class.getResourceAsStream("/model/" + model2);
		TokenNameFinderModel model = new TokenNameFinderModel(inputStreamNameFinder);

		// Instantiating the NameFinderME class
		NameFinderME nameFinder = new NameFinderME(model);

		// Finding the names in the sentence
		Span nameSpans[] = nameFinder.find(tokens);

		// Printing the names and their spans in a sentence
		for (Span s : nameSpans)
			System.out.println(s.toString() + "  " + tokens[s.getStart()]);
//		
		
	}
}
