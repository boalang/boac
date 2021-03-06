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
package boa.types.proto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boa.types.BoaInt;
import boa.types.BoaProtoList;
import boa.types.BoaProtoTuple;
import boa.types.BoaString;
import boa.types.BoaType;
import boa.types.proto.enums.ChangeKindProtoMap;
import boa.types.proto.enums.StatementKindProtoMap;

/**
 * A {@link StatementProtoTuple}.
 * 
 * @author rdyer
 */
public class StatementProtoTuple extends BoaProtoTuple {
	private final static List<BoaType> members = new ArrayList<BoaType>();
	private final static Map<String, Integer> names = new HashMap<String, Integer>();

	static {
		int counter = 0;

		names.put("kind", counter++);
		members.add(new StatementKindProtoMap());

		names.put("comments", counter++);
		members.add(new BoaProtoList(new CommentProtoTuple()));

		names.put("statements", counter++);
		members.add(new BoaProtoList(new StatementProtoTuple()));

		names.put("initializations", counter++);
		members.add(new BoaProtoList(new ExpressionProtoTuple()));

		names.put("conditions", counter++);
		members.add(new BoaProtoList(new ExpressionProtoTuple()));

		names.put("updates", counter++);
		members.add(new BoaProtoList(new ExpressionProtoTuple()));

		names.put("variable_declaration", counter++);
		members.add(new VariableProtoTuple());

		names.put("type_declaration", counter++);
		members.add(new DeclarationProtoTuple());

		names.put("expressions", counter++);
		members.add(new BoaProtoList(new ExpressionProtoTuple()));
		
		names.put("key", counter++);
		members.add(new BoaInt());
		
		names.put("methods", counter++);
		members.add(new BoaProtoList(new MethodProtoTuple()));
		
		names.put("variable_declarations", counter++);
		members.add(new BoaProtoList(new VariableProtoTuple()));
		
		names.put("type_declarations", counter++);
		members.add(new BoaProtoList(new DeclarationProtoTuple()));
		
		names.put("names", counter++);
		members.add(new BoaProtoList(new BoaString()));
		
	}

	/**
	 * Construct a {@link StatementProtoTuple}.
	 */
	public StatementProtoTuple() {
		super(members, names);
	}

	/** @{inheritDoc} */
	@Override
	public String toJavaType() {
		return "boa.types.Ast.Statement";
	}
}
