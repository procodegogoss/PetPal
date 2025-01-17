package ltd.petpal.mall.controller.mall;

import ltd.petpal.mall.common.Constants;
import ltd.petpal.mall.controller.vo.PetPalMallGoodsDetailVO;
import ltd.petpal.mall.controller.vo.SearchPageCategoryVO;
import ltd.petpal.mall.entity.PetPalMallGoods;
import ltd.petpal.mall.service.PetPalMallCategoryService;
import ltd.petpal.mall.service.PetPalMallGoodsService;
import ltd.petpal.mall.util.BeanUtil;
import ltd.petpal.mall.util.PageQueryUtil;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class GoodsController {

    @Resource
    private PetPalMallGoodsService PetPalMallGoodsService;
    @Resource
    private PetPalMallCategoryService PetPalMallCategoryService;

    @GetMapping({"/search", "/search.html"})
    public String searchPage(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.GOODS_SEARCH_PAGE_LIMIT);
        //Encapsulating categorical data
        if (params.containsKey("goodsCategoryId") && !StringUtils.isEmpty(params.get("goodsCategoryId") + "")) {
            Long categoryId = Long.valueOf(params.get("goodsCategoryId") + "");
            SearchPageCategoryVO searchPageCategoryVO = PetPalMallCategoryService.getCategoriesForSearch(categoryId);
            if (searchPageCategoryVO != null) {
                request.setAttribute("goodsCategoryId", categoryId);
                request.setAttribute("searchPageCategoryVO", searchPageCategoryVO);
            }
        }
        //Encapsulate parameters for front-end 
        if (params.containsKey("orderBy") && !StringUtils.isEmpty(params.get("orderBy") + "")) {
            request.setAttribute("orderBy", params.get("orderBy") + "");
        }
        String keyword = "";
        //Filter keywords and remove spaces
        if (params.containsKey("keyword") && !StringUtils.isEmpty((params.get("keyword") + "").trim())) {
            keyword = params.get("keyword") + "";
        }
        request.setAttribute("keyword", keyword);
        params.put("keyword", keyword);
        //Encapsulate product data
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("pageResult", PetPalMallGoodsService.searchPetPalMallGoods(pageUtil));
        return "mall/search";
    }

    @GetMapping("/goods/detail/{goodsId}")
    public String detailPage(@PathVariable("goodsId") Long goodsId, HttpServletRequest request) {
        if (goodsId < 1) {
            return "error/error_5xx";
        }
        PetPalMallGoods goods = PetPalMallGoodsService.getPetPalMallGoodsById(goodsId);
        if (goods == null) {
            return "error/error_404";
        }
        PetPalMallGoodsDetailVO goodsDetailVO = new PetPalMallGoodsDetailVO();
        BeanUtil.copyProperties(goods, goodsDetailVO);
        goodsDetailVO.setGoodsCarouselList(goods.getGoodsCarousel().split(","));
        request.setAttribute("goodsDetail", goodsDetailVO);
        return "mall/detail";
    }

}
