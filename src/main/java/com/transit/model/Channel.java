package com.transit.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@TableName("channels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String type; // e.g., "openai", "anthropic", "gemini", "deepseek"

    @TableField("base_url")
    private String baseUrl;

    @TableField("api_key")
    private String apiKey;

    private String models; // Comma separated list of models supported by this channel

    @Builder.Default
    private boolean enabled = true;

    @TableField("created_at")
    private LocalDateTime createdAt;
}
