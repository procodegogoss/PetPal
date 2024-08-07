package ltd.petpal.mall.service.impl;

import ltd.petpal.mall.common.Constants;
import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.controller.vo.PetPalMallShoppingCartItemVO;
import ltd.petpal.mall.dao.PetPalMallGoodsMapper;
import ltd.petpal.mall.dao.PetPalMallShoppingCartItemMapper;
import ltd.petpal.mall.entity.PetPalMallGoods;
import ltd.petpal.mall.entity.PetPalMallShoppingCartItem;
import ltd.petpal.mall.service.PetPalMallShoppingCartService;
import ltd.petpal.mall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PetPalMallShoppingCartServiceImpl implements PetPalMallShoppingCartService {

    @Autowired
    private PetPalMallShoppingCartItemMapper PetPalMallShoppingCartItemMapper;

    @Autowired
    private PetPalMallGoodsMapper PetPalMallGoodsMapper;

    //todo modify cart items quantity
    @Override
    public String savePetPalMallCartItem(PetPalMallShoppingCartItem PetPalMallShoppingCartItem) {
        PetPalMallShoppingCartItem temp = PetPalMallShoppingCartItemMapper.selectByUserIdAndGoodsId(PetPalMallShoppingCartItem.getUserId(), PetPalMallShoppingCartItem.getGoodsId());
        if (temp != null) {
            //Modify the record if it already exists
            //todo count = tempCount + 1
            temp.setGoodsCount(PetPalMallShoppingCartItem.getGoodsCount());
            return updatePetPalMallCartItem(temp);
        }
        PetPalMallGoods PetPalMallGoods = PetPalMallGoodsMapper.selectByPrimaryKey(PetPalMallShoppingCartItem.getGoodsId());
        //no item 
        if (PetPalMallGoods == null) {
            return ServiceResultEnum.GOODS_NOT_EXIST.getResult();
        }
        int totalItem = PetPalMallShoppingCartItemMapper.selectCountByUserId(PetPalMallShoppingCartItem.getUserId());
        //more than maxium 
        if (totalItem > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //keep record 
        if (PetPalMallShoppingCartItemMapper.insertSelective(PetPalMallShoppingCartItem) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updatePetPalMallCartItem(PetPalMallShoppingCartItem PetPalMallShoppingCartItem) {
        PetPalMallShoppingCartItem PetPalMallShoppingCartItemUpdate = PetPalMallShoppingCartItemMapper.selectByPrimaryKey(PetPalMallShoppingCartItem.getCartItemId());
        if (PetPalMallShoppingCartItemUpdate == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        //more than maxium 
        if (PetPalMallShoppingCartItem.getGoodsCount() > Constants.SHOPPING_CART_ITEM_LIMIT_NUMBER) {
            return ServiceResultEnum.SHOPPING_CART_ITEM_LIMIT_NUMBER_ERROR.getResult();
        }
        //todo if count the same, not change 
        //todo cannot change if different userId
        PetPalMallShoppingCartItemUpdate.setGoodsCount(PetPalMallShoppingCartItem.getGoodsCount());
        PetPalMallShoppingCartItemUpdate.setUpdateTime(new Date());
        //change record 
        if (PetPalMallShoppingCartItemMapper.updateByPrimaryKeySelective(PetPalMallShoppingCartItemUpdate) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public PetPalMallShoppingCartItem getPetPalMallCartItemById(Long PetPalMallShoppingCartItemId) {
        return PetPalMallShoppingCartItemMapper.selectByPrimaryKey(PetPalMallShoppingCartItemId);
    }

    @Override
    public Boolean deleteById(Long PetPalMallShoppingCartItemId) {
        //todo cannot change if different userId
        return PetPalMallShoppingCartItemMapper.deleteByPrimaryKey(PetPalMallShoppingCartItemId) > 0;
    }

    @Override
    public List<PetPalMallShoppingCartItemVO> getMyShoppingCartItems(Long PetPalMallUserId) {
        List<PetPalMallShoppingCartItemVO> PetPalMallShoppingCartItemVOS = new ArrayList<>();
        List<PetPalMallShoppingCartItem> PetPalMallShoppingCartItems = PetPalMallShoppingCartItemMapper.selectByUserId(PetPalMallUserId, Constants.SHOPPING_CART_ITEM_TOTAL_NUMBER);
        if (!CollectionUtils.isEmpty(PetPalMallShoppingCartItems)) {
            //Query product information and perform data conversion
            List<Long> PetPalMallGoodsIds = PetPalMallShoppingCartItems.stream().map(PetPalMallShoppingCartItem::getGoodsId).collect(Collectors.toList());
            List<PetPalMallGoods> PetPalMallGoods = PetPalMallGoodsMapper.selectByPrimaryKeys(PetPalMallGoodsIds);
            Map<Long, PetPalMallGoods> PetPalMallGoodsMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(PetPalMallGoods)) {
                PetPalMallGoodsMap = PetPalMallGoods.stream().collect(Collectors.toMap(PetPalMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
            }
            for (PetPalMallShoppingCartItem PetPalMallShoppingCartItem : PetPalMallShoppingCartItems) {
                PetPalMallShoppingCartItemVO PetPalMallShoppingCartItemVO = new PetPalMallShoppingCartItemVO();
                BeanUtil.copyProperties(PetPalMallShoppingCartItem, PetPalMallShoppingCartItemVO);
                if (PetPalMallGoodsMap.containsKey(PetPalMallShoppingCartItem.getGoodsId())) {
                    PetPalMallGoods PetPalMallGoodsTemp = PetPalMallGoodsMap.get(PetPalMallShoppingCartItem.getGoodsId());
                    PetPalMallShoppingCartItemVO.setGoodsCoverImg(PetPalMallGoodsTemp.getGoodsCoverImg());
                    String goodsName = PetPalMallGoodsTemp.getGoodsName();
                    // the string is too long and exceeds the limit
                    if (goodsName.length() > 28) {
                        goodsName = goodsName.substring(0, 28) + "...";
                    }
                    PetPalMallShoppingCartItemVO.setGoodsName(goodsName);
                    PetPalMallShoppingCartItemVO.setSellingPrice(PetPalMallGoodsTemp.getSellingPrice());
                    PetPalMallShoppingCartItemVOS.add(PetPalMallShoppingCartItemVO);
                }
            }
        }
        return PetPalMallShoppingCartItemVOS;
    }
}
