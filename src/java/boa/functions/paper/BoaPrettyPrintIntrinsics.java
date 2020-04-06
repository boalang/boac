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

import static boa.functions.BoaTimeIntrinsics.yearOf;
import static boa.functions.nlp.BoaNLPNoiseRemoval.isASCII;

import boa.functions.FunctionSpec;
import boa.types.Toplevel.Author;
import boa.types.Toplevel.Metadata;
import boa.types.Toplevel.Paper;

/**
 * @author yijiahuang
 * @author rdyer
 */
public class BoaPrettyPrintIntrinsics {

	@FunctionSpec(name = "pretty_print", returnType = "string", formalParameters = { "Metadata" })
	public static String prettyPrint(final Metadata m) {
		String s = "";

		// authors
		if (m.getAuthorsCount() > 0)
			s = authors(m) + ".";

		// year
		if (m.hasPublishTime())
			s += " " + yearOf(m.getPublishTime()) + ".";

		// paper title
		if (m.hasTitle() && isASCII(m.getTitle()))
			s += " " + m.getTitle() + ".";

		// journal
		if (m.hasJournal() && isASCII(m.getJournal()))
			s += " " + m.getJournal() + ".";

		// doi
		if (m.hasDoiUrl() && isASCII(m.getDoiUrl()))
			s += " " + m.getDoiUrl();

		// pubmed ID
		if (m.hasPubmedId())
			s += " https://www.ncbi.nlm.nih.gov/pubmed/" + m.getPubmedId();

		return s;
	}

	@FunctionSpec(name = "pretty_print", returnType = "string", formalParameters = { "Paper" })
	public static String prettyPrint(final Paper p) {
		if (p.hasMetadata())
			return prettyPrint(p.getMetadata());
		return "";
	}

	@FunctionSpec(name = "authors", returnType = "string", formalParameters = { "Metadata" })
	public static String authors(final Metadata m) {
		String s = "";

		if (m.getAuthorsCount() > 0) {
			int lastIdx = m.getAuthorsCount() - 1;
			for (int i = 0; i < m.getAuthorsCount(); i++) {
				if (i == 0) {
					s += getAuthor(m.getAuthors(i));
				} else if (i == lastIdx) {
					if (lastIdx > 1)
						s += ",";
					s += " and " + getAuthor(m.getAuthors(i));
				} else {
					s += ", " + getAuthor(m.getAuthors(i));
				}
			}
		}

		return s;
	}

	@FunctionSpec(name = "authors", returnType = "string", formalParameters = { "Paper" })
	public static String authors(final Paper p) {
		if (p.hasMetadata())
			return authors(p.getMetadata());
		return "";
	}

	private static String getAuthor(Author author) {
		String s = "";
		// first name
		String first = author.getFirst();
		s += first;
		if (first.length() == 1)
			s += ".";
		s += " ";

		// middle name
		String mid = "";
		for (String middle : author.getMiddleList())
			for (String comp : middle.split(" "))
				mid += comp + ".";
		if (!mid.equals(""))
			s += mid + " ";

		// last name
		s += author.getLast();
		return s;
	}

}
