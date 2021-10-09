package io.greenscreens.jt400.example;

import com.ibm.as400.access.AS400;
import io.greenscreens.jt400.ERRC0100;
import io.greenscreens.jt400.JT400ExtFactory;
import io.greenscreens.jt400.programs.qsys.qgyrprtl.*;

public class Test {

	public static void main(String[] args) throws Exception {

		final AS400 as400 = new AS400(); 

		final FILTERINFO_PRINTER printer = new FILTERINFO_PRINTER();
		printer.printerName = "PRT01";
		
		final FILTERINFO info = new FILTERINFO();
		info.setPrinters(printer);
					
		// final ByteBuffer infoBuffer = JT400ExtFactory.build(as400, info);
		
		final Builder builder = Builder.create(as400, PRTL0200.class, 256);
		// builder.withFilterInfo(infoBuffer);
		builder.withFilterInfo(info);
		builder.withReceiveRecords(1);
		
		final QGYRPRTL qgyrprtl = builder.build();		
		IQGYRPRTL.create(as400).call(qgyrprtl);
		
		final OPENLIST openlist = JT400ExtFactory.build(as400, OPENLIST.class, qgyrprtl.listInformation);
		System.out.println(openlist);
		
		final PRTL0200 result = JT400ExtFactory.build(as400, PRTL0200.class, qgyrprtl.receiver);
		System.out.println(result);
		
		final ERRC0100 err = JT400ExtFactory.build(as400, ERRC0100.class, qgyrprtl.errorCode);
		System.out.println(err);
	}

}
