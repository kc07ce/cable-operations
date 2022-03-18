package com.cable.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.cable.dto.Customer;
import com.cable.dto.CustomerStb;
import com.cable.dto.Stb;
import com.cable.dto.Transaction;
import com.cable.service.CableService;

@Component
public class CableServiceImpl implements CableService{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public String getUrlFromType(String type) {
		String sql = "SELECT URL FROM STB_TYPE WHERE TYPE=?";
		return jdbcTemplate.queryForObject(sql, new Object[] {type}, String.class);
	}

	@Override
	public List<Transaction> getTransactions(int id) {
		String sql = "SELECT c.NAME as name, s.ACCOUNT_NO as accountNo, s.STB_NO as stbNo, "
				+ "t.CREATED_ON as createdOn, t.AMOUNT as amount, t.PAID_AMOUNT as paidAmount, t.COLLECTED_BY as collectedBy "
				+ "FROM TRANSACTIONS as t "
				+ "INNER JOIN STB as s ON t.STB_ID=s.STB_ID "
				+ "INNER JOIN CUSTOMERS as c ON c.CID=t.CID where t.STB_ID=?";
		return jdbcTemplate.query(sql, new Object[] {id}, new BeanPropertyRowMapper(Transaction.class));
	}

	@Override
	public ResponseEntity<String> addCustomer(Customer requestBody) {
		String sql = "INSERT INTO CUSTOMERS(NAME, DOOR_NO, PHONE_NO, TOTAL_BAL) values (?, ?, ?, ?)";
		try {
			jdbcTemplate.update(sql, new Object[] {requestBody.getName(), requestBody.getDoorNo(), requestBody.getPhoneNo(), requestBody.getBalance()});
		}catch (Exception e) {
			return new ResponseEntity<String>("Failed to add Customer. "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Successfully added Customer", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> addStb(Stb requestBody) {
		String sql = "insert into STB(ACCOUNT_NO, STB_NO, TYPE) values (?, ?, ?);";
		try {
			jdbcTemplate.update(sql, new Object[] {requestBody.getAccountNo(), requestBody.getStbNo(), requestBody.getStbType()});
		}catch (Exception e) {
			return new ResponseEntity<String>("Failed to add STB. "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Successfully added STB", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> editCustomer(CustomerStb requestBody) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> editStb(CustomerStb requestBody) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<String> assignStbToCustomer(CustomerStb requestBody) {
		
		return null;
	}

	@Override
	public List<CustomerStb> getCustStbLikeSearch(String searchString) {
		List<CustomerStb> finalList = new ArrayList<>();
		if(StringUtils.isBlank(searchString))
			return finalList;
		
		String searchValue = "%"+searchString+"%";
		
		//search in stb and fetch customers
		String sql = "SELECT STB_ID as STBID, ACCOUNT_NO as ACCOUNTNO, STB_NO as STBNO, TYPE as STBTYPE FROM STB WHERE (ACCOUNT_NO like ? OR STB_NO like ?) GROUP BY STB_ID";
		List<Stb> stbList1 = jdbcTemplate.query(sql, new Object[] {searchValue, searchValue}, new BeanPropertyRowMapper(Stb.class));
		final String sqlFetchCust = "SELECT c.CID, c.NAME, c.PHONE_NO as PHONENO, c.DOOR_NO as DOORNO, TOTAL_BAL as BALANCE FROM CUSTOMERS as c JOIN CUST_STB as cs "
				+ "ON c.CID=cs.CID WHERE cs.STB_ID=? AND cs.STATUS='CURRENT'";
		
		List<CustomerStb> list1 =  stbList1.parallelStream().map(e -> {
			
			List<Customer> custList = jdbcTemplate.query(sqlFetchCust, new Object[] {e.getStbId()}, new BeanPropertyRowMapper(Customer.class));
			CustomerStb custStb = new CustomerStb();
			custStb.setCustomer(custList.size()>0?custList.get(0):null);
			custStb.setStb(e);
			if(Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE).matcher(e.getAccountNo()).find())
				custStb.setMatchString(e.getAccountNo());
			else
				custStb.setMatchString(e.getStbNo());
			
			return custStb;
		}).collect(Collectors.toList());
		
		//search in customers and fetch stb
		sql = "SELECT CID, NAME, PHONE_NO as PHONENO, DOOR_NO as DOORNO, TOTAL_BAL as BALANCE FROM CUSTOMERS WHERE (NAME like ? OR DOOR_NO like ?) GROUP BY CID";
		List<Customer> custList = jdbcTemplate.query(sql, new Object[] {searchValue, searchValue}, new BeanPropertyRowMapper(Customer.class));
		final String sqlFetchStb = "SELECT s.STB_ID as STBID, s.ACCOUNT_NO as ACCOUNTNO, s.STB_NO as STBNO, s.TYPE as STBTYPE FROM STB as s JOIN CUST_STB as cs "
				+ "ON s.STB_ID=cs.STB_ID WHERE cs.CID=? AND cs.STATUS='CURRENT'";
		List<CustomerStb> list2 = custList.parallelStream().map(e -> {
			List<Stb> stbList = jdbcTemplate.query(sqlFetchStb, new Object[] {e.getCid()}, new BeanPropertyRowMapper<>(Stb.class));
			CustomerStb custStb = new CustomerStb();
			custStb.setCustomer(e);
			custStb.setStb(stbList.size()>0?stbList.get(0):null);
			if(Pattern.compile(Pattern.quote(searchString), Pattern.CASE_INSENSITIVE).matcher(e.getDoorNo()).find())
				custStb.setMatchString(e.getDoorNo());
			else
				custStb.setMatchString(e.getName());
			return custStb;
		}).collect(Collectors.toList());
		
		Set<String> foundList = new HashSet<>();
		for(CustomerStb custStb:list1) {
			if(!foundList.contains(custStb.getMatchString())) {
				finalList.add(custStb);
				foundList.add(custStb.getMatchString());
			}
		}
		for(CustomerStb custStb:list2) {
			if(!foundList.contains(custStb.getMatchString())) {
				finalList.add(custStb);
				foundList.add(custStb.getMatchString());
			}
		}
		return finalList;
	}

	@Override
	public List<CustomerStb> getCustStbHistory(int cid, int stbId) {
		// TODO Auto-generated method stub
		return null;
	}

}
