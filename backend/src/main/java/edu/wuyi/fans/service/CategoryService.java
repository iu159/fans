package edu.wuyi.fans.service;

import edu.wuyi.fans.model.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author fans <wuyi_edu@163.com>
 * @since 2020-11-10
 */
public interface CategoryService extends IService<Category> {

    List<Category> listCategory();
}
