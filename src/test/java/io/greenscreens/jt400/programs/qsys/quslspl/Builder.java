/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.quslspl;

import java.nio.ByteBuffer;
import io.greenscreens.jt400.ERRC0100;
import io.greenscreens.jt400.interfaces.IJT400Format;

public class Builder {

	protected String userSpaceName;
	protected String format;
	protected String userName;
	protected String outputQueueName;
	protected String formType;
	protected String userSpecifiedData;
	protected ByteBuffer errorCode;

	Builder() {}

	public Builder withUserSpaceName(final String userSpaceName) {
		this.userSpaceName = userSpaceName;
		return this;
	}

	public Builder withUserName(final String userName) {
		this.userName = userName;
		return this;
	}

	public Builder withOutputQueueName(final String outputQueueName) {
		this.outputQueueName = outputQueueName;
		return this;
	}
	
	public Builder withFormType(final String formType) {
		this.formType = formType;
		return this;
	}
	
	public Builder withUserSpecifiedData(final String userSpecifiedData) {
		this.userSpecifiedData = userSpecifiedData;
		return this;
	}
	
	public Builder withFormat(final String format) {
		this.format = format;
		return this;
	}
	
	public Builder withErrorCode(final ByteBuffer errorCode) {
		this.errorCode = errorCode;
		return this;
	}

	public QUSLSPL build() {
		return new QUSLSPL(this);
	}
	

	public static <T extends IJT400Format> Builder create(final Class<T> format, final String userSpaceName, final String outputQueueName, final int errorSize) {		

		final ByteBuffer err = ERRC0100.toBuffer(errorSize);
		return new Builder().withErrorCode(err)
				.withUserName(userSpaceName)
				.withOutputQueueName(outputQueueName)
				.withFormat(format.getSimpleName())
				.withErrorCode(err);

	}

}
