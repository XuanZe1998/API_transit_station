package com.transit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.transit.dto.PublicModel;
import com.transit.model.ModelMapping;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ModelMappingMapper extends BaseMapper<ModelMapping> {
    
    @Select("SELECT mm.*, c.name as channel_name, c.base_url, c.api_key " +
            "FROM model_mappings mm " +
            "LEFT JOIN channels c ON mm.channel_id = c.id " +
            "WHERE mm.public_model_name = #{publicModelName} AND mm.enabled = 1 " +
            "ORDER BY mm.priority DESC")
    List<ModelMapping> findByPublicModelNameWithChannel(@Param("publicModelName") String publicModelName);

    @Select("SELECT DISTINCT mm.public_model_name AS publicName, c.type AS type " +
            "FROM model_mappings mm " +
            "LEFT JOIN channels c ON mm.channel_id = c.id " +
            "WHERE mm.enabled = 1 AND c.enabled = 1")
    List<PublicModel> findPublicModels();

    @Select("SELECT COUNT(1) " +
            "FROM (SELECT DISTINCT mm.public_model_name " +
            "      FROM model_mappings mm " +
            "      LEFT JOIN channels c ON mm.channel_id = c.id " +
            "      WHERE mm.enabled = 1 AND c.enabled = 1 " +
            "        AND (#{type} IS NULL OR c.type = #{type}) " +
            "        AND (#{query} IS NULL OR mm.public_model_name LIKE CONCAT('%', #{query}, '%'))) t")
    Long countPublicModels(@Param("query") String query, @Param("type") String type);

    @Select("SELECT t.public_model_name AS publicName, t.type AS type FROM (" +
            "   SELECT DISTINCT mm.public_model_name, c.type " +
            "   FROM model_mappings mm " +
            "   LEFT JOIN channels c ON mm.channel_id = c.id " +
            "   WHERE mm.enabled = 1 AND c.enabled = 1 " +
            "     AND (#{query} IS NULL OR mm.public_model_name LIKE CONCAT('%', #{query}, '%'))" +
            ") t LIMIT #{limit} OFFSET #{offset}")
    List<PublicModel> findPublicModelsPaged(@Param("query") String query, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select("SELECT t.public_name AS publicName, t.type AS type FROM (" +
            "  SELECT mm.public_model_name AS public_name, " +
            "         MAX(c.type) AS type, " +
            "         COALESCE(MAX(lg.logs_count), 0) AS hot, " +
            "         MAX(lg.last_used) AS last_used " +
            "  FROM model_mappings mm " +
            "  LEFT JOIN channels c ON mm.channel_id = c.id " +
            "  LEFT JOIN (SELECT model, COUNT(*) AS logs_count, MAX(created_at) AS last_used FROM logs GROUP BY model) lg " +
            "    ON lg.model = mm.public_model_name " +
            "  WHERE mm.enabled = 1 AND c.enabled = 1 " +
            "    AND (#{type} IS NULL OR c.type = #{type}) " +
            "    AND (#{query} IS NULL OR mm.public_model_name LIKE CONCAT('%', #{query}, '%')) " +
            "  GROUP BY mm.public_model_name " +
            ") t ORDER BY t.hot DESC, t.public_name ASC LIMIT #{limit} OFFSET #{offset}")
    List<PublicModel> findPublicModelsPagedHot(@Param("query") String query, @Param("type") String type, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select("SELECT t.public_name AS publicName, t.type AS type FROM (" +
            "  SELECT mm.public_model_name AS public_name, " +
            "         MAX(c.type) AS type, " +
            "         MAX(lg.last_used) AS last_used " +
            "  FROM model_mappings mm " +
            "  LEFT JOIN channels c ON mm.channel_id = c.id " +
            "  LEFT JOIN (SELECT model, MAX(created_at) AS last_used FROM logs GROUP BY model) lg " +
            "    ON lg.model = mm.public_model_name " +
            "  WHERE mm.enabled = 1 AND c.enabled = 1 " +
            "    AND (#{type} IS NULL OR c.type = #{type}) " +
            "    AND (#{query} IS NULL OR mm.public_model_name LIKE CONCAT('%', #{query}, '%')) " +
            "  GROUP BY mm.public_model_name " +
            ") t ORDER BY (t.last_used IS NULL), t.last_used DESC, t.public_name ASC LIMIT #{limit} OFFSET #{offset}")
    List<PublicModel> findPublicModelsPagedRecent(@Param("query") String query, @Param("type") String type, @Param("limit") Integer limit, @Param("offset") Integer offset);

    @Select("SELECT t.public_name AS publicName, t.type AS type FROM (" +
            "  SELECT mm.public_model_name AS public_name, MAX(c.type) AS type " +
            "  FROM model_mappings mm " +
            "  LEFT JOIN channels c ON mm.channel_id = c.id " +
            "  WHERE mm.enabled = 1 AND c.enabled = 1 " +
            "    AND (#{type} IS NULL OR c.type = #{type}) " +
            "    AND (#{query} IS NULL OR mm.public_model_name LIKE CONCAT('%', #{query}, '%')) " +
            "  GROUP BY mm.public_model_name " +
            ") t ORDER BY t.public_name ASC LIMIT #{limit} OFFSET #{offset}")
    List<PublicModel> findPublicModelsPagedByName(@Param("query") String query, @Param("type") String type, @Param("limit") Integer limit, @Param("offset") Integer offset);
}
