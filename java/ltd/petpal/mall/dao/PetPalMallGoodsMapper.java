package ltd.petpal.mall.dao;

import ltd.petpal.mall.entity.PetPalMallGoods;
import ltd.petpal.mall.entity.StockNumDTO;
import ltd.petpal.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PetPalMallGoodsMapper {
    int deleteByPrimaryKey(Long goodsId);

    int insert(PetPalMallGoods record);

    int insertSelective(PetPalMallGoods record);

    PetPalMallGoods selectByPrimaryKey(Long goodsId);

    int updateByPrimaryKeySelective(PetPalMallGoods record);

    int updateByPrimaryKeyWithBLOBs(PetPalMallGoods record);

    int updateByPrimaryKey(PetPalMallGoods record);

    List<PetPalMallGoods> findPetPalMallGoodsList(PageQueryUtil pageUtil);

    int getTotalPetPalMallGoods(PageQueryUtil pageUtil);

    List<PetPalMallGoods> selectByPrimaryKeys(List<Long> goodsIds);

    List<PetPalMallGoods> findPetPalMallGoodsListBySearch(PageQueryUtil pageUtil);

    int getTotalPetPalMallGoodsBySearch(PageQueryUtil pageUtil);

    int batchInsert(@Param("PetPalMallGoodsList") List<PetPalMallGoods> PetPalMallGoodsList);

    int updateStockNum(@Param("stockNumDTOS") List<StockNumDTO> stockNumDTOS);

    int batchUpdateSellStatus(@Param("orderIds")Long[] orderIds,@Param("sellStatus") int sellStatus);

}