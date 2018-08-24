package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.Area;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area,String> {
    Page<Area> findAll(Specification<Area> specification, Pageable pageable);
}
