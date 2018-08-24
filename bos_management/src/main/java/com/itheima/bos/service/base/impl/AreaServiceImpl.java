package com.itheima.bos.service.base.impl;

import com.itheima.bos.dao.base.AreaRepository;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.service.base.AreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AreaServiceImpl implements AreaService {
    // 注入DAO
    @Autowired
    private AreaRepository areaRepository;

    @Override
    public void saveBatch(List<Area> list) {
            areaRepository.save(list);
    }

    @Override
    public Page<Area> findPageData(Specification<Area> specification, Pageable pageable) {
        return areaRepository.findAll(specification,pageable);
    }
}
