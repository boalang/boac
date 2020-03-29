/*
 * Copyright 2014, Hridesh Rajan, Robert Dyer, 
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
package boa.runtime;

import java.util.List;

import boa.functions.BoaAstIntrinsics;
import boa.functions.BoaIntrinsics;
import boa.types.Ast.*;
import boa.types.Code.CodeRepository;
import boa.types.Code.Revision;
import boa.types.Diff.ChangedFile;
import boa.types.Shared.Person;
import boa.types.Toplevel.Affiliation;
import boa.types.Toplevel.Author;
import boa.types.Toplevel.Citation;
import boa.types.Toplevel.Location;
import boa.types.Toplevel.Metadata;
import boa.types.Toplevel.Paper;
import boa.types.Toplevel.Paragraph;
import boa.types.Toplevel.Project;
import boa.types.Toplevel.Reference;
import boa.types.Toplevel.Section;

/**
 * Boa abstract AST visitor.
 * 
 * The <code>visit()</code> methods first call <code>preVisit()</code> for the
 * node. If <code>preVisit()</code> returns <code>true</code>, then each of that
 * node's children are visited and then <code>postVisit()</code> is called.
 * 
 * By default, all <code>preVisit()</code> methods call
 * {@link #defaultPreVisit()} and return <code>true</code>. By default, all
 * <code>postVisit()</code> methods call {@link #defaultPostVisit()}.
 * 
 * @author rdyer
 */
public abstract class BoaAbstractVisitor1 {
	/**
	 * Initializes any visitor-specific data before starting a visit.
	 * 
	 * @return itself, to allow method chaining
	 */
	public BoaAbstractVisitor1 initialize() {
		return this;
	}

	/* ----------------------------- pre-visit ----------------------------- */
	
	/**
	 * Provides a default action for pre-visiting nodes. Any <code>preVisit()</code>
	 * method that is not overridden calls this method.
	 * 
	 * @return always returns true
	 */
	protected boolean defaultPreVisit() throws Exception {
		return true;
	}

	protected boolean preVisit(final Paper node) throws Exception {
		return defaultPreVisit();
	}

	protected boolean preVisit(final Metadata node) throws Exception {
		return defaultPreVisit();
	}

	protected boolean preVisit(final Author node) throws Exception {
		return defaultPreVisit();
	}

	protected boolean preVisit(final Affiliation node) throws Exception {
		return defaultPreVisit();
	}

	protected boolean preVisit(final Section node) throws Exception {
		return defaultPreVisit();
	}

	protected boolean preVisit(final Paragraph node) throws Exception {
		return defaultPreVisit();
	}

	protected boolean preVisit(final Citation node) throws Exception {
		return defaultPreVisit();
	}

	protected boolean preVisit(final Location node) throws Exception {
		return defaultPreVisit();
	}

	protected boolean preVisit(final Reference node) throws Exception {
		return defaultPreVisit();
	}

	/* ----------------------------- post-visit ----------------------------- */
	
	/**
	 * Provides a default action for post-visiting nodes. Any
	 * <code>postVisit()</code> method that is not overridden calls this method.
	 */
	protected void defaultPostVisit() throws Exception {
	}

	protected void postVisit(final Paper node) throws Exception {
		defaultPostVisit();
	}

	protected void postVisit(final Metadata node) throws Exception {
		defaultPostVisit();
	}

	protected void postVisit(final Author node) throws Exception {
		defaultPostVisit();
	}

	protected void postVisit(final Affiliation node) throws Exception {
		defaultPostVisit();
	}

	protected void postVisit(final Section node) throws Exception {
		defaultPreVisit();
	}

	protected void postVisit(final Paragraph node) throws Exception {
		defaultPreVisit();
	}

	protected void postVisit(final Citation node) throws Exception {
		defaultPreVisit();
	}

	protected void postVisit(final Location node) throws Exception {
		defaultPreVisit();
	}

	protected void postVisit(final Reference node) throws Exception {
		defaultPreVisit();
	}

	/* ----------------------------- visit ----------------------------- */
	
	public final void visit(final Paper node) throws Exception {
		if (preVisit(node)) {
			visit(node.getMetadata());
			for (Paragraph para : node.getAbstractList())
				visit(para);
			for (Section sec : node.getBodyTextList())
				visit(sec);
			for (Reference ref : node.getBibEntriesList())
				visit(ref);
			for (Reference ref : node.getRefEntriesList())
				visit(ref);
			postVisit(node);
		}
	}

	public final void visit(final Metadata node) throws Exception {
		if (preVisit(node)) {
			for (Author author : node.getAuthorsList())
				visit(author);
			postVisit(node);
		}
	}

	public final void visit(final Author node) throws Exception {
		if (preVisit(node)) {
			visit(node.getAffiliation());
			postVisit(node);
		}
	}

	public final void visit(final Affiliation node) throws Exception {
		if (preVisit(node)) {
			visit(node.getLocation());
			postVisit(node);
		}
	}

	public final void visit(final Section node) throws Exception {
		if (preVisit(node)) {
			for (Paragraph para : node.getBodyList())
				visit(para);
			postVisit(node);
		}
	}

	public final void visit(final Paragraph node) throws Exception {
		if (preVisit(node)) {
			for (Citation cite : node.getCiteSpansList())
				visit(cite);
			for (Citation cite : node.getRefSpansList())
				visit(cite);
			postVisit(node);
		}
	}

	public final void visit(final Citation node) throws Exception {
		if (preVisit(node)) {

			postVisit(node);
		}
	}

	public final void visit(final Location node) throws Exception {
		if (preVisit(node)) {

			postVisit(node);
		}
	}

	public final void visit(final Reference node) throws Exception {
		if (preVisit(node)) {
			for (Author author : node.getAuthorsList())
				visit(author);
			postVisit(node);
		}
	}
}
