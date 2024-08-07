package ltd.petpal.mall.controller.mall;

import ltd.petpal.mall.common.Constants;
import ltd.petpal.mall.common.IndexConfigTypeEnum;
import ltd.petpal.mall.controller.vo.PetPalMallIndexCarouselVO;
import ltd.petpal.mall.controller.vo.PetPalMallIndexCategoryVO;
import ltd.petpal.mall.controller.vo.PetPalMallIndexConfigGoodsVO;
import ltd.petpal.mall.service.PetPalMallCarouselService;
import ltd.petpal.mall.service.PetPalMallCategoryService;
import ltd.petpal.mall.service.PetPalMallIndexConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private PetPalMallCarouselService PetPalMallCarouselService;

    @Resource
    private PetPalMallIndexConfigService PetPalMallIndexConfigService;

    @Resource
    private PetPalMallCategoryService PetPalMallCategoryService;

    @GetMapping({"/index", "/", "/index.html"})
    public String indexPage(HttpServletRequest request) {
        List<PetPalMallIndexCategoryVO> categories = PetPalMallCategoryService.getCategoriesForIndex();
        if (CollectionUtils.isEmpty(categories)) {
            return "error/error_5xx";
        }
        List<PetPalMallIndexCarouselVO> carousels = PetPalMallCarouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        List<PetPalMallIndexConfigGoodsVO> hotGoodses = PetPalMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        List<PetPalMallIndexConfigGoodsVO> newGoodses = PetPalMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        List<PetPalMallIndexConfigGoodsVO> recommendGoodses = PetPalMallIndexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);
        request.setAttribute("categories", categories);//categories data 
        request.setAttribute("carousels", carousels);//carousel turning
        request.setAttribute("hotGoodses", hotGoodses);//hot sales
        request.setAttribute("newGoodses", newGoodses);//new items
        request.setAttribute("recommendGoodses", recommendGoodses);//recommend items 
        return "mall/index";
    }
}
