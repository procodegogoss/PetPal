package ltd.petpal.mall.service;

import ltd.petpal.mall.entity.PetPalMallGoods;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;

import java.util.List;

public interface PetPalMallGoodsService {
    /**
     * backend util
     *
     * @param pageUtil
     * @return
     */
    PageResult getPetPalMallGoodsPage(PageQueryUtil pageUtil);

    /**
     * add items
     *
     * @param goods
     * @return
     */
    String savePetPalMallGoods(PetPalMallGoods goods);

    /**
     * add items in lists 
     *
     * @param PetPalMallGoodsList
     * @return
     */
    void batchSavePetPalMallGoods(List<PetPalMallGoods> PetPalMallGoodsList);

    /**
     * change item data 
     *
     * @param goods
     * @return
     */
    String updatePetPalMallGoods(PetPalMallGoods goods);

    /**
     * get item details 
     *
     * @param id
     * @return
     */
    PetPalMallGoods getPetPalMallGoodsById(Long id);

    /**
     * Modify sales status in batches (on and off shelves)
     *
     * @param ids
     * @return
     */
    Boolean batchUpdateSellStatus(Long[] ids,int sellStatus);

    /**
     * search items 
     *
     * @param pageUtil
     * @return
     */
    PageResult searchPetPalMallGoods(PageQueryUtil pageUtil);
}
