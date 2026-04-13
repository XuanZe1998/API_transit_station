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

@TableName("logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Log {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("token_key")
    private String tokenKey;

    @TableField("model")
    private String model;

    @TableField("prompt_tokens")
    private int promptTokens;

    @TableField("completion_tokens")
    private int completionTokens;

    @TableField("total_tokens")
    private int totalTokens;

    @TableField("cost")
    private long cost; // token-based cost

    @TableField("status")
    private String status; // SUCCESS, FAILED

    @TableField("created_at")
    private LocalDateTime createdAt;
}
