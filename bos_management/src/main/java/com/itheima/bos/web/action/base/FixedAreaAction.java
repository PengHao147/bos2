package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.web.action.common.BaseAction;
import com.itheima.crm.domain.Customer;
import com.opensymphony.xwork2.ActionContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class FixedAreaAction extends BaseAction<FixedArea> {
    // 注入Service
    @Autowired
    private FixedAreaService fixedAreaService;

    // 保存定区
    @Action(value = "fixedArea_save",results = {@Result(name = "success",type = "redirect",location = "./pages/base/fixed_area.html")})
    public String saveFixedArea(){
        fixedAreaService.save(model);
        return SUCCESS;
    }

    // 分页条件查询
    @Action(value = "fixedArea_pageQuery" , results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        // 构造分页查询对象
        Pageable pageable = new PageRequest(page-1,rows);

        // 构造条件查询条件
        Specification<FixedArea> specification = new Specification<FixedArea>() {
            @Override
            public Predicate toPredicate(Root<FixedArea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();

                // 构造查询条件
                if (StringUtils.isNotBlank(model.getId())){
                    // 根据id精准查询
                    Predicate p1 = cb.equal(root.get("id").as(String.class), model.getId());
                    list.add(p1);
                }
                if(StringUtils.isNotBlank(model.getCompany())){
                    // 根据公司模糊查询
                    Predicate p2 = cb.like(root.get("company").as(String.class), "%"+model.getCompany()+"%");
                    list.add(p2);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };
        // 调用业务层
        Page<FixedArea> pageData = fixedAreaService.findPageData(specification,pageable);

        // 将结果押入值栈
        pushPageDataToValueStack(pageData);

        return SUCCESS;
    }

    /**
     * 查询未关联定区列表
     * @return
     */
    @Action(value = "fixedArea_queryUnassociatedCustomers" , results = {@Result(name = "success",type = "json")})
    public String queryUnassociatedCustomers(){
        // 使用webClient调用webService接口
        Collection<? extends Customer> collection = WebClient.create("http://localhost:8082/crm_management/services/customerService/unassociatedcustomers")
                .accept(MediaType.APPLICATION_JSON_TYPE).getCollection(Customer.class);

        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    /**
     * 查询关联当前定区列表
     */
    @Action(value = "fixedArea_queryAssociatedCustomers",results = {@Result(name = "success",type = "json")})
    public String queryAssociatedCustomers(){
        // 使用webClient调用webService接口
        Collection<? extends Customer> collection = WebClient.create("http://localhost:8082/crm_management/services/customerService/associatedcustomers/"+model.getId())
                .accept(MediaType.APPLICATION_JSON_TYPE).getCollection(Customer.class);
        ActionContext.getContext().getValueStack().push(collection);
        return SUCCESS;
    }

    // 属性驱动
    private String[] customerIds;

    public void setCustomerIds(String[] customerIds) {
        this.customerIds = customerIds;
    }

    // 关联客户到定区
    @Action(value = "fixedArea_associationCustomersToFixedArea", results = { @Result(name = "success", type = "redirect", location = "./pages/base/fixed_area.html") })
    public String associationCustomersToFixedArea() {

        System.out.println(111111111);
        String customerIdStr = StringUtils.join(customerIds, ",");

        WebClient.create(
                "http://localhost:8082/crm_management/services/customerService"
                        + "/associationcustomerstofixedarea?customerIdStr="
                        + customerIdStr + "&fixedAreaId=" + model.getId()).put(
                null);
        return SUCCESS;
    }


    // 属性驱动
    private Integer courierId;
    private Integer takeTimeId;

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public void setTakeTimeId(Integer takeTimeId) {
        this.takeTimeId = takeTimeId;
    }

    // 关联快递员到定区
    @Action(value = "fixedArea_associationCourierToFixedArea",results = {@Result(name = "success",type = "redirect",location = "./pages/base/fixed_area.html")})
    public String associationCourierToFixedArea(){
        // 调用业务层,定区关联快递员
        fixedAreaService.associationCourierToFixedArea(model,courierId,takeTimeId);
        return SUCCESS;
    }
}
