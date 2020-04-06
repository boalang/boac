/*
 * Copyright 2020, Hridesh Rajan, Robert Dyer, Yijia Huang
 *                 Bowling Green State University
 *                 and Iowa State University of Science and Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package boa.functions.paper;

import boa.functions.FunctionSpec;
import boa.types.Toplevel.Paper;
import boa.types.Toplevel.Paragraph;
import boa.types.Toplevel.Section;

import static boa.functions.paper.BoaPaperIntrinsics.*;

/**
 * @author rdyer
 * @author yijiahuang
 */
public class BoaContentPatternIntrinsics {

	/* ------------------------- Finding ------------------------- */

	@FunctionSpec(name = "is_finding", returnType = "bool", formalParameters = { "Paper" })
	public static boolean isFinding(final Paper p) {
		for (int i = 0; i < p.getAbstractCount(); i++)
			if (isFinding(p.getAbstract(i)))
				return true;
		for (int i = 0; i < p.getBodyTextCount(); i++)
			if (isFinding(p.getBodyText(i)))
				return true;
		return false;
	}

	@FunctionSpec(name = "is_finding", returnType = "bool", formalParameters = { "Section" })
	public static boolean isFinding(final Section s) {
		if (isFinding(s.getTitle()))
			return true;
		for (int i = 0; i < s.getBodyCount(); i++)
			if (isFinding(s.getBody(i)))
				return true;
		return false;
	}

	@FunctionSpec(name = "is_finding", returnType = "bool", formalParameters = { "Paragraph" })
	public static boolean isFinding(final Paragraph p) {
		if (!p.hasText())
			return false;

		return isFinding(p.getText());
	}

	@FunctionSpec(name = "is_finding", returnType = "bool", formalParameters = { "string" })
	public static boolean isFinding(final String s) {
		final String lc = s.toLowerCase();

		if (lc.endsWith("results") || lc.endsWith("findings"))
			return true;

		if (lc.indexOf("finding") != -1)
			return true;

		if (lc.indexOf("we found") != -1)
			return true;

		return false;
	}

	@FunctionSpec(name = "has_finding", returnType = "bool", formalParameters = { "string", "Paper" })
	public static boolean hasFinding(final String finding, final Paper p) {
		if (!isFinding(p))
			return false;
		return searchPaper(finding, p);
	}

	@FunctionSpec(name = "has_finding", returnType = "bool", formalParameters = { "string", "Section" })
	public static boolean hasFinding(final String finding, final Section s) {
		if (!isFinding(s))
			return false;
		return searchSection(finding, s);
	}

	@FunctionSpec(name = "has_finding", returnType = "bool", formalParameters = { "string", "Paragraph" })
	public static boolean hasFinding(final String finding, final Paragraph p) {
		if (!isFinding(p))
			return false;
		return searchPara(finding, p);
	}

	@FunctionSpec(name = "has_finding", returnType = "bool", formalParameters = { "string", "string" })
	public static boolean hasFinding(final String finding, final String s) {
		if (!isFinding(s))
			return false;
		return searchKeyword(s, finding);
	}

	/* ------------------------- Conclusion ------------------------- */
	
	@FunctionSpec(name = "is_conclusion", returnType = "bool", formalParameters = { "string" })
	public static boolean isConclusion(final String s) {
		final String lc = s.toLowerCase();

		if (searchKeywords(lc, "in conclusion"))
			return true;

		if (searchKeywords(lc, "to conclude"))
			return true;
		
		if (searchKeywords(lc, "consequently"))
			return true;
		
		if (searchKeywords(lc, "sum up"))
			return true;
		
		if (searchKeywords(lc, "in summary"))
			return true;

		return false;
	}
	
}
