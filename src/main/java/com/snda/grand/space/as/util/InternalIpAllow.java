package com.snda.grand.space.as.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.snda.grand.space.as.rest.util.Rule;

public class InternalIpAllow {
	
	private static final String COMMA = ",";
	private List<String> allowIpList;
	
	public InternalIpAllow(String allowIpString) {
		this.allowIpList = parseIpList(allowIpString);
	}
	
	private List<String> parseIpList(String ipListString) {
		List<String> ipList = null;
		if (ipListString != null) {
			ipList = Lists.newArrayList();
			String[] ips = StringUtils.split(ipListString, COMMA);
			for (String ip : ips) {
				if (Rule.checkIpv4(ip.trim())) {
					ipList.add(ip.trim());
				}
			}
		}
		return ipList;
	}

	public List<String> getAllowIpList() {
		return allowIpList;
	}
	
	public boolean contains(String ip) {
		return allowIpList.contains(ip);
	}

}
