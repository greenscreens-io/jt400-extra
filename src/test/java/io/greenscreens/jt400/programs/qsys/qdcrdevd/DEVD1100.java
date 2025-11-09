/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
 package io.greenscreens.jt400.programs.qsys.qdcrdevd;

import java.util.Arrays;

import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Ref;

// There are two ways to call to populate array
// 1. length should be big enough to fit array data
// 2. call 2 times, first to get sizes, second with calculated size from data from 1st call.
@Id(value = 0)
@JT400Format(length = 1712)
public class DEVD1100 extends DEVD0100 {

	// Offset to list of switched lines
	@JT400Format(offset = 920, type = AS400DataType.TYPE_BIN4)
	protected int offsetSwitchedLines;

	// Number of switched lines
	@JT400Format(offset = 924, type = AS400DataType.TYPE_BIN4)
	protected int lengthSwitchedLines;

	// Offset to list of user-defined options
	@JT400Format(offset = 964, type = AS400DataType.TYPE_BIN4)
	protected int offsetUserDefOpt;

	// Number of user-defined options
	@JT400Format(offset = 968, type = AS400DataType.TYPE_BIN4)
	protected int lengthUserDefOpt;

	// Offset to user-defined data
	@JT400Format(offset = 976, type = AS400DataType.TYPE_BIN4)
	protected int offsetUserDefData;

	// Length to user-defined data
	@JT400Format(offset = 980, type = AS400DataType.TYPE_BIN4)
	protected int lengthUserDefData;

	// Offset to list of publishing information
	@JT400Format(offset = 1520, type = AS400DataType.TYPE_BIN4)
	protected int offsetPubInfo;

	// Number of publishing list entries
	@JT400Format(offset = 1524, type = AS400DataType.TYPE_BIN4)
	protected int lengthPubInfo;

	@JT400Ref(offset = 920, length = 924)
	@JT400Format(type = AS400DataType.TYPE_STRUCTURE)
	Data_10_2 [] switchedLineName;

	@JT400Ref(offset = 964, length = 968)
	@JT400Format(type = AS400DataType.TYPE_STRUCTURE)
	Data_10_2 [] userDefOpt;

	@JT400Ref(offset = 976, length = 980)
	@JT400Format(type = AS400DataType.TYPE_STRUCTURE)
	Data_10_2 [] dataStreamSupp;

	@JT400Ref(offset = 1520, length = 1524)
	@JT400Format(type = AS400DataType.TYPE_STRUCTURE)
	DEVD1101PubEntry [] publishingEntry;

	@Override
	public String toString() {
		return super.toString() + "\n" +
		"DEVD1100 [offsetSwitchedLines=" + offsetSwitchedLines + ", lengthSwitchedLines=" + lengthSwitchedLines
				+ ", offsetUserDefOpt=" + offsetUserDefOpt + ", lengthUserDefOpt=" + lengthUserDefOpt
				+ ", offsetUserDefData=" + offsetUserDefData + ", lengthUserDefData=" + lengthUserDefData
				+ ", offsetPubInfo=" + offsetPubInfo + ", lengthPubInfo=" + lengthPubInfo + ", switchedLineName="
				+ Arrays.toString(switchedLineName) + ", userDefOpt=" + Arrays.toString(userDefOpt)
				+ ", dataStreamSupp=" + Arrays.toString(dataStreamSupp) + ", publishingEntry="
				+ Arrays.toString(publishingEntry) + "]";
	}

}
