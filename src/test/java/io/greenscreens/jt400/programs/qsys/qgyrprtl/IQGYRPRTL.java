/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qgyrprtl;

import com.ibm.as400.access.AS400;

import io.greenscreens.jt400.JT400ExtFactory;
import io.greenscreens.jt400.interfaces.IJT400Program;

/**
 * 
 */
public interface IQGYRPRTL extends IJT400Program<QGYRPRTL> {

	public static IQGYRPRTL create(final AS400 as400) {
		return JT400ExtFactory.create(as400, IQGYRPRTL.class);
	}

}
