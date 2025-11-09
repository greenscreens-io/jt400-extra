/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
 package io.greenscreens.jt400.programs.qsys.quslspl;

 import com.ibm.as400.access.AS400DataType;

 import io.greenscreens.jt400.annotations.Id;
 import io.greenscreens.jt400.annotations.JT400Format;
 import io.greenscreens.jt400.interfaces.IJT400Format;

 /**
  * Writer inform<tion
  */
 @Id(value = 0)
 @JT400Format(length = 136)
 public class SPLF0300 implements IJT400Format {

 	@JT400Format(offset = 0, length = 10)
 	public String jobName;

 	@JT400Format(offset = 10, length = 10)
 	public String userName;
 	
 	@JT400Format(offset = 20, length = 6)
 	public String jobNumber;
 	
 	@JT400Format(offset = 26, length = 10)
 	public String spooledFileName;

 	@JT400Format(offset = 36, type = AS400DataType.TYPE_BIN4)
 	public int spooledFileNumber;

 	@JT400Format(offset = 40, type = AS400DataType.TYPE_BIN4)
 	public int spooledFileStatus;

 	@JT400Format(offset = 44, length = 7)
 	public String dateCreated;

 	@JT400Format(offset = 51, length = 6)
 	public String timeCreated;

 	@JT400Format(offset = 57, length = 1)
 	public String spooledFileSchedule;

 	@JT400Format(offset = 58, length = 10)
 	public String spooledFileSystemName;

 	@JT400Format(offset = 68, length = 10)
 	public String userSpecifiedData;

 	@JT400Format(offset = 78, length = 10)
 	public String formType;

 	@JT400Format(offset = 88, length = 10)
 	public String outputQueueName;

 	@JT400Format(offset = 98, length = 10)
 	public String outputQueueLibrary;
 	
 	@JT400Format(offset = 108, type = AS400DataType.TYPE_BIN4)
 	public int auxiliaryStoragePool;

 	@JT400Format(offset = 112, type = AS400DataType.TYPE_BIN4)
 	public int spoolFileSize;

 	@JT400Format(offset = 116, type = AS400DataType.TYPE_BIN4)
 	public int spoolFileSizeMultiplier;

 	@JT400Format(offset = 120, type = AS400DataType.TYPE_BIN4)
 	public int totalPages;

 	@JT400Format(offset = 124, type = AS400DataType.TYPE_BIN4)
 	public int copiesLeft;

 	@JT400Format(offset = 128, length = 1)
 	public String priority;

 	@JT400Format(offset = 129, length = 3)
 	public String reserved;

 	@JT400Format(offset = 132, type = AS400DataType.TYPE_BIN4)
 	public String internetPrintProtocolJobIdentifier;

 }
