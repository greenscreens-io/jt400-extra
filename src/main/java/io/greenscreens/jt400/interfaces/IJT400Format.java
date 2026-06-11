/*
 * Copyright (C) 2015, 2026 Green Screens Ltd.
 */
package io.greenscreens.jt400.interfaces;

import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400;

import io.greenscreens.jt400.JT400ExtFactory;

/**
 * Defines program call response struct
 */
public interface IJT400Format {

 	default ByteBuffer toBytes(final AS400 as400) throws Exception {
 		return JT400ExtFactory.build(as400, this);
 	}

}
