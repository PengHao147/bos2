package com.itheima.bos.dao.base;

import com.itheima.bos.domain.base.Standard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 7911丶猪头肉 on 2018/8/14.
 */
public interface StandardRepository extends JpaRepository<Standard,Integer> {
    public List<Standard> findByName(String name);

    @Query(value = "from Standard where name=?")
    public List<Standard> queryName(String name);

    @Query(value = "update Standard set minLength=?2 where id=?1")
    @Modifying
    public void updateMinLength(Integer id,Integer minLength);
}
