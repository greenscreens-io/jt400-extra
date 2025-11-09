/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
package io.greenscreens.jt400.programs.qsys.qgyrprtl;

import java.util.Objects;

import com.ibm.as400.access.AS400DataType;
import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Ref;
import io.greenscreens.jt400.interfaces.IJT400Format;

 /**
  * Filter information
  */
 @Id(value = 0)
 @JT400Format(length = -1)
 public class FILTERINFO implements IJT400Format {

	@Id(0) @JT400Ref(length = 1)
	@JT400Format(type = AS400DataType.TYPE_BIN4)
 	public int numberOfPrinters;

	@Id(1)
	@JT400Format(type = AS400DataType.TYPE_STRUCTURE)
	public FILTERINFO_PRINTER [] printers;
	
	@Id(2) @JT400Ref(length = 3)
	@JT400Format(type = AS400DataType.TYPE_BIN4)
 	public int numberOfOutqs;

 	@Id(3)
 	@JT400Format(type = AS400DataType.TYPE_STRUCTURE)
 	public FILTERINFO_OUTQ [] outqs;

 	public void setPrinters(FILTERINFO_PRINTER...printers) {
 		this.printers = printers;
 		this.numberOfPrinters = Objects.isNull(printers) ? 0 : printers.length;
 	}
 	
 	public void setOutqs(FILTERINFO_OUTQ...outqs) {
 		this.outqs = outqs;
 		this.numberOfOutqs = Objects.isNull(outqs) ? 0 : outqs.length;
 	}
 }
