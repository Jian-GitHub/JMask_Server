package com.jian.mapper;

import com.jian.entity.CompareAppWeb;
import com.jian.entity.JMaskAppInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Jian Qi
 * @Date 2022/4/6 5:11 下午
 * @Description 功能对比表Mapper
 * @Version 1
 */
@Mapper
public interface JMask_Mapper {
    List<CompareAppWeb> selectCompareAppWebList();

    JMaskAppInfo selectJMaskAppInfo();
}
