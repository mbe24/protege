package org.beyene.protege.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.beyene.protege.core.Type;

public final class Primitive<T> {

	public static final Primitive<Boolean> BOOLEAN = new Primitive<>(Boolean.class, Type.BOOLEAN);
	public static final Primitive<Byte[]> BYTES = new Primitive<>(Byte[].class, Type.BYTE);
	public static final Primitive<Integer> INTEGER = new Primitive<>(Integer.class, Type.INTEGER);
	public static final Primitive<String> STRING = new Primitive<>(String.class, Type.STRING);
	public static final Primitive<Float> FLOAT = new Primitive<>(Float.class, Type.FLOAT);
	public static final Primitive<Double> DOUBLE = new Primitive<>(Double.class, Type.DOUBLE);

	private static final Map<Type, Primitive<?>> types = new HashMap<>();
	static {
		for (Primitive<?> primitive : Arrays.asList(BOOLEAN, BYTES, INTEGER, STRING, FLOAT, DOUBLE))
			types.put(primitive.getMappingType(), primitive);
	}

	private final Class<T> type;
	private final Type mappingType;

	private Primitive(Class<T> type, Type mappingType) {
		this.type = type;
		this.mappingType = mappingType;
	}

	public Class<T> getType() {
		return type;
	}

	public Type getMappingType() {
		return mappingType;
	}

	public static Primitive<?> forType(Type type) {
		return types.get(type);
	}
}