package io.greenscreens.jt400.programs.qsys.qdcrdevd;

import io.greenscreens.jt400.annotations.JT400Format;

@JT400Format(length = 12)
public class DEVD0200Modes {

	@JT400Format(offset = 0, length = 10)
	String modeName;
	
	@JT400Format(offset = 10, length = 2)
	String reserved;
}
