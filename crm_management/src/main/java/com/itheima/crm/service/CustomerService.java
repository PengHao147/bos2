package com.itheima.crm.service;

import com.itheima.crm.domain.Customer;

import javax.ws.rs.*;
import java.util.List;

/**
 * 客户操作
 */
public interface CustomerService  {

    // 查询未关联客户
    @Path("/unassociatedcustomers")
    @GET
    @Produces({"application/xml","application/json"})
    public List<Customer> queryUnassociatedCustomers();

    // 查询已关联客户
    @Path("/associatedcustomers/{fixedAreaId}")
    @GET
    @Consumes({"application/xml","application/json"})
    @Produces({"application/xml","application/json"})
    public List<Customer> queryAssociatedCustomers(@PathParam("fixedAreaId")  String fixedAreaId);

    // 关联客户
    @Path("/associationcustomerstofixedarea")
    @PUT
    @Consumes({"application/xml","application/json"})
    public void associationCustomersToFixedArea(@QueryParam("customerIdStr")  String customerIdStr , @QueryParam("fixedAreaId") String fixedAreaId);

    @Path("/customer")
    @POST
    @Consumes({ "application/xml", "application/json" })
    public void regist(Customer customer);

    @Path("/customer/telephone/{telephone}")
    @GET
    @Consumes({ "application/xml", "application/json" })
    public Customer findByTelephone(@PathParam("telephone") String telephone);

    @Path("/customer/updatetype/{telephone}")
    @GET
    public void updateType(@PathParam("telephone") String telephone);
}
