package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Standard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by 7911丶猪头肉 on 2018/8/14.
 */
public interface StandardService {
    public void save(Standard standard);

    Page<Standard> findPageData(Pageable pageable);

    List<Standard> findAll();
}
