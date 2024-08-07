package ltd.petpal.mall.service.impl;

import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.controller.vo.PetPalMallIndexCarouselVO;
import ltd.petpal.mall.dao.CarouselMapper;
import ltd.petpal.mall.entity.Carousel;
import ltd.petpal.mall.service.PetPalMallCarouselService;
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
public class PetPalMallCarouselServiceImpl implements PetPalMallCarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Override
    public PageResult getCarouselPage(PageQueryUtil pageUtil) {
        List<Carousel> carousels = carouselMapper.findCarouselList(pageUtil);
        int total = carouselMapper.getTotalCarousels(pageUtil);
        PageResult pageResult = new PageResult(carousels, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String saveCarousel(Carousel carousel) {
        if (carouselMapper.insertSelective(carousel) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String updateCarousel(Carousel carousel) {
        Carousel temp = carouselMapper.selectByPrimaryKey(carousel.getCarouselId());
        if (temp == null) {
            return ServiceResultEnum.DATA_NOT_EXIST.getResult();
        }
        temp.setCarouselRank(carousel.getCarouselRank());
        temp.setRedirectUrl(carousel.getRedirectUrl());
        temp.setCarouselUrl(carousel.getCarouselUrl());
        temp.setUpdateTime(new Date());
        if (carouselMapper.updateByPrimaryKeySelective(temp) > 0) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public Carousel getCarouselById(Integer id) {
        return carouselMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        if (ids.length < 1) {
            return false;
        }
        //delete data 
        return carouselMapper.deleteBatch(ids) > 0;
    }

    @Override
    public List<PetPalMallIndexCarouselVO> getCarouselsForIndex(int number) {
        List<PetPalMallIndexCarouselVO> PetPalMallIndexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if (!CollectionUtils.isEmpty(carousels)) {
            PetPalMallIndexCarouselVOS = BeanUtil.copyList(carousels, PetPalMallIndexCarouselVO.class);
        }
        return PetPalMallIndexCarouselVOS;
    }
}
