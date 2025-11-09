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
 @JT400Format(length = 142)
 public class PRTL0200 implements IJT400Format {

 	@JT400Format(offset = 0, length = 10)
 	public String deviceName;

 	@JT400Format(offset = 10, length = 50)
 	public String description;

 	@JT400Format(offset = 60, type = AS400DataType.TYPE_BIN4)
 	public int overallStatus;

 	@JT400Format(offset = 64, type = AS400DataType.TYPE_BIN4)
 	public int deviceStatus;
 	
 	@JT400Format(offset = 68, length = 10)
 	public String outqName;
 	
 	@JT400Format(offset = 78, length = 10)
 	public String outqLibrary;

 	@JT400Format(offset = 88, length = 1)
 	public String outqStatus;

 	@JT400Format(offset = 89, length = 10)
 	public String writerName;

 	@JT400Format(offset = 99, length = 1)
 	public String writerStatus;
 	
 	@JT400Format(offset = 100, length = 1)
 	public String writerStarted;
 	
 	@JT400Format(offset = 101, length = 10)
 	public String formType;

 	@JT400Format(offset = 111, length = 10)
 	public String currentFileName;
 	
 	@JT400Format(offset = 121, length = 10)
 	public String currentFileuser;
 	
 	@JT400Format(offset = 131, length = 10)
 	public String currentFileuserSpecifiedData;
 	
 	@JT400Format(offset = 141, length = 1)
 	public String networkDirectoryPublishingStatus;
 }
