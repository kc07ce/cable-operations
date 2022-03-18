package com.cable.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Customer {
	private Integer cid;
	private String name;
	private String doorNo;
	private Long phoneNo;
	private Integer balance;
}
