package mrsssswan.mall.service;

import mrsssswan.mall.commons.ServerResponse;

public interface ICategaoryService {
   ServerResponse addCategory(String categoryName,  Integer parentId);
   ServerResponse updateCategoryName(Integer categoryId, String categoryName);
   ServerResponse getChildrenParallelCategory(Integer categoryId);
   ServerResponse getCategoryAndChildrenDeepCategory(Integer categoryId);
}
