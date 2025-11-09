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
import io.greenscreens.jt400.annotations.Output;
import io.greenscreens.jt400.interfaces.IJT400Format;

/**
 * Error format for program calls
 * https://www.ibm.com/support/knowledgecenter/ssw_ibm_i_71/apiref/errorcodeformat.htm
 * https://javadoc.midrange.com/jtopen/index.html?com/ibm/as400/access/NativeErrorCode0100Exception.html
 */
@JT400Format(length = 16)
public final class ERRC0100 implements IJT400Format {

	@Id(0) @Input
	@JT400Format(type = AS400DataType.TYPE_BIN4, offset = 0)
	int bytesProvided;

	@Id(1) @Output
	@JT400Format(type = AS400DataType.TYPE_BIN4, offset = 4)
	int bytesAvailable;

	@Id(2) @Output
	@JT400Format(type = AS400DataType.TYPE_TEXT, offset = 8, length = 7)
	String exceptionID;

	@Id(3) @Output
	@JT400Format(type = AS400DataType.TYPE_TEXT, offset = 15, length = 1)
	String reserved;

	@Id(4) @Output
	@JT400Format(type = AS400DataType.TYPE_TEXT, offset = 16)
	ByteBuffer data;

	@Override
	public String toString() {
		return "ERRC0100 [bytesProvided=" + bytesProvided + ", bytesAvailable=" + bytesAvailable + ", exceptionID="
				+ exceptionID + ", reserved=" + reserved + ", data=" + data + "]";
	}

	/**
	 * Generate bytes for program error param
	 * @param size
	 * @return
	 */
	public static ByteBuffer toBuffer(final int size) {

		final AS400Bin4 bin4 = new AS400Bin4();
		final ByteBuffer buffer = ByteBuffer.allocate(size + 16);

		buffer.put(bin4.toBytes(size));
		buffer.rewind();

		return buffer;
	}

}
