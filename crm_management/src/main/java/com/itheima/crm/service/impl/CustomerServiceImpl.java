package com.itheima.crm.service.impl;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {
    // 注入Dao
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> queryUnassociatedCustomers() {
        return customerRepository.findByFixedAreaIdIsNull();
    }

    @Override
    public List<Customer> queryAssociatedCustomers(String fixedAreaId) {
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }

    @Override
    public void associationCustomersToFixedArea(String customerIdStr, String fixedAreaId) {
        // 解除已经关联的客户
        customerRepository.clearFixedAreaId(fixedAreaId);
        System.out.println(22222);

        // 切割字符串
        if (StringUtils.isBlank(customerIdStr)) {
            return;
        }
        String[] customerIds = customerIdStr.split(",");
        for (String customerId : customerIds) {
            Integer id = Integer.parseInt(customerId);
            customerRepository.updateFixedAreaId(fixedAreaId,id);
        }
    }

    @Override
    public void regist(Customer customer) {
        System.out.println(customer);
        customerRepository.save(customer);
    }

    @Override
    public Customer findByTelephone(String telephone) {
        return customerRepository.findByTelephone(telephone);
    }

    @Override
    public void updateType(String telephone) {
        customerRepository.updateType(telephone);
    }
}
