package com.transit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.transit.model.Token;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenMapper extends BaseMapper<Token> {
}
