package opennlp;

import java.io.FileInputStream; 
import java.io.InputStream; 
import opennlp.tools.tokenize.TokenizerME; 
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
import opennlp.tools.tokenize.SimpleTokenizer; 


public class TokenizerMESentence {
	public static void main(String args[]) throws Exception{     
	     
	      String sentence = "Hi. How are you? Welcome to Tutorialspoint. " 
	            + "We provide free tutorials on various technologies"; 
	       
	      //Loading the Tokenizer model 
	      InputStream inputStream = new FileInputStream("model/en-token.bin"); 
	      TokenizerModel tokenModel = new TokenizerModel(inputStream); 
	       
	      //Instantiating the TokenizerME class 
	      TokenizerME tokenizer = new TokenizerME(tokenModel); 
	       
	      //Tokenizing the given raw text 
	      String tokens[] = tokenizer.tokenize(sentence);       
	          
	      //Printing the tokens  
	      for (String a : tokens) 
	         System.out.println(a);
	      
	      
	    //Instantiating SimpleTokenizer class 
	      SimpleTokenizer simpleTokenizer = SimpleTokenizer.INSTANCE;  
	       
	      //Retrieving the boundaries of the tokens 
	      Span[] tokens_position = simpleTokenizer.tokenizePos(sentence);  
	      
	      //Getting the probabilities of the recent calls to tokenizePos() method 
	      double[] probs = tokenizer.getTokenProbabilities(); 
	      
	      //Printing the spans of tokens 
	      for( Span token : tokens_position)
	         System.out.println(token +" "+sentence.substring(token.getStart(), token.getEnd()));
	      	 System.out.println();
	      	 for(int i = 0; i<probs.length; i++) 
	            System.out.println(probs[i]);    
	      
	   } 
}
