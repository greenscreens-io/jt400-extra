/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
 package io.greenscreens.jt400.programs.qsys.quslspl;

import java.util.List;

import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;

 /**
  * 
  */
 @Id(value = 0)
 @JT400Format(length = 20, offset = 88)
 public class SPLF0200 extends SPLF0100 {

 	@JT400Format(offset = 0, type = AS400DataType.TYPE_BIN4)
 	public int fieldsReturned;

 	@JT400Format(offset = 8, type = AS400DataType.TYPE_ARRAY)
 	public List<SPLF0200Field> fields;

 }
