package boa.types.proto.enums;

import com.google.protobuf.ProtocolMessageEnum;

import boa.types.BoaProtoMap;

public class ReferenceKindProtoMap extends BoaProtoMap {
	/** {@inheritDoc} */
	@Override
	protected Class<? extends ProtocolMessageEnum> getEnumClass() {
		return boa.types.Toplevel.Reference.ReferenceKind.class;
	}
}
