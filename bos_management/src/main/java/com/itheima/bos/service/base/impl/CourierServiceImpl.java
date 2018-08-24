package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.service.base.CourierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public  class CourierServiceImpl implements CourierService {
    // 注入Dao对象
    @Autowired
    private CourierRepository courierRepository;

    @Override
    public void save(Courier courier) {
        courierRepository.save(courier);
    }

    @Override
    public Page<Courier> findPageDate(Specification<Courier> specification, Pageable pageable) {
        return courierRepository.findAll(specification,pageable);
    }

    @Override
    public void delBatch(String[] idArray) {
        // 调用Dao实现 update修改操作,将deltag修改为1
        for (String s : idArray) {
            Integer id = Integer.parseInt(s);
            courierRepository.updateDelTag(id);
        }
    }

    @Override
    public void restoreBatch(String[] idArray) {
        // 调用Dao实现 update修改操作,将deltag修改为null
        for (String s : idArray) {
            Integer id = Integer.parseInt(s);
            courierRepository.restoreDelTag(id);
        }
    }

    @Override
    public List<Courier> findNoAssociation() {
        // 封装Specification
        Specification<Courier> specification =new Specification<Courier>() {
            @Override
            public Predicate toPredicate(Root<Courier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // 查询条件  判定列表size为空
                Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
                return p;
            }
        };
        return courierRepository.findAll(specification);
    }
}
