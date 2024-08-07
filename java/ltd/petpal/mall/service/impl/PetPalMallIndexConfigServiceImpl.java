package ltd.petpal.mall.service.impl;

import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.controller.vo.PetPalMallIndexConfigGoodsVO;
import ltd.petpal.mall.dao.IndexConfigMapper;
import ltd.petpal.mall.dao.PetPalMallGoodsMapper;
import ltd.petpal.mall.entity.IndexConfig;
import ltd.petpal.mall.entity.PetPalMallGoods;
import ltd.petpal.mall.service.PetPalMallIndexConfigService;
import ltd.petpal.mall.util.BeanUtil;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetPalMallIndexConfigServiceImpl implements PetPalMallIndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private PetPalMallGoodsMapper goodsMapper;

    @Override
    public PageResult getConfigsPage(PageQueryUtil pageUtil) {
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigList(pageUtil);
        int total = indexConfigMapper.getTotalIndexConfigs(pageUtil);
        PageResult pageResult = new PageResult(indexConfigs, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveIndexConfig(IndexConfig indexConfig) {
        //if item exits?
        if (indexConfigMapper.insertSelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateIndexConfig(IndexConfig indexConfig) {
        //if item exits? 
        IndexConfig temp = indexConfigMapper.selectByPrimaryKey(indexConfig.getConfigId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        if (indexConfigMapper.updateByPrimaryKeySelective(indexConfig) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public IndexConfig getIndexConfigById(Long id) {
        return null;
    }

    @Override
    public List<PetPalMallIndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {
        List<PetPalMallIndexConfigGoodsVO> PetPalMallIndexConfigGoodsVOS = new ArrayList<>(number);
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);
        if (!CollectionUtils.isEmpty(indexConfigs)) {
            //get all goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());
            List<PetPalMallGoods> PetPalMallGoods = goodsMapper.selectByPrimaryKeys(goodsIds);
            PetPalMallIndexConfigGoodsVOS = BeanUtil.copyList(PetPalMallGoods, PetPalMallIndexConfigGoodsVO.class);
            for (PetPalMallIndexConfigGoodsVO PetPalMallIndexConfigGoodsVO : PetPalMallIndexConfigGoodsVOS) {
                String goodsName = PetPalMallIndexConfigGoodsVO.getGoodsName();
                String goodsIntro = PetPalMallIndexConfigGoodsVO.getGoodsIntro();
                // string is too long and the text exceeds the limit
                if (goodsName.length() > 30) {
                    goodsName = goodsName.substring(0, 30) + "...";
                    PetPalMallIndexConfigGoodsVO.setGoodsName(goodsName);
                }
                if (goodsIntro.length() > 22) {
                    goodsIntro = goodsIntro.substring(0, 22) + "...";
                    PetPalMallIndexConfigGoodsVO.setGoodsIntro(goodsIntro);
                }
            }
        }
        return PetPalMallIndexConfigGoodsVOS;
    }

    @Override
    public Boolean deleteBatch(Long[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //delete data 
        return indexConfigMapper.deleteBatch(ids) > 0;
    }
}
