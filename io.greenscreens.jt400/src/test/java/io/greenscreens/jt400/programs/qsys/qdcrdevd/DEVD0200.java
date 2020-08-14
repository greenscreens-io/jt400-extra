/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
 package io.greenscreens.jt400.programs.qsys.qdcrdevd;

import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Ref;

// There are two ways to call to populate array
// 1. length should be big enough to fit array data or modes
// 2. call 2 times, first to get sizes, second with calculated size from data from 1st call.
@JT400Format(length = 268)
public class DEVD0200 extends DEVD0100 {

	// Offset to list of mode names
	@JT400Format(offset = 104, type = AS400DataType.TYPE_BIN4)
	protected int offsetModeNamesList;

	// Number of mode names
	@JT400Format(offset = 108, type = AS400DataType.TYPE_BIN4)
	protected int numberModeNames;

	// Entry length for list of mode names
	@JT400Format(offset = 112, type = AS400DataType.TYPE_BIN4)
	protected int lengthModeNames;

	// Offset to list of mode names
	@JT400Format(offset = 256, type = AS400DataType.TYPE_BIN4)
	protected int offsetModeNamesListActive;

	// Number of mode names
	@JT400Format(offset = 260, type = AS400DataType.TYPE_BIN4)
	protected int numberModeNamesActive;

	// Entry length for list of mode names
	@JT400Format(offset = 264, type = AS400DataType.TYPE_BIN4)
	protected int lengthModeNamesActive;

	// These fields repeat for each mode name
	@JT400Ref(length = 108, offset = 104)
	@JT400Format(type = AS400DataType.TYPE_STRUCTURE)
	Data_10_2 [] modes;

	// These fields repeat for each active mode
	@JT400Ref(length = 260, offset = 256)
	@JT400Format(type = AS400DataType.TYPE_STRUCTURE)
	DEVD0200ModesActive []  modesActive;
}
