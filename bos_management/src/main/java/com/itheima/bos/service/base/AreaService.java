package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface AreaService {
    public void saveBatch(List<Area> list);

    Page<Area> findPageData(Specification<Area> specification, Pageable pageable);
}
