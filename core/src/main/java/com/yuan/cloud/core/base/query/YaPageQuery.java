package com.yuan.cloud.core.base.query;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;

import java.io.Serializable;

/**
 * Description 通用分页查询对象
 *
 * @author Mr.Y
 * Created on 2025-08-08 10:17
 */
@Data
@Tag(name = "分页查询对象")
public class YaPageQuery<Q extends AbstractBaseQuery> implements Serializable {
    /**
     * 当前页码， 默认第一页
     */
    @Schema(description = "当前页码,默认从1开始")
    private int currentPage = 1;

    /**
     * 每页条数， 默认每页10条
     */
    @Schema(description = "每页条数,默认10条")
    private int pageSize = 10;

    /**
     * 排序字段, 默认为更新时间
     */
    @Schema(description = "排序字段,默认更新时间")
    private String sortBy = "updatedAt";

    /**
     * 排序方式是否升序， 默认降序
     */
    @Schema(description = "排序方式是否升序,默认降序")
    private Boolean isAsc = false;

    /**
     * 查询条件
     */
    @Schema(description = "查询条件", implementation = AbstractBaseQuery.class)
    private Q query;

    /**
     * 获取当前页码, 同步前端传参1，2，3... 默认第一页从0开始
     *
     * @return 当前页码
     */
    public int getCurrentPage() {
        return Math.max(0, currentPage - 1);
    }
}
