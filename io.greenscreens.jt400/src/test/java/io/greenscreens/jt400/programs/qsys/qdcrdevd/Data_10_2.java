/*
 * Copyright (C) 2015, 2020  Green Screens Ltd.
 *
 * https://www.greenscreens.io
 *
 */
 package io.greenscreens.jt400.programs.qsys.qdcrdevd;

import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.interfaces.IJT400Format;

@JT400Format(length = 12)
public class Data_10_2 implements IJT400Format {

	@JT400Format(offset = 0, length = 10)
	String name;

	@JT400Format(offset = 10, length = 2)
	String reserved;

	@Override
	public String toString() {
		return "Data_10_2 [name=" + name + ", reserved=" + reserved + "]";
	}

}
