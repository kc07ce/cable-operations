package com.cable.api;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cable.dto.Customer;
import com.cable.dto.CustomerStb;
import com.cable.dto.Stb;
import com.cable.dto.Transaction;
import com.cable.service.impl.CableServiceImpl;

@RestController
@RequestMapping(value = "/api/v1/cable")
public class CableController {

	@Autowired
	private CableServiceImpl cableService;
	
	@GetMapping(value = "/transactions")
	public List<Transaction> getTransactions(@RequestParam(required = false) Integer cid, @RequestParam(required = false) Integer stbId){
		int id = cid==null?stbId:cid;
		return cableService.getTransactions(id);
	}
	
	@CrossOrigin
	@GetMapping(value = "/search")
	public List<CustomerStb> searchCustStb(@RequestParam(required = true) String searchValue){
		List<CustomerStb> list = cableService.getCustStbLikeSearch(searchValue);
		list.parallelStream().forEach(e -> {
			if(e.getStb()!=null)
				e.getStb().setStbUrl(cableService.getUrlFromType(e.getStb().getStbType()));
		});
		return list;
	}
	
	@PostMapping(value = "/customer/add")
	public ResponseEntity<String> addCustomer(@RequestBody Customer requestBody){
		if(StringUtils.isBlank(requestBody.getName()) || StringUtils.isBlank(requestBody.getDoorNo()) || requestBody.getPhoneNo()==null)
			return new ResponseEntity<String>("Name or DoorNo or Phone Number is missing", HttpStatus.BAD_REQUEST);
		return cableService.addCustomer(requestBody);
	}
	
	@PostMapping(value = "/stb/add")
	public ResponseEntity<String> addStb(@RequestBody Stb requestBody){
		if(StringUtils.isBlank(requestBody.getAccountNo()) && StringUtils.isBlank(requestBody.getStbNo()))
			return new ResponseEntity<String>("Enter Account No or STB No", HttpStatus.BAD_REQUEST);
		if(StringUtils.isBlank(requestBody.getStbType()))
			return new ResponseEntity<String>("Enter STB type", HttpStatus.BAD_REQUEST);
		return cableService.addStb(requestBody);
	}
	
	
}
