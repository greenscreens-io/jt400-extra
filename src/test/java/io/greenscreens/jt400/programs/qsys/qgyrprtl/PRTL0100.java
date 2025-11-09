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
  * Writer information
  */
 @Id(value = 0)
 @JT400Format(length = 64)
 public class PRTL0100 implements IJT400Format {

 	@JT400Format(offset = 0, length = 10)
 	public String deviceName;

 	@JT400Format(offset = 10, length = 50)
 	public String description;

 	@JT400Format(offset = 60, type = AS400DataType.TYPE_BIN4)
 	public int overallStatus;

 }
