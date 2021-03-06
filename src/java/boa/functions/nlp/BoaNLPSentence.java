package boa.functions.nlp;

import java.io.IOException;
import java.io.InputStream;

import boa.functions.FunctionSpec;
import boa.types.Toplevel.Paragraph;
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
	
	@FunctionSpec(name = "sentences", returnType = "array of string", formalParameters = { "string" })
	public static String[] sentences(final String text) {
		return detector.sentDetect(text);
	}
	
	@FunctionSpec(name = "sentences", returnType = "array of string", formalParameters = { "Paragraph" })
	public static String[] sentences(final Paragraph para) {
		return detector.sentDetect(para.getText());
	}
	
}
