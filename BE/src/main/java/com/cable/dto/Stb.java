package com.cable.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Stb {
	private Integer stbId;
	private String accountNo;
	private String stbNo;
	private String stbType;
	private String stbUrl;
	private String remarks;
}
