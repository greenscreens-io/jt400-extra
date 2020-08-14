package io.greenscreens.jt400.programs.qsys.qdcrdevd;

import com.ibm.as400.access.AS400DataType;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.interfaces.IJT400Format;

// There are two ways to call to populate array
// 1. length should be big enough to fit array data or modes
// 2. call 2 times, first to get sizes, second with calculated size from data from 1st call.
@JT400Format(length = 72)
public class DEVD1101PubEntry implements IJT400Format {

	@JT400Format(offset = 0, length = 10, type = AS400DataType.TYPE_TEXT)
	protected String duplexSupported;

	@JT400Format(offset = 10, length = 10, type = AS400DataType.TYPE_TEXT)
	protected String colorSupported;

	@JT400Format(offset = 20, type = AS400DataType.TYPE_BIN4)
	protected int pagesperMinuteBlack;

	@JT400Format(offset = 24, type = AS400DataType.TYPE_BIN4)
	protected int pagesperMinuteColor;

	@JT400Format(offset = 28, length = 30, type = AS400DataType.TYPE_TEXT)
	protected String location;

	@JT400Format(offset = 58, length = 2, type = AS400DataType.TYPE_TEXT)
	protected String reserved;

	// Offset to list of data stream formats supported
	@JT400Format(offset = 60, type = AS400DataType.TYPE_BIN4)
	protected int dataStreamOffset;

	// Number of data stream formats supported
	@JT400Format(offset = 64, type = AS400DataType.TYPE_BIN4)
	protected int dataStreamNumber;

	// Entry length for data stream format supported
	@JT400Format(offset = 68, type = AS400DataType.TYPE_BIN4)
	protected int dataStreamLength;

	@Override
	public String toString() {
		return "DEVD1101PubEntry [duplexSupported=" + duplexSupported + ", colorSupported=" + colorSupported
				+ ", pagesperMinuteBlack=" + pagesperMinuteBlack + ", pagesperMinuteColor=" + pagesperMinuteColor
				+ ", location=" + location + ", reserved=" + reserved + ", dataStreamOffset=" + dataStreamOffset
				+ ", dataStreamNumber=" + dataStreamNumber + ", dataStreamLength=" + dataStreamLength + "]";
	}

}
