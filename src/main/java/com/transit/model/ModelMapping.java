package com.transit.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@TableName("model_mappings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModelMapping {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("public_model_name")
    private String publicModelName; // What the user sends (e.g., "gpt-4")

    @TableField("channel_model_name")
    private String channelModelName; // What the provider expects (e.g., "gpt-4-0613")

    @TableField("channel_id")
    private Long channelId;

    @Builder.Default
    private int priority = 10; // To handle multiple channels supporting the same model

    @Builder.Default
    private boolean enabled = true;

    // Use a transient field for the Channel object if needed, 
    // or handle join manually in mapper.
    @TableField(exist = false)
    private Channel channel;
}
