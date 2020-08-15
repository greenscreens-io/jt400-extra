/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qtocnetsts;

import com.ibm.as400.access.AS400;

import io.greenscreens.jt400.JT400ExtFactory;
import io.greenscreens.jt400.interfaces.IJT400Program;


/**
 * Service program call QTOCNETSTS
 */
public interface IQTOCNETSTS extends IJT400Program<QTOCNETSTS> {

	public static IQTOCNETSTS create(final AS400 as400) {
		return JT400ExtFactory.create(as400, IQTOCNETSTS.class);
	}

}
