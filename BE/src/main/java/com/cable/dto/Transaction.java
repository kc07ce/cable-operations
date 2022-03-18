package com.cable.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Transaction {

	private String name;
	private String accountNo;
	private String stbNo;
	private Date createdOn;
	private int amount;
	private int paidAmount;
	private String collectedBy;
}
