/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qsprwtri;

import com.ibm.as400.access.AS400;

import io.greenscreens.jt400.JT400ExtFactory;
import io.greenscreens.jt400.interfaces.IJT400Program;

/**
 * Service program call example
 * The Get Client Handle (QzcaGetClientHandle) API 
 * allows applications to know the client handle assigned if the client ID is known.
 * 
 * https://www.ibm.com/support/knowledgecenter/ssw_ibm_i_71/apis/qzcagetc.htm
 */
public interface IQSPRWTRI extends IJT400Program<QSPRWTRI> {

	public static IQSPRWTRI create(final AS400 as400) {
		return JT400ExtFactory.create(as400, IQSPRWTRI.class);
	}

}
