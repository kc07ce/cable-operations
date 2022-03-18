package com.cable.service;


import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cable.dto.Customer;
import com.cable.dto.CustomerStb;
import com.cable.dto.Stb;
import com.cable.dto.Transaction;

public interface CableService {

	public List<CustomerStb> getCustStbHistory(int cid, int stbId);
	public List<Transaction> getTransactions(int id);
	public ResponseEntity<String> editCustomer(CustomerStb requestBody);
	public ResponseEntity<String> editStb(CustomerStb requestBody);
	public ResponseEntity<String> assignStbToCustomer(CustomerStb requestBody);
	ResponseEntity<String> addStb(Stb requestBody);
	ResponseEntity<String> addCustomer(Customer requestBody);
	List<CustomerStb> getCustStbLikeSearch(String searchValue);
}
