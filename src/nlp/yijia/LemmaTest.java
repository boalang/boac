package yijia;

import java.util.Arrays;
import java.util.List;

import opennlp.tools.stemmer.PorterStemmer;

public class LemmaTest {
	
	
	public static void main(String[] args) {
	      List<String> words = Arrays.asList("eating", "going", "done", "eaten");
	      PorterStemmer porterStemmer = new PorterStemmer();

	      words.forEach(word -> {
	          String stemmed = porterStemmer.stem(word);
	          System.out.println(word + "->" + stemmed);
	      });
	}
}