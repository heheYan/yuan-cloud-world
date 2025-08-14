package com.yuan.cloud.core.base.api;

import cn.hutool.core.bean.BeanUtil;
import com.yuan.cloud.core.base.dto.AbstractBaseDTO;
import com.yuan.cloud.core.base.entity.AbstractBaseEntity;
import com.yuan.cloud.core.base.query.AbstractBaseQuery;
import com.yuan.cloud.core.base.query.YaPageQuery;
import com.yuan.cloud.core.base.service.IBaseService;
import com.yuan.cloud.core.base.vo.AbstractBaseVO;
import com.yuan.cloud.core.base.vo.page.YaPageResult;
import com.yuan.cloud.core.common.enums.YuanStatusEnum;
import com.yuan.cloud.core.common.exception.YuanApiException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description api接口父类
 *
 * @param <T> 数据库实体对象
 * @param <Q> 对应的查询对象
 * @param <D> 对应的DTO对象
 * @param <V> 对应的VO对象
 * @param <S> 对应的IService接口
 * @author Mr.Y
 * Created on 2025-08-07 17:05
 */
public abstract class AbstractBaseApi<
        T extends AbstractBaseEntity,
        Q extends AbstractBaseQuery,
        D extends AbstractBaseDTO,
        V extends AbstractBaseVO,
        S extends IBaseService<T, Q>> {

    protected final S baseService;

    public AbstractBaseApi(S baseService) {
        this.baseService = baseService;
    }

    @Operation(summary = "根据id查询对象信息", parameters = {@Parameter(name = "id", description = "对象id", required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "查询成功",
                    content = @Content(schema = @Schema(implementation = AbstractBaseVO.class)))
            })
    @GetMapping("/{id}")
    public V findById(@NotNull @PathVariable("id") Long id) {
        T t = baseService.findById(id);
        if (t == null) {
            throw new YuanApiException(YuanStatusEnum.NOT_FOUND);
        }
        return BeanUtil.copyProperties(t, getVoClass());
    }

    @Operation(summary = "查询所有对象信息",
            responses = {@ApiResponse(responseCode = "200", description = "查询列表成功",
                    content = @Content(schema = @Schema(implementation = List.class)))
            })
    @GetMapping
    public List<V> listAll() {
        return BeanUtil.copyToList(baseService.listAll(null), getVoClass());
    }

    @Operation(summary = "根据条件查询所有对象信息",
            parameters = {@Parameter(name = "condition", schema = @Schema(implementation = AbstractBaseQuery.class), description = "查询条件", required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "查询列表成功",
                    content = @Content(schema = @Schema(implementation = List.class)))
            })
    @PostMapping("/list")
    public List<V> listAll(@Nullable @RequestBody Q condition) {
        return BeanUtil.copyToList(baseService.listAll(condition), getVoClass());
    }

    @Operation(summary = "新增对象信息",
            parameters = {@Parameter(name = "dto", schema = @Schema(implementation = AbstractBaseDTO.class), description = "对象信息", required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "保存成功",
                    content = @Content(schema = @Schema(implementation = AbstractBaseVO.class)))
            })
    @PostMapping
    public V save(@Validated @RequestBody D dto) {
        return BeanUtil.copyProperties(baseService.save(BeanUtil.copyProperties(dto, getEntityClass())), getVoClass());
    }

    @Operation(summary = "更新对象信息",
            parameters = {@Parameter(name = "dto", schema = @Schema(implementation = AbstractBaseDTO.class), description = "对象信息", required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "更新成功",
                    content = @Content(schema = @Schema(implementation = AbstractBaseVO.class)))
            })
    @PutMapping
    public V update(@Validated @RequestBody D dto) {
        T t = baseService.update(BeanUtil.copyProperties(dto, getEntityClass()));
        return BeanUtil.copyProperties(t, getVoClass());
    }

    @Operation(summary = "根据id删除对象信息", parameters = {@Parameter(name = "id", description = "对象id", required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "删除成功", content = @Content(schema = @Schema(implementation = Boolean.class)))})
    @DeleteMapping("/{id}")
    public boolean delete(@NotNull @PathVariable("id") Long id) {
        return baseService.deleteById(id);
    }

    @Operation(summary = "分页查询对象信息",
            parameters = {@Parameter(name = "condition", schema = @Schema(implementation = YaPageQuery.class), description = "分页查询条件", required = true)},
            responses = {@ApiResponse(responseCode = "200", description = "分页查询成功",
                    content = @Content(schema = @Schema(implementation = YaPageResult.class)))
            })
    @PostMapping("/page")
    public YaPageResult<V> page(@RequestBody YaPageQuery<Q> condition) {
        Page<T> page = baseService.page(condition);
        List<V> contents = BeanUtil.copyToList(page.getContent(), getVoClass());
        return YaPageResult.<V>builder()
                .totalElements(page.getTotalElements())
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .size(page.getSize())
                .hasContent(page.hasContent())
                .contents(contents)
                .build();
    }

    protected abstract Class<T> getEntityClass();

    protected abstract Class<V> getVoClass();
}
