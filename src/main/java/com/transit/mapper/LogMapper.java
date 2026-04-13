package com.transit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.transit.model.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<Log> {
}
