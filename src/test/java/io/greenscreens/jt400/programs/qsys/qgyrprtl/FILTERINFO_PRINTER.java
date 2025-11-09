/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400.programs.qsys.qgyrprtl;

import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Writer inform<tion
 */
@Id(value = 0)
@JT400Format(length = 12)
public class FILTERINFO_PRINTER implements IJT400Format {

	@Id(0)
	@JT400Format(offset = 0, type = AS400DataType.TYPE_TEXT, length = 10)
	public String printerName;

	@Id(1)
	@JT400Format(offset = 10, type = AS400DataType.TYPE_TEXT, length = 2)
	public String reserved;

}
