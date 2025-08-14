package com.yuan.cloud.core.base.vo.page;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description 分页结果对象
 *
 * @author Mr.Y
 * Created on 2025-08-08 10:40
 */
@Data
@Builder
@Tag(name = "YaPageResult", description = "分页结果对象")
public class YaPageResult<T> implements Serializable {
    /**
     * 总记录数
     */
    @Schema(description = "总记录数")
    private long totalElements;
    /**
     * 总页数
     */
    @Schema(description = "总页数")
    private int totalPages;
    /**
     * 当前页
     */
    @Schema(description = "当前页")
    private int currentPage;
    /**
     * 每页记录数
     */
    @Schema(description = "每页记录数")
    private int size;
    /**
     * 当前页数据
     */
    @Schema(description = "当前页数据")
    private List<T> contents;
    /**
     * 是否有数据
     */
    @Schema(description = "是否有数据")
    private boolean hasContent;
}
