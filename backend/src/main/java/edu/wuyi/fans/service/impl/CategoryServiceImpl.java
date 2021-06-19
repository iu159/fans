package edu.wuyi.fans.service.impl;

import edu.wuyi.fans.model.entity.Category;
import edu.wuyi.fans.mapper.CategoryMapper;
import edu.wuyi.fans.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    @Cacheable(cacheNames = {"category"})
    public List<Category> listCategory() {
        return categoryMapper.selectList(null);
    }
}
