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
 @JT400Format(length = 88)
 public class SPLF0100 implements IJT400Format {

 	@JT400Format(offset = 0, type = AS400DataType.TYPE_TEXT, length = 10)
 	public String userName;

 	@JT400Format(offset = 10, type = AS400DataType.TYPE_TEXT, length = 10)
 	public String outputQueueName;

 	@JT400Format(offset = 20, type = AS400DataType.TYPE_TEXT, length = 10)
 	public String outputQueueLibrary;
 	
 	@JT400Format(offset = 30, type = AS400DataType.TYPE_TEXT, length = 10)
 	public String formType;

 	@JT400Format(offset = 40, type = AS400DataType.TYPE_TEXT, length = 10)
 	public String userSpecifiedData;
 	
 	@JT400Format(offset = 50, type = AS400DataType.TYPE_TEXT, length = 16)
 	public String internalJobIdentifier;

 	@JT400Format(offset = 66, type = AS400DataType.TYPE_TEXT, length = 16)
 	public String internalSpoolFileIdentifier;
 	
 	@JT400Format(offset = 82, type = AS400DataType.TYPE_TEXT, length = 2)
 	public String reserved;
 	
 	@JT400Format(offset = 84, type = AS400DataType.TYPE_BIN4)
 	public int auxiliaryStoragePool;

 }
