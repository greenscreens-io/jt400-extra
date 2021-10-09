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
  * Writer inform<tion
  */
 @Id(value = 0)
 @JT400Format(length = 20)
 public class FILTERINFO_OUTQ implements IJT400Format {
 	
	@Id(0)
	@JT400Format(type = AS400DataType.TYPE_TEXT, length = 10)
 	public String outqueue;
 	
	@Id(1)
	@JT400Format(type = AS400DataType.TYPE_TEXT, length = 10)
 	public String library;

 }
