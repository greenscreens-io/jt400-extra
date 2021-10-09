/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
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
  * Filter information
  */
 @Id(value = 0)
 @JT400Format(length = 80)
 public class OPENLIST implements IJT400Format {

	@JT400Format(offset = 0, type = AS400DataType.TYPE_BIN4)
 	public int totalRecords;

	@JT400Format(offset = 4, type = AS400DataType.TYPE_BIN4)
 	public int recordsReturned;
	
	@JT400Format(offset = 8, type = AS400DataType.TYPE_TEXT, length = 4)
 	public String requestHandle;

	@JT400Format(offset = 12, type = AS400DataType.TYPE_BIN4)
 	public int recordLength;
	
	@JT400Format(offset = 16, type = AS400DataType.TYPE_TEXT, length = 1)
 	public String informationCompleteIndicator;
	
	@JT400Format(offset = 17, type = AS400DataType.TYPE_TEXT, length = 13)
 	public String dateTimeCreated;
 	
	@JT400Format(offset = 30, type = AS400DataType.TYPE_TEXT, length = 1)
 	public String statusIndicator;

	@JT400Format(offset = 31, type = AS400DataType.TYPE_TEXT, length = 1)
 	public String reserved1;

	@JT400Format(offset = 32, type = AS400DataType.TYPE_BIN4)
 	public int lengthOfInformatioNReturned;

	@JT400Format(offset = 36, type = AS400DataType.TYPE_BIN4)
 	public int firstRecordInReceiverVariable;
	
	@JT400Format(offset = 40, type = AS400DataType.TYPE_TEXT, length = 40)
 	public String reserved12;
	
 }
