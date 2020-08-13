package io.greenscreens.jt400.programs.qsys.qdcrdevd;

import io.greenscreens.jt400.annotations.JT400Format;

@JT400Format(length = 36)
public class DEVD0200ModesActive {

	@JT400Format(offset = 0, length = 10)
	String modeName;
	
	@JT400Format(offset = 10, length = 10)
	String jobName;
	
	@JT400Format(offset = 21, length = 10)
	String userName;
	
	@JT400Format(offset = 31, length = 6)
	String jobNumber;
	
}
