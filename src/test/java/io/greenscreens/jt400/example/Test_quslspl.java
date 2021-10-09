package io.greenscreens.jt400.example;

import com.ibm.as400.access.AS400;

import io.greenscreens.jt400.ERRC0100;
import io.greenscreens.jt400.JT400ExtFactory;
import io.greenscreens.jt400.JT400ExtUtil;
import io.greenscreens.jt400.programs.qsys.quslspl.*;

public class Test {

	public static void main(String[] args) throws Exception {

		final AS400 as400 = new AS400(); 

		final String usrspc = JT400ExtUtil.pad("PRT01", "QTEMP");
		final String outq = JT400ExtUtil.pad("PRT01", "QGPL");
		final Builder builder = Builder.create(SPLF0300.class, usrspc, outq, 256);
		
		builder.withUserName("*ALL");
		
		final QUSLSPL qgyrprtl = builder.build();		
		IQUSLSPL.create(as400).call(qgyrprtl);
		
		final ERRC0100 err = JT400ExtFactory.build(as400, ERRC0100.class, qgyrprtl.errorCode);
		System.out.println(err);

		/*
		final int len = JT400ExtUtil.getFormatLength(SPLF0300.class);
		final byte [] data = new byte[len];
		final String path = QSYSObjectPathName.toPath("QTEMP", "PRT01", "*USRSPC");
		final UserSpace uspc = new UserSpace(as400, path);
		uspc.read(data, len);
		uspc.close();
		final SPLF0300 result = JT400ExtFactory.build(as400, SPLF0300.class, ByteBuffer.wrap(data));
		System.out.println(result);
		*/
		
	}

}
