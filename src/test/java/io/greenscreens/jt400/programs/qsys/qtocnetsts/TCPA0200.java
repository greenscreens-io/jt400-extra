/*
 * Copyright (C) 2015, 2025 Green Screens Ltd.
 * 
 * https://www.greenscreens.io
 * 
 */
package io.greenscreens.jt400.programs.qsys.qtocnetsts;

import java.nio.ByteBuffer;

import com.ibm.as400.access.AS400DataType;

import io.greenscreens.jt400.annotations.Id;
import io.greenscreens.jt400.annotations.JT400Format;
import io.greenscreens.jt400.annotations.JT400Ref;

/**
 * This structure starts from offset position defined by
 * received field values for offset and length.
 * Field offsets are calculated from that position.
 * 
 * Dynamic Proxy will take data slice from received program output bytes.
 * https://www.ibm.com/support/knowledgecenter/ssw_ibm_i_73/apis/qtocrtvtcpa.htm#TCPA0200
 */
@Id(value = 0)
@JT400Ref(offset = 128, length = 132) // this format is dynamic, starts from offset defined at 128
@JT400Format(length = 140 + 160) // TCPA0100 + 120 + dynamic param (last) 
public class TCPA0200 extends TCPA0100 {

	@JT400Format(offset = 0, type = AS400DataType.TYPE_BIN4)
	protected int ipDatagramForwrding;

	@JT400Format(offset = 4, type = AS400DataType.TYPE_BIN4)
	protected int udpCheksum;

	@JT400Format(offset = 8, type = AS400DataType.TYPE_BIN4)
	protected int logErrors;
	
	@JT400Format(offset = 12, type = AS400DataType.TYPE_BIN4)
	protected int ipSourceRouting;
	
	@JT400Format(offset = 16, type = AS400DataType.TYPE_BIN4)
	protected int tcpUrgentPointer;
	
	@JT400Format(offset = 20, type = AS400DataType.TYPE_BIN4)
	protected int ipRessemblyTimeout;
	
	@JT400Format(offset = 24, type = AS400DataType.TYPE_BIN4)
	protected int ipTimeToLive;
	
	@JT400Format(offset = 28, type = AS400DataType.TYPE_BIN4)
	protected int tcpKeepAlive;

	@JT400Format(offset = 32, type = AS400DataType.TYPE_BIN4)
	protected int tcpReceiveBuffer;

	@JT400Format(offset = 36, type = AS400DataType.TYPE_BIN4)
	protected int tcpSendBuffer;
	
	@JT400Format(offset = 40, type = AS400DataType.TYPE_BIN4)
	protected int arpCacheTimeout;
	
	@JT400Format(offset = 44, type = AS400DataType.TYPE_BIN4)
	protected int mtuPathDiscovery;
	
	@JT400Format(offset = 48, type = AS400DataType.TYPE_BIN4)
	protected int mtuDiscoveryInterval;
	
	@JT400Format(offset = 52, type = AS400DataType.TYPE_BIN4)
	protected int qosEnablement;
	
	@JT400Format(offset = 56, type = AS400DataType.TYPE_BIN4)
	protected int qosTimerResolution;
	
	@JT400Format(offset = 60, type = AS400DataType.TYPE_BIN4)
	protected int qosDataPathOptimization;
	
	@JT400Format(offset = 64, type = AS400DataType.TYPE_BIN4)
	protected int deadGatewayDetectionEnablement;
	
	@JT400Format(offset = 68, type = AS400DataType.TYPE_BIN4)
	protected int deadGatwayDetectionInterval;
	
	@JT400Format(offset = 72, type = AS400DataType.TYPE_BIN4)
	protected int tcpTimeWaitTimeout;
	
	@JT400Format(offset = 76, type = AS400DataType.TYPE_BIN4)
	protected int tcpR1RetransmissionCount;
	
	@JT400Format(offset = 80, type = AS400DataType.TYPE_BIN4)
	protected int tcpR2RetransmissionCount;
	
	@JT400Format(offset = 84, type = AS400DataType.TYPE_BIN4)
	protected int tcpMinimumRetransmissionTimeout;
	
	@JT400Format(offset = 88, type = AS400DataType.TYPE_BIN4)
	protected int tcpCloseConnectionMessage;
		
	@JT400Format(offset = 92, type = AS400DataType.TYPE_BIN4)
	protected int networkFileCacheEnablement;

	@JT400Format(offset = 96, type = AS400DataType.TYPE_BIN4)
	protected int networkFileCacheTimeout;
	
	@JT400Format(offset = 100, type = AS400DataType.TYPE_BIN4)
	protected int networkFileCacheSize;
	
	@JT400Format(offset = 104, type = AS400DataType.TYPE_BIN4)
	protected int explicitCongnestionNotification;
	
	@JT400Format(offset = 108, length = 4, type = AS400DataType.TYPE_TEXT)
	protected String reserved;
	
	@JT400Format(offset = 112, type = AS400DataType.TYPE_BIN4)
	protected int dhcpUIDOffset;
	
	@JT400Format(offset = 116, type = AS400DataType.TYPE_BIN4)
	protected int dhcpUIDLength;
	
	@JT400Ref(length = 116, offset = 112)
	@JT400Format(offset = 120, type = AS400DataType.TYPE_BYTE_ARRAY)
	protected ByteBuffer dhcpUID;

	@Override
	public String toString() {
		return super.toString() + "\n" +
		 "TCPA0200 [ipDatagramForwrding=" + ipDatagramForwrding + ", udpCheksum=" + udpCheksum + ", logErrors="
				+ logErrors + ", ipSourceRouting=" + ipSourceRouting + ", tcpUrgentPointer=" + tcpUrgentPointer
				+ ", ipRessemblyTimeout=" + ipRessemblyTimeout + ", ipTimeToLive=" + ipTimeToLive + ", tcpKeepAlive="
				+ tcpKeepAlive + ", tcpReceiveBuffer=" + tcpReceiveBuffer + ", tcpSendBuffer=" + tcpSendBuffer
				+ ", arpCacheTimeout=" + arpCacheTimeout + ", mtuPathDiscovery=" + mtuPathDiscovery
				+ ", mtuDiscoveryInterval=" + mtuDiscoveryInterval + ", qosEnablement=" + qosEnablement
				+ ", qosTimerResolution=" + qosTimerResolution + ", qosDataPathOptimization=" + qosDataPathOptimization
				+ ", deadGatewayDetectionEnablement=" + deadGatewayDetectionEnablement
				+ ", deadGatwayDetectionInterval=" + deadGatwayDetectionInterval + ", tcpTimeWaitTimeout="
				+ tcpTimeWaitTimeout + ", tcpR1RetransmissionCount=" + tcpR1RetransmissionCount
				+ ", tcpR2RetransmissionCount=" + tcpR2RetransmissionCount + ", tcpMinimumRetransmissionTimeout="
				+ tcpMinimumRetransmissionTimeout + ", tcpCloseConnectionMessage=" + tcpCloseConnectionMessage
				+ ", networkFileCacheEnablement=" + networkFileCacheEnablement + ", networkFileCacheTimeout="
				+ networkFileCacheTimeout + ", networkFileCacheSize=" + networkFileCacheSize
				+ ", explicitCongnestionNotification=" + explicitCongnestionNotification + ", reserved=" + reserved
				+ ", dhcpUIDOffset=" + dhcpUIDOffset + ", dhcpUIDLength=" + dhcpUIDLength + ", dhcpUID=" + dhcpUID
				+ "]";
	}

}
