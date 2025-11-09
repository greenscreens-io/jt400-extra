/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400;

import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400Bin4;
import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.Input;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Ref;
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Error format for program calls
 * https://www.ibm.com/support/knowledgecenter/ssw_ibm_i_71/apiref/errorcodeformat.htm
 *
 */
@JT400Format(length = 32)
public final class ERRC0200 implements IJT400Format {

	@Id(0) @Input
	@JT400Format(type = AS400DataType.TYPE_BIN4, offset = 0)
	int key;

	@Id(1) @Input
	@JT400Format(type = AS400DataType.TYPE_BIN4, offset = 4)
	int bytesProvided;

	@Id(2) @Output
	@JT400Format(type = AS400DataType.TYPE_BIN4, offset = 8)
	int bytesAvailable;

	@Id(3) @Output
	@JT400Format(type = AS400DataType.TYPE_TEXT, offset = 12, length = 7)
	String exceptionID;

	@Id(4) @Output
	@JT400Format(type = AS400DataType.TYPE_TEXT, offset = 19, length = 1)
	String reserved;

	@Id(5) @Output
	@JT400Format(type = AS400DataType.TYPE_BIN4, offset = 20)
	int ccsid;

	@Id(6) @Output
	@JT400Format(type = AS400DataType.TYPE_BIN4, offset = 24)
	int exceptionOffset;

	@Id(7) @Output
	@JT400Format(type = AS400DataType.TYPE_BIN4, offset = 28)
	int exceptionLength;

	@Id(8) @Output
	@JT400Ref(offset = 24, length = 28)
	@JT400Format(type = AS400DataType.TYPE_TEXT)
	ByteBuffer data;

	@Override
	public String toString() {
		return "ERRC0200 [key=" + key + ", bytesProvided=" + bytesProvided + ", bytesAvailable=" + bytesAvailable
				+ ", exceptionID=" + exceptionID + ", reserved=" + reserved + ", ccsid=" + ccsid + ", exceptionOffset="
				+ exceptionOffset + ", exceptionLength=" + exceptionLength + ", data=" + data + "]";
	}

	/**
	 * Generate bytes for program error param
	 * @param size
	 * @return
	 */
	public static ByteBuffer toBuffer(final int key, final int size) {

		final AS400Bin4 bin4 = new AS400Bin4();
		final ByteBuffer buffer = ByteBuffer.allocate(size + 32);

		buffer.put(bin4.toBytes(key));
		buffer.put(bin4.toBytes(size));
		buffer.rewind();

		return buffer;
	}

}
