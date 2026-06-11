/*
 * Copyright (C) 2015, 2026 Green Screens Ltd.
 */
package io.greenscreens.jt400;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.ibm.as400.access.AS400Message;

/**
 * Exception thrown from Proxy engine if
 * program call was unsuccessful.
 */
public class JT400Exception extends Exception {

	private static final long serialVersionUID = 1L;

	private final AS400Message [] errors;

	JT400Exception(final AS400Message[] errors) {
		super(JT400ExtUtil.getErrors(errors));
		this.errors = errors == null ? new AS400Message[]{} : errors;
	}

	public List<AS400Message> getErrors() {
		return Collections.unmodifiableList(Arrays.asList(errors));
	}

}
