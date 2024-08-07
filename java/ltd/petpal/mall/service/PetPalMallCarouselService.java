package ltd.petpal.mall.service;

import ltd.petpal.mall.controller.vo.PetPalMallIndexCarouselVO;
import ltd.petpal.mall.entity.Carousel;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;

import java.util.List;

public interface PetPalMallCarouselService {
    /**
     * backend page
     *
     * @param pageUtil
     * @return
     */
    PageResult getCarouselPage(PageQueryUtil pageUtil);

    String saveCarousel(Carousel carousel);

    String updateCarousel(Carousel carousel);

    Carousel getCarouselById(Integer id);

    Boolean deleteBatch(Integer[] ids);

    /**
     * Returns a fixed number of carousel objects (called on the home page)
     *
     * @param number
     * @return
     */
    List<PetPalMallIndexCarouselVO> getCarouselsForIndex(int number);
}
