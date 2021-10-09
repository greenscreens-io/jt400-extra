/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
 package io.greenscreens.jt400.programs.qsys.quslspl;

import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Ref;
import io.greenscreens.jt400.interfaces.IJT400Format;

 /**
  * 
  */
 @Id(value = 0)
 @JT400Format(length = 16, offset = 4)
 public class SPLF0200Field implements IJT400Format {

 	@JT400Format(offset = 0, type = AS400DataType.TYPE_BIN4)
 	public int dataLength;

 	@JT400Format(offset = 4, type = AS400DataType.TYPE_BIN4)
 	public int keyField;
 	
 	@JT400Format(offset = 8, length = 1)
 	public String type;

 	@JT400Format(offset = 9, length = 3)
 	public String reservedLen;
 	
 	@JT400Ref(length = 0, offset = -1)
 	@JT400Format(offset = 12)
 	public String data;

 	@JT400Ref(length = 9, offset = 0)
 	@JT400Format(offset = 12)
 	public String reserved;
 }
