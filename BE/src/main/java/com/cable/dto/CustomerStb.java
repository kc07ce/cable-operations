package com.cable.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerStb {
	
	private String matchString;
	private Customer customer;
	private Stb stb;
}
