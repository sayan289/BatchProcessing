package com.springbatch.config;

import com.springbatch.entity.Customer;
import com.springbatch.entity.CustomerDetails;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, CustomerDetails> {

    @Override
    public CustomerDetails process(Customer item) throws Exception {
        CustomerDetails customerDetails=new CustomerDetails();
        customerDetails.setId(item.getId());
        customerDetails.setDob(item.getDob());
        customerDetails.setEmail(item.getEmail());
        customerDetails.setCountry(item.getCountry());
        customerDetails.setGender(item.getGender());
        customerDetails.setContactNo(item.getContactNo());
        customerDetails.setFirstname(item.getFirstname());
        customerDetails.setLastname(item.getLastname());
        return customerDetails;
    }
}
