/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.quslspl;

import com.ibm.as400.access.AS400;

import io.greenscreens.jt400.JT400ExtFactory;
import io.greenscreens.jt400.interfaces.IJT400Program;

/**
 * The List Spooled Files (QUSLSPL) API is similar to the Work with Spooled Files (WRKSPLF) command or 
 * the Work with Job (WRKJOB OPTION(*SPLF)) command. 
 * The API generates a list of spooled files on the system and places the list in a user space. 
 * The list can include some of the following:
 * 
 * All spooled files
 * Spooled files of specific users or all users
 * Spooled files in a specified output queue or in all output queues
 * Spooled files for all form types or the standard form type
 * Spooled files that have any user-specified data values
 * Spooled files generated by a specific job
 * Spooled files stored in an auxiliary storage pool.
 * 
 * The generated list replaces any existing information in the user space.
 * 
 * https://www.ibm.com/docs/en/i/7.1?topic=ssw_ibm_i_71/apis/QUSLSPL.htm
 */
public interface IQUSLSPL extends IJT400Program<QUSLSPL> {

	public static IQUSLSPL create(final AS400 as400) {
		return JT400ExtFactory.create(as400, IQUSLSPL.class);
	}

}