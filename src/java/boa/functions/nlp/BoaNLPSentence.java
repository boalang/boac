package boa.functions.nlp;

import java.io.IOException;
import java.io.InputStream;
import boa.functions.FunctionSpec;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import static boa.functions.nlp.BoaNLPProperties.*;

public class BoaNLPSentence {

	
	public final static InputStream inputStream = BoaNLPSentence.class.getResourceAsStream(OPEN_NLP_MODEL_PATH + EN_SENT_MODEL_PATH);
	public static SentenceDetectorME detector;
	
	static {
		 try {
			detector =  new SentenceDetectorME(new SentenceModel(inputStream));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FunctionSpec(name = "sent_detect", returnType = "array of string", formalParameters = { "string" })
	public static String[] sentDetect(final String text) {
		return detector.sentDetect(text);
	}
	
}
