package ltd.petpal.mall.service;

import ltd.petpal.mall.controller.vo.PetPalMallIndexCategoryVO;
import ltd.petpal.mall.controller.vo.SearchPageCategoryVO;
import ltd.petpal.mall.entity.GoodsCategory;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;

import java.util.List;

public interface PetPalMallCategoryService {
    /**
     * 
     *
     * @param pageUtil
     * @return
     */
    PageResult getCategorisPage(PageQueryUtil pageUtil);

    String saveCategory(GoodsCategory goodsCategory);

    String updateGoodsCategory(GoodsCategory goodsCategory);

    GoodsCategory getGoodsCategoryById(Long id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * 
     *
     * @return
     */
    List<PetPalMallIndexCategoryVO> getCategoriesForIndex();

    /**
     * 
     *
     * @param categoryId
     * @return
     */
    SearchPageCategoryVO getCategoriesForSearch(Long categoryId);

    /**
     * Get the classification list based on parentId and level
     *
     * @param parentIds
     * @param categoryLevel
     * @return
     */
    List<GoodsCategory> selectByLevelAndParentIdsAndNumber(List<Long> parentIds, int categoryLevel);
}
