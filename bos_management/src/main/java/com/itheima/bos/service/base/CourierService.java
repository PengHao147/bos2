package com.itheima.bos.service.base;

import com.itheima.bos.domain.base.Courier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface CourierService {
    public void save(Courier courier);

    Page<Courier> findPageDate(Specification<Courier> specification,Pageable pageable);

    public void delBatch(String[] idArray);

    public void restoreBatch(String[] idArray);

    List<Courier> findNoAssociation();
}
