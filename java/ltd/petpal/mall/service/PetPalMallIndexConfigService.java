package ltd.petpal.mall.service;

import ltd.petpal.mall.controller.vo.PetPalMallIndexConfigGoodsVO;
import ltd.petpal.mall.entity.IndexConfig;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;

import java.util.List;

public interface PetPalMallIndexConfigService {
    /**
     * 
     *
     * @param pageUtil
     * @return
     */
    PageResult getConfigsPage(PageQueryUtil pageUtil);

    String saveIndexConfig(IndexConfig indexConfig);

    String updateIndexConfig(IndexConfig indexConfig);

    IndexConfig getIndexConfigById(Long id);

    /**
     * Return a fixed number of home page configuration product objects (home page call)
     *
     * @param number
     * @return
     */
    List<PetPalMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number);

    Boolean deleteBatch(Long[] ids);
}
