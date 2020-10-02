/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400.interfaces;

import com.ibm.as400.access.AS400;
import com.ibm.as400.access.AS400Message;

import io.greenscreens.jt400.JT400Exception;

/**
 * Dummy Interface for proxy invocation handler - program call generator
 * @param <T>
 * @param <K>
 */
public interface IJT400Program<T extends IJT400Params> {

	public boolean isError();

	public AS400Message[] getErrors();

	public void call(final T args) throws JT400Exception;

	public void call(final AS400 as400, final T args) throws JT400Exception;

	public <F extends IJT400Format> F call(final T args, final Class<F> format) throws JT400Exception;

	public <F extends IJT400Format> F call(final AS400 as400, final T args, final F format) throws JT400Exception;
}
