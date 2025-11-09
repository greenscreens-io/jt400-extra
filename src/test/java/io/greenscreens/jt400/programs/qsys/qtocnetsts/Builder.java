/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qtocnetsts;

import java.nio.ByteBuffer;

import io.greenscreens.jt400.JT400ExtUtil;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Builder to build {@link QTOCNETSTS}.
 */
public final class Builder {

	ByteBuffer receiver;
	int length;
	String format;
	ByteBuffer errorCode;

	Builder() {}

	public Builder withReceiver(ByteBuffer receiver) {
		this.receiver = receiver;
		return this;
	}

	public Builder withLength(int length) {
		this.length = length;
		return this;
	}

	public Builder withFormat(String format) {
		this.format = format;
		return this;
	}

	public Builder withErrorCode(ByteBuffer errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public QTOCNETSTS build() {
		return new QTOCNETSTS(this);
	}

	/**
	 * Helper method as only relevant args are format and display name.
	 * Also, validate proper format is provided
	 * @param <T>
	 * @param clazz
	 * @param displayName
	 * @return
	 */
	public static <T extends IJT400Format> QTOCNETSTS build(Class<T> clazz) {

		if (!JT400ExtUtil.contains(clazz, QTOCNETSTS.class)) {
			throw new RuntimeException("Format not supported by parameter definition!");
		}

		final int len = JT400ExtUtil.getFormatLength(clazz);

		return QTOCNETSTS.builder()
				.withReceiver(ByteBuffer.allocate(len))
				.withLength(len)
				.withFormat(clazz.getSimpleName())
				.withErrorCode(ByteBuffer.allocate(0))
				.build();
	}

}
