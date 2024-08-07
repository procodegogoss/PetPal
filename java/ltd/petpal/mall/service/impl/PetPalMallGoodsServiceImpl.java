package ltd.petpal.mall.service.impl;

import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.controller.vo.PetPalMallSearchGoodsVO;
import ltd.petpal.mall.dao.PetPalMallGoodsMapper;
import ltd.petpal.mall.entity.PetPalMallGoods;
import ltd.petpal.mall.service.PetPalMallGoodsService;
import ltd.petpal.mall.util.BeanUtil;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PetPalMallGoodsServiceImpl implements PetPalMallGoodsService {

    @Autowired
    private PetPalMallGoodsMapper goodsMapper;

    @Override
    public PageResult getPetPalMallGoodsPage(PageQueryUtil pageUtil) {
        List<PetPalMallGoods> goodsList = goodsMapper.findPetPalMallGoodsList(pageUtil);
        int total = goodsMapper.getTotalPetPalMallGoods(pageUtil);
        PageResult pageResult = new PageResult(goodsList, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String savePetPalMallGoods(PetPalMallGoods goods) {
        if (goodsMapper.insertSelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public void batchSavePetPalMallGoods(List<PetPalMallGoods> PetPalMallGoodsList) {
        if (!CollectionUtils.isEmpty(PetPalMallGoodsList)) {
            goodsMapper.batchInsert(PetPalMallGoodsList);
        }
    }

    @Override
    public String updatePetPalMallGoods(PetPalMallGoods goods) {
        PetPalMallGoods temp = goodsMapper.selectByPrimaryKey(goods.getGoodsId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        goods.setUpdateTime(new Date());
        if (goodsMapper.updateByPrimaryKeySelective(goods) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public PetPalMallGoods getPetPalMallGoodsById(Long id) {
        return goodsMapper.selectByPrimaryKey(id);
    }
    
    @Override
    public Boolean batchUpdateSellStatus(Long[] ids, int sellStatus) {
        return goodsMapper.batchUpdateSellStatus(ids, sellStatus) > 0;
    }

    @Override
    public PageResult searchPetPalMallGoods(PageQueryUtil pageUtil) {
        List<PetPalMallGoods> goodsList = goodsMapper.findPetPalMallGoodsListBySearch(pageUtil);
        int total = goodsMapper.getTotalPetPalMallGoodsBySearch(pageUtil);
        List<PetPalMallSearchGoodsVO> PetPalMallSearchGoodsVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(goodsList)) {
            PetPalMallSearchGoodsVOS = BeanUtil.copyList(goodsList, PetPalMallSearchGoodsVO.class);
            for (PetPalMallSearchGoodsVO PetPalMallSearchGoodsVO : PetPalMallSearchGoodsVOS) {
                String goodsName = PetPalMallSearchGoodsVO.getGoodsName();
                String goodsIntro = PetPalMallSearchGoodsVO.getGoodsIntro();
                // string is too long and the text exceeds the limit
                if (goodsName.length() > 28) {
                    goodsName = goodsName.substring(0, 28) + "...";
                    PetPalMallSearchGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 30) {
                    goodsIntro = goodsIntro.substring(0, 30) + "...";
                    PetPalMallSearchGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        PageResult pageResult = new PageResult(PetPalMallSearchGoodsVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }
}
