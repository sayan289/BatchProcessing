package com.springbatch.config;

import com.springbatch.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer,Customer> {
    @Override
    public Customer process(Customer item) throws Exception {
//        if(item.getCountry().equals("China"))
//            return item;
//        else
//            return null;
        return item;
    }
}
