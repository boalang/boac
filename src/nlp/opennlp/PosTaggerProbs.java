package opennlp;

import java.io.FileInputStream; 
import java.io.InputStream;  

import opennlp.tools.postag.POSModel; 
import opennlp.tools.postag.POSSample; 
import opennlp.tools.postag.POSTaggerME; 
import opennlp.tools.tokenize.WhitespaceTokenizer;  

/*
 * Parts of Speech	Meaning of parts of speech
	NN	Noun, singular or mass
	DT	Determiner
	VB	Verb, base form
	VBD	Verb, past tense
	VBZ	Verb, third person singular present
	IN	Preposition or subordinating conjunction
	NNP	Proper noun, singular
	TO	to
	JJ	Adjective
 */
public class PosTaggerProbs { 
   
   public static void main(String args[]) throws Exception{ 
      
      //Loading Parts of speech-maxent model       
      InputStream inputStream = new FileInputStream("model/en-pos-maxent.bin"); 
      POSModel model = new POSModel(inputStream); 
       
      //Creating an object of WhitespaceTokenizer class  
      WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE; 
       
      //Tokenizing the sentence 
      String sentence = "Hi welcome to Tutorialspoint"; 
      String[] tokens = whitespaceTokenizer.tokenize(sentence); 
       
      //Instantiating POSTaggerME class 
      POSTaggerME tagger = new POSTaggerME(model); 
             
      //Generating tags 
      String[] tags = tagger.tag(tokens);       
      
      //Instantiating the POSSample class 
      POSSample sample = new POSSample(tokens, tags);  
      System.out.println(sample.toString());
      
      //Probabilities for each tag of the last tagged sentence. 
      double [] probs = tagger.probs();       
      System.out.println("  ");       
      
      //Printing the probabilities  
      for(int i = 0; i<probs.length; i++) 
         System.out.println(probs[i]); 
   } 
}      