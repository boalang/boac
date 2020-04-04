package boa.functions.paper;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

import boa.functions.FunctionSpec;

public class BoaNLPIntrinsics {
	
	public static void main(String[] args) throws IOException {

	}




	public static String text = "Why painful the sixteen how minuter looking nor. "
			+ "Subject but why ten earnest husband imagine sixteen brandon. "
			+ "Are unpleasing occasional celebrated motionless unaffected conviction out. "
			+ "Evil make to no five they. Stuff at avoid of sense small fully it whose an. "
			+ "Ten scarcely distance moreover handsome age although. As when have find fine "
			+ "or said no mile. He in dispatched in imprudence dissimilar be possession "
			+ "unreserved insensible. She evil face fine calm have now. Separate screened"
			+ " he outweigh of distance landlord. ";

	private static HashSet<String> testIds = new HashSet<String>(
			Arrays.asList("a08c87a957ae94e9d23bb572191e2ded2c16414e", "e89563eb94ead6b73b9bc6efde85843cbfb1653b",
					"31165346222e30211f9895693dee83cd824e2232", "b1052d3e68d607d53c9ba567094cac528de60fd1",
					"5722e1b167fe879fc2901ba8ea2fa69fec89a6f7", "62cbd36e69d6db0897bd514bd120772bc8841c1d",
					"d7823cfc64949e723256c9f860fd028055a42730", "f678918637d6949ca22b1d7cd5982309f8ad6dbe",
					"7f3dc96aa00c32144d5f11e368d36271eeaf64ff", "0ffc503972e23c8b6c079724b9b24ff59028d323",
					"84c6fb023cfde66935e2dba83991efefd45b0cc4", "b69be33b29de31a987633b3a382a1bee5f18a954"));

	@FunctionSpec(name = "test_ids", returnType = "set of string")
	public static HashSet<String> testIds() {
		return testIds;
	}
}
