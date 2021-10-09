/*
 * Copyright (C) 2015, 2021 Green Screens Ltd.
 */
package io.greenscreens.jt400.programs.qsys.quslspl;

import java.nio.ByteBuffer;
import java.util.List;

import com.ibm.as400.access.AS400DataType;
import com.ibm.as400.access.ProgramParameter;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.Input;
import io.greenscreens.jt400.annotations.JT400Argument;
import io.greenscreens.jt400.annotations.JT400Program;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Params;

/**
 * List Spooled Files (QUSLSPL) API
 * https://www.ibm.com/docs/en/i/7.4?topic=ssw_ibm_i_74/apis/QUSLSPL.htm
 *
 */
@JT400Program(
		library = "QSYS",
		program = "QUSLSPL",
		arguments = 17,
		formats = {SPLF0100.class, SPLF0200.class, SPLF0300.class, SPLF0400.class}
		)
public class QUSLSPL implements IJT400Params {

	/**
	 * The user space that receives the generated list, and the library in which it is located. 
	 * The first 10 characters contain the user space name, and the second 10 characters contain the library name.
	 * 
	 * You can use these special values for the library name:
	 * 		*CURLIB	The job's current library
	 * 		*LIBL	The library list
	 */
	@Id(0) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, length = 20, pass = ProgramParameter.PASS_BY_VALUE)
	String userSpaceName;
	
	/**
	 * 
	 */
	@Id(1) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 8)
	String formatName = "SPLF0100";

	/**
	 * The name of the user whose spooled files are included in the list. 
	 * This parameter can be used in conjunction with the output queue and library name, form type, user-specified data, 
	 * auxiliary storage pool, job system name, starting spooled file create date, starting spooled file create time, 
	 * ending spooled file create date, and ending spooled file create timeparameters to return a partial list of all the spooled files. 
	 * The list of spooled files returned is sorted by status, output priority, date, and time. 
	 * It must be blank if the qualified job name parameter is specified.
	 * 
	 * A value must be specified if the qualified job name parameter is blank or not specified. Otherwise, message CPF34C6 or CPD34C6 is issued.
	 * 
	 * The possible special values are:
	 * 	*ALL	Files owned by all users
	 * 	*CURRENT	Files owned by the current user
	 */	
	@Id(2) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 10)
	String userName;
	
	/**
	 * The name of the output queue whose files are to be included in the list, and the library in which it is located. 
	 * The first 10 characters contain the output queue name, and the second 10 characters contain the library name. 
	 * This parameter can be used in conjunction with the user name, form type, user-specified data, auxiliary storage pool, 
	 * job system name, starting spooled file create date, starting spooled file create time, ending spooled file create date, 
	 * and ending spooled file create time parameters to return a partial list of all the spooled files. 
	 * 
	 * The list of spooled files returned is sorted by status, output priority, date, and time. 
	 * It must be blank if the qualified job name parameter is specified.
	 * 
	 * Note: A value must be specified if the qualified job name parameter is blank or not specified. Otherwise, message CPF34C6 or CPD34C6 is issued.
	 * 
	 * You can use this special value for the output queue name:
	 *	*ALL	Files on all output queues. When you use this value, the library name must be blanks.
	 *	Output queue name	The name of the output queue.
	 *
	 * You can use these special values for the library name:
	 *	*CURLIB	The job's current library
	 *	*LIBL	The library list
	 */
	@Id(3) @Input 
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 10)
	String outputQueueName;

	/**
	 * The form type whose files are included in the list. 
	 * The form type is the value specified on the form type parameter of the printer file. 
	 * This parameter can be used in conjunction with the user name, qualified output queue name, user-specified data, 
	 * auxiliary storage pool, job system name, starting spooled file create date, starting spooled file create time, 
	 * ending spooled file create date, and ending spooled file create time parameters to return a partial list of all the spooled files. 
	 * 
	 * The list of spooled files returned is sorted by status, output priority, date, and time. 
	 * It must be blank if the qualified job name parameter is specified.
	 * 
	 * The special values supported are:
	 * 	*ALL	Files for all form types
	 * 	*STD	Only files that specify the standard form type
	 */
	@Id(4) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 10)
	String formType;

	/**
	 * The user-specified data value whose files are to be included in the list. 
	 * This parameter can be used in conjunction with the user name, qualified output queue name, 
	 * form type, auxiliary storage pool, job system name, starting spooled file create date, 
	 * starting spooled file create time, ending spooled file create date, 
	 * and ending spooled file create time parameters to return a partial list of all the spooled files. 
	 * 
	 * The list of spooled files returned is sorted by status, output priority, date, and time. 
	 * It must be blank if the qualified job name parameter is specified.
	 * 
	 * The special value supported is:
	 * 	*ALL	Files with any user-specified data values
	 */
	@Id(5) @Input
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 10)
	String userSpecifiedData;

	/**
	 * The structure in which to return error information. 
	 * For the format of the structure, see Error code parameter. 
	 * If this parameter is omitted, diagnostic and escape messages are issued to the application.
	 */
	@Id(6) @Input @Output
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_REFERENCE)
	ByteBuffer errorCode;

	/**
	 * The qualified job name of the job whose spooled files are to be included in the list. 
	 * If the user name, the qualified output queue name, the form type, user-specified data, 
	 * or auxiliary storage pool is specified, this parameter must be blank. 
	 * Otherwise, message CPF34C2 is issued. The list of spooled files returned is sorted by spooled file number.
	 * 
	 * The qualified job name has three parts:
	 * 	 job name	CHAR(10). A specific job name, or the following special value:
	 * 					*	Current running job.
	 * 						The rest of the qualified job name parameter must be blank.
	 *   user name	CHAR(10). A specific user profile name, or blank when the job name is asterisk (*).
	 *   job number	CHAR(6). A specific job number, or blank when the job name is asterisk (*).
	 *   
	 *   If this parameter is omitted, the API assumes all blanks.
	 */
	@Id(7) @Input 
	@JT400Argument(type = AS400DataType.TYPE_BYTE_ARRAY, pass = ProgramParameter.PASS_BY_VALUE, length = 26)
	String jobName;

	/**
	 * The list of the fields to be returned in the SPLF0200 format. 
	 * By specifying a set of keys, only the fields whose keys are specified are in the returned format. 
	 * See Valid Keys for a list of valid keys. This field is only used for the SPLF0200 format.
	 * This field is ignored if the number of keys for fields to return parameter is 0.
	 */
	@Id(8) @Input 
	@JT400Argument(type = AS400DataType.TYPE_ARRAY, of = AS400DataType.TYPE_BIN4, pass = ProgramParameter.PASS_BY_VALUE)
	List<Integer> fields;

	/**
	 * The number of fields to return in the SPLF0200 format. 
	 * This indicates how many keys are in the array of the keys for the fields to return parameter. 
	 * This field is used for the SPLF0200 format only, and it must be 0 when the SPLF0100, SPLF0300, or SPLF0400 format is specified.
	 * 
	 * If this parameter is omitted, the API assumes 0.
	 */
	@Id(9) @Input 
	@JT400Argument(type = AS400DataType.TYPE_BIN4, pass = ProgramParameter.PASS_BY_VALUE)
	int fieldSize;

	/**
	 * The auxiliary storage pool whose spooled files are to be included in the list. 
	 * This parameter can be used in conjunction with the user name, qualified output queue name, 
	 * form type, user-specified data, job system name, starting spooled file create date, 
	 * starting spooled file create time, ending spooled file create date, 
	 * and ending spooled file create time parameters to return a partial list of all the spooled files. 
	 * 
	 * The list of spooled files returned is sorted by status, output priority, date, and time. 
	 * It must be blank if the qualified job name parameter is specified.
	 * 
	 * The valid values are:
	 * 		-1	*BLANK. No auxiliary storage pool is specified.
	 * 		0	*ALL. Spooled files in all auxiliary storage pools as specified in the Auxiliary storage pool device name parameter.
	 * 		1	*SYSTEM. Spooled files in the system auxiliary storage pool.
	 * 		-2	*ASPDEV. Spooled files in the auxiliary storage pool device specified on the Auxiliary storage pool device name parameter.
	 * 		2-255	User auxiliary storage pool (2-32), or a primary or a secondary auxiliary storage pool (33-255).
	 * 
	 * If this parameter is omitted, the API assumes 0 when the qualified job name parameter is blank or not specified. The API assumes -1 if the qualified job name parameter is not blank.
	 */
	@Id(10) @Input 
	@JT400Argument(type = AS400DataType.TYPE_BIN4, pass = ProgramParameter.PASS_BY_VALUE)
	int auxiliaryStoragePool; 

	/**
	 * The name of the system where the job, specified in the qualified job name parameter, was run. 
	 * This parameter can be used in conjunction with the user name, qualified output queue name, form type, 
	 * user-specified data, auxiliary storage pool, starting spooled file create date, starting spooled file create time, 
	 * ending spooled file create date, ending spooled file create time, 
	 * or qualified job name parameters to return a partial list of all the spooled files. 
	 * 
	 * The list of spooled files returned is sorted by status, output priority, date, and time.
	 * 
	 * The following special values are supported for this parameter:
	 * 		*ALL	The returned list is not to be filtered based on job system name.
	 * 		*CURRENT	Only spooled files created on the current system are to be returned.
	 * 		job-system-name	Only spooled files created on the system specified are to be returned.
	 */
	@Id(11) @Input 
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 8)
	String jobSystenName;

	@Id(12) @Input 
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 7)
	String startingSpooledFileCreateDate;
	
	@Id(13) @Input 
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 6)
	String startingSpooledFileCreateTime;

	@Id(14) @Input 
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 7)
	String endingSpooledFileCreateDate;
	
	@Id(15) @Input 
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 6)
	String endingSpooledFileCreateTime;
	
	@Id(16) @Input 
	@JT400Argument(type = AS400DataType.TYPE_TEXT, pass = ProgramParameter.PASS_BY_VALUE, length = 10)
	String auxiliaryStoragePoolDeviceName;

	QUSLSPL(final Builder builder) {
		this.userSpaceName = builder.userSpaceName;
		this.formatName = builder.format;
		this.userName = builder.userName;
		this.outputQueueName = builder.outputQueueName;
		this.formType = builder.formType;
		this.userSpecifiedData = builder.userSpecifiedData;
	}

	/**
	 * Creates builder to build {@link QUSLSPL}.
	 * @return created builder
	 */
	public static Builder builder() {
		return new Builder();
	}

}
