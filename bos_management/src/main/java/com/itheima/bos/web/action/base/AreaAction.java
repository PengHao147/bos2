package com.itheima.bos.web.action.base;

import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;
import com.itheima.bos.utils.PinYin4jUtils;
import com.itheima.bos.web.action.common.BaseAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParentPackage("json-default")
@Namespace("/")
@Controller
@Scope("prototype")
public class AreaAction extends BaseAction<Area> {

    // 注入业务层对象
    @Autowired
    private AreaService areaService;

    // 接收上传文件
    private File file;

    public void setFile(File file) {
        this.file = file;
    }

    @Action(value = "area_batchImport")
    public String batchImport() throws Exception {
        List<Area> list = new ArrayList<>();

        // 加载Excel文件对象
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(new FileInputStream(file));
        // 读取一个Sheet
        HSSFSheet sheet = hssfWorkbook.getSheetAt(0);
        // 读取Sheet的每一行
        for (Row cells : sheet) {
            // 第一行跳过
            if(cells.getRowNum() == 0){
                continue;
            }

            // 空行跳过
            if (cells.getCell(0) == null || StringUtils.isBlank(cells.getCell(0).getStringCellValue())){
                continue;
            }
            Area area =  new Area();
            area.setId(cells.getCell(0).getStringCellValue());
            area.setProvince(cells.getCell(1).getStringCellValue());
            area.setCity(cells.getCell(2).getStringCellValue());
            area.setDistrict(cells.getCell(3).getStringCellValue());
            area.setPostcode(cells.getCell(4).getStringCellValue());

            // 基于pinyin4j生成城市编码和简码
            String province = area.getProvince();
            String city = area.getCity();
            String district = area.getDistrict();

            province = province.substring(0,province.length()-1);
            city = city.substring(0,city.length()-1);
            district = district.substring(0,district.length()-1);

            // 简码
            String[] headArray = PinYin4jUtils.getHeadByString(province + city +district);
            StringBuffer sb = new StringBuffer();
            for (String headstr : headArray) {
                sb.append(headstr);
            }
            String shortcode = sb.toString();
            area.setShortcode(shortcode);

            // 城市编码
            String citycode = PinYin4jUtils.hanziToPinyin(city,"");
            area.setCitycode(citycode);
            list.add(area);
        }

        // 调用业务层
        areaService.saveBatch(list);

        return NONE ;
    }



    @Action(value = "area_pageQuery",results = {@Result(name = "success",type = "json")})
    public String pageQuery(){
        // 构造分页查询对象
        Pageable pageable = new PageRequest(page-1,rows);

        // 构造条件查询对象
        Specification<Area> specification = new Specification<Area>() {
            @Override
            public Predicate toPredicate(Root<Area> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 创建保存条件集合对象
                List<Predicate> list = new ArrayList<>();

                // 添加条件
                if (StringUtils.isNotBlank(model.getProvince())){
                    // 根据省份模糊查询
                    Predicate p1 = cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%");
                    list.add(p1);
                }
                if (StringUtils.isNotBlank(model.getCity())){
                    // 根据城市模糊查询
                    Predicate p2 = cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%");
                    list.add(p2);
                }
                if (StringUtils.isNotBlank(model.getDistrict())){
                    // 根据区域模糊查询
                    Predicate p3 = cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%");
                    list.add(p3);
                }
                return cb.and(list.toArray(new Predicate[0]));
            }
        };

        // 调用业务层完成查询
        Page<Area> pageData = areaService.findPageData(specification,pageable);

        pushPageDataToValueStack(pageData);
        return SUCCESS;
    }
}
