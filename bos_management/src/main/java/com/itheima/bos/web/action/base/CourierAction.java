package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;
import com.itheima.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class CourierAction extends BaseAction<Courier> {

    // 注入Service
    @Autowired
    private CourierService courierService;

    @Action(value = "courier_save",results = {@Result(name = "success",location = "./pages/base/courier.html",type = "redirect")})
    public String save(){
        courierService.save(model);
        return SUCCESS;
    }

    // 属性驱动
    private int page ;
    private int rows ;

    public void setPage(int page) {
        this.page = page;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    // 查询
    @Action(value = "courier_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        // 封装pageable对象
        Pageable pageable = new PageRequest(page-1 , rows);

        // 根据查询条件构造Specification 条件查询对象
        Specification<Courier> specification = new Specification<Courier>() {
            @Override
            // Root 用于获取属性字段，CriteriaQuery可以用于简单条件查询，CriteriaBuilder 用于构造复杂条件查询
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 当前查询Root根目录Courier
                List<Predicate> list = new ArrayList<>();

                // 单表查询 (查询当前对象对应的数据表
                if (StringUtils.isNotBlank(model.getCourierNum())){
                    // 进行快递员 工号查询
                    Predicate p1 = cb.equal(root.get("courierNum").as(String.class),model.getCourierNum());
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCompany())){
                    // 进行公司查询 模糊查询
                    Predicate p2 = cb.like(root.get("company").as(String.class),"%"+model.getCompany()+"%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(model.getType())){
                    // 进行快递员类型查询 等值查询
                    Predicate p3 = cb.equal(root.get("type").as(String.class),model.getType());
                    list.add(p3);
                }

                // 多表查询 (查询当前对象 关联对象 对应的数据表)
                Join<Object,Object> standardRoot = root.join("standard",JoinType.INNER);
                if (model.getStandard() != null && StringUtils.isNotBlank(model.getStandard().getName())){
                    // 进行收派标准名称模糊查询
                    Predicate p4 = cb.like(standardRoot.get("name").as(String.class),"%"+model.getName()+"%");
                    list.add(p4);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };

        // 调用业务层,返回Page
        Page<Courier> pageDate = courierService.findPageDate(specification,pageable);

        // 将返回page对象,转换datagrid需要格式
        Map<String,Object> result = new HashMap<>();
        result.put("total",pageDate.getTotalElements());
        result.put("rows",pageDate.getContent());

        // 将结果压入值栈顶部
        ActionContext.getContext().getValueStack().push(result);

        return SUCCESS;
    }

    // 属性驱动
    private String ids ;

    public void setIds(String ids) {
        this.ids = ids;
    }

    // 作废快递员
    @Action(value = "courier_delBatch",results = {@Result(name = "success",location = "./pages/base/courier.html",type = "redirect")})
    public String delBatch(){
        // 按","分隔ids
        String[] idArray = ids.split(",");

        // 调用业务层,批量作废
        courierService.delBatch(idArray);

        return SUCCESS;
    }

    // 还原快递员
    @Action(value = "courier_restoreBatch",results = {@Result(name = "success",location = "./pages/base/courier.html",type = "redirect")})
    public String restoreBatch(){
        // 按","分隔ids
        String[] idArray = ids.split(",");

        // 调用业务层,批量作废
        courierService.restoreBatch(idArray);

        return SUCCESS;
    }

    /**
     * 查找未关联定区的快递员
     */
    @Action(value = "courier_findnoassociation", results = {@Result(name = "success",type = "json")})
    public String findnoassociation(){
        // 调用业务层,查询未关联定区的快递员
        List<Courier> list = courierService.findNoAssociation();

        // 将查询到的快递员压入值栈顶部
        ActionContext.getContext().getValueStack().push(list);

        return SUCCESS;
    }
}
