package mrsssswan.mall.service.impl;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import mrsssswan.mall.commons.ServerResponse;
import mrsssswan.mall.dao.CategoryMapper;
import mrsssswan.mall.pojo.Category;
import mrsssswan.mall.service.ICategaoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service("iCategaoryService")
public class CategoryServiceImpl implements ICategaoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse addCategory(String categoryName, Integer parentId) {
        if(parentId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("添加商品参数错误");
        }
        Category category = new Category();
        category.setParentId(parentId);
        category.setName(categoryName);
        category.setStatus(true);
        int resultCount = categoryMapper.insert(category);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("添加成功");
        }
        return ServerResponse.createByErrorMessage("添加失败");
    }

    @Override
    public ServerResponse updateCategoryName(Integer categoryId, String categoryName) {
        if(categoryId == null || StringUtils.isBlank(categoryName)){
            return ServerResponse.createByErrorMessage("更新商品参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);
        if(resultCount > 0){
            return ServerResponse.createBySuccessMessage("更新成功");
        }
        return ServerResponse.createByErrorMessage("更新失败");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        List<Category> categories = categoryMapper.selectChildrenParallelCategory(categoryId);
        if(CollectionUtils.isEmpty(categories)){
            logger.info("未找到当前分类的子类");
        }
        return ServerResponse.createBySuccess(categories);
    }


    @Override
    public ServerResponse getCategoryAndChildrenDeepCategory(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();
        findAllChildrenDeepCategory(categorySet,categoryId);
        List<Integer> categoryList = Lists.newArrayList();
        if(categoryId!=null){
           for(Category categoryItem : categorySet){
               categoryList.add(categoryItem.getId());
           }
        }
        return ServerResponse.createBySuccess(categoryList);
    }

    /**
     * 获取某一父节点所有子商品的集合
     * @param categorySet 存放商品的集合
     * @param categoryId 父节点
     * @return
     */
    public Set<Category> findAllChildrenDeepCategory(Set<Category> categorySet,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //遍历子节点
        List<Category> categoryList = categoryMapper.selectChildrenParallelCategory(categoryId);
        for(Category categoryItem : categoryList){
            findAllChildrenDeepCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
