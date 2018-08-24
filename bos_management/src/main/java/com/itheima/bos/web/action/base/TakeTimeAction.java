package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;
import com.itheima.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class TakeTimeAction extends BaseAction<TakeTime> {
    @Autowired
    private TakeTimeService takeTimeService;

    @Action(value = "taketime_findAll", results = {@Result(name = "success",type = "json")})
    public String findAll(){
        List<TakeTime> list = takeTimeService.findAll();

        ActionContext.getContext().getValueStack().push(list);
        return SUCCESS;
    }
}
