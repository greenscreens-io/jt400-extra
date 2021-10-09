/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
 package io.greenscreens.jt400.programs.qsys.qsprwtri;

 import com.ibm.as400.access.AS400DataType;

 import io.greenscreens.jt400.annotations.Id;
 import io.greenscreens.jt400.annotations.JT400Format;
 import io.greenscreens.jt400.interfaces.IJT400Format;

 /**
  * Writer inform<tion
  */
 @Id(value = 0)
 @JT400Format(length = 320)
 public class WTRI0100 implements IJT400Format {

 	@JT400Format(offset = 0, type = AS400DataType.TYPE_BIN4)
 	public int bytesReturned;

 	@JT400Format(offset = 4, type = AS400DataType.TYPE_BIN4)
 	public int bytesAvailable;

 	@JT400Format(offset = 8, length = 10)
 	public String startedByUser;

 	@JT400Format(offset = 18, length = 1)
 	public String writingStatus;

 	@JT400Format(offset = 19, length = 1)
 	public String waitingForMessageStatus;

 	@JT400Format(offset = 20, length = 1)
 	public String heldStatus;

 	@JT400Format(offset = 21, length = 1)
 	public String endPendingStatus;

 	@JT400Format(offset = 22, length = 1)
 	public String holdPendingStatus;
 	
 	@JT400Format(offset = 23, length = 1)
 	public String betweenFilesStatus;

 	@JT400Format(offset = 24, length = 1)
 	public String betweenCopiesStatus;

 	@JT400Format(offset = 25, length = 1)
 	public String waitingForDataStatus;

 	@JT400Format(offset = 26, length = 1)
 	public String waitingForDeviceStatus;

 	@JT400Format(offset = 27, length = 1)
 	public String onJobQueueStatus;

 	@JT400Format(offset = 28, length = 1)
 	public String typeOfWriter;

 	@JT400Format(offset = 29, length = 3)
 	public String reserved_1;
 	
 	@JT400Format(offset = 32, length = 10)
 	public String writerJobName;
 	
 	@JT400Format(offset = 42, length = 10)
 	public String writerJobUserName;
 	
 	@JT400Format(offset = 52, length = 6)
 	public String writerJobNumber;
 	
 	@JT400Format(offset = 58, length = 10)
 	public String printerDeviceType;
 	
 	@JT400Format(offset = 68, type = AS400DataType.TYPE_BIN4)
 	public int numberOfSeparators;

 	@JT400Format(offset = 72, type = AS400DataType.TYPE_BIN4)
 	public int drawerOfSeparators;
 	
 	@JT400Format(offset = 76, length = 10)
 	public String alignForms;
 
 	@JT400Format(offset = 86, length = 10)
 	public String outputQueueName;
 	
 	@JT400Format(offset = 96, length = 10)
 	public String outputQueueLibraryName;

 	@JT400Format(offset = 106, length = 1)
 	public String outputQueuestatus;
 
	@JT400Format(offset = 107, length = 1)
 	public String reserved_2;
 
 	@JT400Format(offset = 108, length = 10)
 	public String formType;

 	@JT400Format(offset = 118, length = 10)
 	public String messageOption;

 	@JT400Format(offset = 128, length = 10)
 	public String autoEndWriter;

 	@JT400Format(offset = 138, length = 10)
 	public String allowDirectPrinting;

 	@JT400Format(offset = 148, length = 10)
 	public String messageQueueName;

 	@JT400Format(offset = 158, length = 10)
 	public String messageQueueLibraryName;
 	
 	@JT400Format(offset = 168, length = 2)
 	public String reserved_3;

 	@JT400Format(offset = 170, length = 10)
 	public String changesTakeEffect;

 	@JT400Format(offset = 180, length = 10)
 	public String nextOutputQueueName;

 	@JT400Format(offset = 190, length = 10)
 	public String nextOutputQueueLibraryName;

 	@JT400Format(offset = 200, length = 10)
 	public String nextFormType;

 	@JT400Format(offset = 210, length = 10)
 	public String nextMessageOption;
 	
 	@JT400Format(offset = 220, type = AS400DataType.TYPE_BIN4)
 	public int nextFileSeparators;

 	@JT400Format(offset = 224, type = AS400DataType.TYPE_BIN4)
 	public int nextSeparatorDrawer;
 	
 	@JT400Format(offset = 228, length = 10)
 	public String spooledFileName;

 	@JT400Format(offset = 238, length = 10)
 	public String jobName;

 	@JT400Format(offset = 248, length = 10)
 	public String userName;
 	
 	@JT400Format(offset = 258, length = 6)
 	public String jobNumber;
 	
 	@JT400Format(offset = 264, type = AS400DataType.TYPE_BIN4)
 	public int spooledFileNumber;

 	@JT400Format(offset = 268, type = AS400DataType.TYPE_BIN4)
 	public int pageBeingWritten;
 	
 	@JT400Format(offset = 272, type = AS400DataType.TYPE_BIN4)
 	public int totalPages;

 	@JT400Format(offset = 276, type = AS400DataType.TYPE_BIN4)
 	public int copiesLeftToProduce;
 	
 	@JT400Format(offset = 280, type = AS400DataType.TYPE_BIN4)
 	public int totalCopies;
 	
 	@JT400Format(offset = 284, length = 4)
 	public String messageKey;

 	@JT400Format(offset = 288, length = 1)
 	public String initializePrinter;

 	@JT400Format(offset = 289, length = 10)
 	public String printerDeviceName;
 	
 	@JT400Format(offset = 299, length = 8)
 	public String jobSystemName;
 	
 	@JT400Format(offset = 307, length = 7)
 	public String spooledFileCreateDate;
 	
 	@JT400Format(offset = 314, length = 6)
 	public String spooledFileCreateTime;

 }
