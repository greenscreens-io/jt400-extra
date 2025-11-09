/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qtocnetsts;

import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * https://www.ibm.com/support/knowledgecenter/ssw_ibm_i_73/apis/qtocrtvtcpa.htm#TCPA0100
 */
@Id(value = 0)
@JT400Format(length = 140)
public class TCPA0100 implements IJT400Format {

	@JT400Format(offset = 0, type = AS400DataType.TYPE_BIN4)
	protected int bytesReturned;

	@JT400Format(offset = 4, type = AS400DataType.TYPE_BIN4)
	protected int bytesAvailable;

	// TCP/IPv4 stack status
	@JT400Format(offset = 8, type = AS400DataType.TYPE_BIN4)
	protected int stackStatus;

	// How long active
	@JT400Format(offset = 12, type = AS400DataType.TYPE_BIN4)
	protected int active;

	// When last started - date
	@JT400Format(offset = 16, length = 8, type = AS400DataType.TYPE_TEXT)
	protected String lastStartedDate;

	// When last started - time
	@JT400Format(offset = 24, length = 6, type = AS400DataType.TYPE_TEXT)
	protected String lastStartedTime;
	
	// When last end - date
	@JT400Format(offset = 30, length = 8, type = AS400DataType.TYPE_TEXT)
	protected String lastEndDate;

	// When last end - time
	@JT400Format(offset = 38, length = 6, type = AS400DataType.TYPE_TEXT)
	protected String lastEndTime;

	// Who last started - job name
	@JT400Format(offset = 44, length = 10, type = AS400DataType.TYPE_TEXT)
	protected String lastStartedJobName;

	// Who last started - job user
	@JT400Format(offset = 54, length = 10, type = AS400DataType.TYPE_TEXT)
	protected String lastStartedJobUser;
	
	// Who last started - job number
	@JT400Format(offset = 64, length = 6, type = AS400DataType.TYPE_TEXT)
	protected String lastStartedJobNumber;
	
	// Who last started - internal job identifier
	@JT400Format(offset = 70, length = 16, type = AS400DataType.TYPE_TEXT)
	protected String lastStartedJobIdentifier;

	// Who last ended - job name
	@JT400Format(offset = 86, length = 10, type = AS400DataType.TYPE_TEXT)
	protected String lastEndedJobName;

	// Who last ended - job user
	@JT400Format(offset = 96, length = 10, type = AS400DataType.TYPE_TEXT)
	protected String lastEndedJobUser;
	
	// Who last ended - job number
	@JT400Format(offset = 106, length = 6, type = AS400DataType.TYPE_TEXT)
	protected String lastEndedJobNumber;
	
	// Who last ended - internal job identifier
	@JT400Format(offset = 112, length = 16, type = AS400DataType.TYPE_TEXT)
	protected String lastEndedJobIdentifier;
	
	// Offset to additional information
	@JT400Format(offset = 128, type = AS400DataType.TYPE_BIN4)
	protected int additionalInfoOffset;

	// Length of additional information
	@JT400Format(offset = 132, type = AS400DataType.TYPE_BIN4)
	protected int additionalInfoLength;

	// Limited mode
	@JT400Format(offset = 136, type = AS400DataType.TYPE_BIN4)
	protected int limitedMode;

	@Override
	public String toString() {
		return "TCPA0100 [bytesReturned=" + bytesReturned + ", bytesAvailable=" + bytesAvailable + ", stackStatus="
				+ stackStatus + ", active=" + active + ", lastStartedDate=" + lastStartedDate + ", lastStartedTime="
				+ lastStartedTime + ", lastEndDate=" + lastEndDate + ", lastEndTime=" + lastEndTime
				+ ", lastStartedJobName=" + lastStartedJobName + ", lastStartedJobUser=" + lastStartedJobUser
				+ ", lastStartedJobNumber=" + lastStartedJobNumber + ", lastStartedJobIdentifier="
				+ lastStartedJobIdentifier + ", lastEndedJobName=" + lastEndedJobName + ", lastEndedJobUser="
				+ lastEndedJobUser + ", lastEndedJobNumber=" + lastEndedJobNumber + ", lastEndedJobIdentifier="
				+ lastEndedJobIdentifier + ", additionalInfoOffset=" + additionalInfoOffset + ", additionalInfoLength="
				+ additionalInfoLength + ", limitedMode=" + limitedMode + "]";
	}

}
