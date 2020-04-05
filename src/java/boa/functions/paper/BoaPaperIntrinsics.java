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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import boa.functions.FunctionSpec;
import boa.types.Toplevel.Author;
import boa.types.Toplevel.Metadata;
import boa.types.Toplevel.Paper;
import boa.types.Toplevel.Paragraph;
import boa.types.Toplevel.Reference;
import boa.types.Toplevel.Section;

import static boa.functions.BoaTimeIntrinsics.*;
import static boa.functions.nlp.BoaNLPNoiseRemoval.*;
import static boa.functions.nlp.BoaNLPIntrinsics.*;
import static boa.functions.nlp.BoaNLPSentence.*;

/**
 * @author yijiahuang
 * @author rdyer
 *
 */
public class BoaPaperIntrinsics {

	@FunctionSpec(name = "search_title", returnType = "bool", formalParameters = { "string", "Paper" })
	public static boolean searchTitle(final String keyword, final Paper p) {
		if (p.getMetadata().hasTitle() && searchKeyword(p.getMetadata().getTitle(), keyword))
			return true;
		return false;
	}

	@FunctionSpec(name = "search_abstract", returnType = "bool", formalParameters = { "string", "Paper" })
	public static boolean searchAbstract(final String keyword, final Paper p) {
		for (final Paragraph para : p.getAbstractList())
			if (searchKeyword(para.getText(), keyword))
				return true;
		return false;
	}

	@FunctionSpec(name = "search_body", returnType = "bool", formalParameters = { "string", "Paper" })
	public static boolean searchBody(final String keyword, final Paper p) {
		for (final Section sec : p.getBodyTextList())
			if (searchSection(keyword, sec))
				return true;
		return false;
	}

	@FunctionSpec(name = "search_section", returnType = "bool", formalParameters = { "string", "Section" })
	public static boolean searchSection(final String keyword, final Section sec) {
		if (searchKeyword(sec.getTitle(), keyword))
			return true;
		for (final Paragraph para : sec.getBodyList())
			if (searchPara(keyword, para))
				return true;
		return false;
	}

	@FunctionSpec(name = "search_para", returnType = "bool", formalParameters = { "string", "Paragraph" })
	public static boolean searchPara(final String keyword, final Paragraph para) {
		return searchKeyword(para.getText(), keyword);
	}

	@FunctionSpec(name = "search_paper", returnType = "bool", formalParameters = { "string", "Paper" })
	public static boolean searchPaper(final String keyword, final Paper p) {
		if (searchTitle(keyword, p))
			return true;
		if (searchAbstract(keyword, p))
			return true;
		if (searchBody(keyword, p))
			return true;
		return false;
	}

	@FunctionSpec(name = "find_paras", returnType = "array of Paragraph", formalParameters = { "Paper", "string..." })
	public static Paragraph[] findParas(final Paper p, final String... keywords) {
		List<Paragraph> results = new ArrayList<Paragraph>();
		for (Paragraph para : p.getAbstractList())
			if (searchKeywords(para.getText(), keywords))
				results.add(para);
		for (Section sec : p.getBodyTextList())
			for (Paragraph para : sec.getBodyList())
				if (searchKeywords(para.getText(), keywords))
					results.add(para);
		return results.toArray(new Paragraph[results.size()]);
	}

	@FunctionSpec(name = "find_stens", returnType = "array of String", formalParameters = { "Paper", "string..." })
	public static String[] findStens(final Paper p, final String... keywords) {
		List<String> results = new ArrayList<String>();
		for (Paragraph para : p.getAbstractList())
			for (String sentence : sentences(para))
				results.add(sentence);
		for (Section sec : p.getBodyTextList())
			for (Paragraph para : sec.getBodyList())
				for (String sentence : sentences(para))
					results.add(sentence);
		return results.toArray(new String[results.size()]);
	}

	/**
	 * @param from A time range start with MM/dd/yyyy inclusive
	 * @param to   A time range end with MM/dd/yyyy inclusive
	 * @param p    A Paper instance
	 * @return boolean
	 */
	@FunctionSpec(name = "search_paper", returnType = "bool", formalParameters = { "string", "string", "Paper" })
	public static boolean searchPaper(final String from, final String to, final Paper p) {
		DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
		long left = -1, right = -1;
		try {
			left = df.parse(from).getTime() * 1000;
			right = df.parse(to).getTime() * 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long publishTime = p.getMetadata().getPublishTime();
		if (publishTime >= left && publishTime <= right)
			return true;
		return false;
	}

	@FunctionSpec(name = "search_citation", returnType = "bool", formalParameters = { "string", "Paper" })
	public static boolean searchCitation(final String title, final Paper p) {
		for (Reference ref : p.getBibEntriesList())
			if (ref.getTitle().equals(title))
				return true;
		return false;
	}

	// TODO use Aho-Corasick algorithm??
	@FunctionSpec(name = "search_keywords", returnType = "bool", formalParameters = { "string", "string..." })
	public static boolean searchKeywords(final String src, final String... keywords) {
//		Trie trie = Trie.builder().onlyWholeWords().addKeywords(keywords).build();
//		Collection<Emit> emits = trie.parseText(src);
		for (String keyword : keywords)
			if (!searchKeyword(src, keyword))
				return false;
		return true;
	}

	public static void main(String[] args) {
		System.out.println(searchKeywords(text, "ten"));
	}

	@FunctionSpec(name = "search_keyword", returnType = "bool", formalParameters = { "string", "string" })
	public static boolean searchKeyword(final String src, final String keyword) {
		final int length = keyword.length();
		if (length == 0)
			return true; // Empty string is contained

		final char firstLo = Character.toLowerCase(keyword.charAt(0));
		final char firstUp = Character.toUpperCase(keyword.charAt(0));

		for (int i = src.length() - length; i >= 0; i--) {
			// Quick check before calling the more expensive regionMatches() method:
			final char ch = src.charAt(i);
			if (ch != firstLo && ch != firstUp)
				continue;

			if (src.regionMatches(true, i, keyword, 0, length))
				return true;
		}

		return false;
	}

	@FunctionSpec(name = "pretty_print", returnType = "string", formalParameters = { "Metadata" })
	public static String prettyPrint(final Metadata m) {
		String s = "";

		// authors
		if (m.getAuthorsCount() > 0)
			s += authors(m) + ". ";

		// year
		if (m.hasPublishTime())
			s += yearOf(m.getPublishTime()) + ". ";

		// paper title
		if (m.hasTitle() && isASCII(m.getTitle()))
			s += m.getTitle() + ". ";

		// journal
		if (m.hasJournal() && isASCII(m.getJournal()))
			s += m.getJournal() + ". ";

		// doi
		if (m.hasDoiUrl() && isASCII(m.getDoiUrl()))
			s += m.getDoiUrl();

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
}
