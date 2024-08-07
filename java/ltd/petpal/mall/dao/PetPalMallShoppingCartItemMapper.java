package ltd.petpal.mall.dao;

import ltd.petpal.mall.entity.PetPalMallShoppingCartItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PetPalMallShoppingCartItemMapper {
    int deleteByPrimaryKey(Long cartItemId);

    int insert(PetPalMallShoppingCartItem record);

    int insertSelective(PetPalMallShoppingCartItem record);

    PetPalMallShoppingCartItem selectByPrimaryKey(Long cartItemId);

    PetPalMallShoppingCartItem selectByUserIdAndGoodsId(@Param("PetPalMallUserId") Long PetPalMallUserId, @Param("goodsId") Long goodsId);

    List<PetPalMallShoppingCartItem> selectByUserId(@Param("PetPalMallUserId") Long PetPalMallUserId, @Param("number") int number);

    int selectCountByUserId(Long PetPalMallUserId);

    int updateByPrimaryKeySelective(PetPalMallShoppingCartItem record);

    int updateByPrimaryKey(PetPalMallShoppingCartItem record);

    int deleteBatch(List<Long> ids);
}