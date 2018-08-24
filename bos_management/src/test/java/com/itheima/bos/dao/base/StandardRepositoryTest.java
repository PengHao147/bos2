package com.itheima.bos.dao.base;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class StandardRepositoryTest {
    @Autowired
    private StandardRepository standardRepository;

    @Test
    public void test1() {
        System.out.println(standardRepository.findByName("20-30公斤"));
    }

    @Test
    public void test2() {
        System.out.println(standardRepository.queryName("20-30公斤"));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void test3() {
        standardRepository.updateMinLength(1, 10);
    }
}

