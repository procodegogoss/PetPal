package ltd.petpal.mall.controller.admin;

import ltd.petpal.mall.common.Constants;
import ltd.petpal.mall.common.PetPalMallCategoryLevelEnum;
import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.entity.GoodsCategory;
import ltd.petpal.mall.entity.PetPalMallGoods;
import ltd.petpal.mall.service.PetPalMallCategoryService;
import ltd.petpal.mall.service.PetPalMallGoodsService;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.Result;
import ltd.petpal.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 
 */
@Controller
@RequestMapping("/admin")
public class PetPalMallGoodsController {

    @Resource
    private PetPalMallGoodsService PetPalMallGoodsService;
    @Resource
    private PetPalMallCategoryService PetPalMallCategoryService;

    @GetMapping("/goods")
    public String goodsPage(HttpServletRequest request) {
        request.setAttribute("path", "petpal_mall_goods");
        return "admin/petpal_mall_goods";
    }

    @GetMapping("/goods/edit")
    public String edit(HttpServletRequest request) {
        request.setAttribute("path", "edit");
        //list level 1
        List<GoodsCategory> firstLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), PetPalMallCategoryLevelEnum.LEVEL_ONE.getLevel());
        if (!CollectionUtils.isEmpty(firstLevelCategories)) {
            //list level 2 of first class of level 1
            List<GoodsCategory> secondLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), PetPalMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //list level 3 of first class of level 2
                List<GoodsCategory> thirdLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), PetPalMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                request.setAttribute("firstLevelCategories", firstLevelCategories);
                request.setAttribute("secondLevelCategories", secondLevelCategories);
                request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                request.setAttribute("path", "goods-edit");
                return "admin/petpal_mall_goods_edit";
            }
        }
        return "error/error_5xx";
    }

    @GetMapping("/goods/edit/{goodsId}")
    public String edit(HttpServletRequest request, @PathVariable("goodsId") Long goodsId) {
        request.setAttribute("path", "edit");
        PetPalMallGoods PetPalMallGoods = PetPalMallGoodsService.getPetPalMallGoodsById(goodsId);
        if (PetPalMallGoods == null) {
            return "error/error_400";
        }
        if (PetPalMallGoods.getGoodsCategoryId() > 0) {
            if (PetPalMallGoods.getGoodsCategoryId() != null || PetPalMallGoods.getGoodsCategoryId() > 0) {
                //return to the front end for three-level  display 
                GoodsCategory currentGoodsCategory = PetPalMallCategoryService.getGoodsCategoryById(PetPalMallGoods.getGoodsCategoryId());
                //category ID field stored in the product table is the ID of three-level classification. If it is not, it is incorrect data.
                if (currentGoodsCategory != null && currentGoodsCategory.getCategoryLevel() == PetPalMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
                    //list all level 1
                    List<GoodsCategory> firstLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), PetPalMallCategoryLevelEnum.LEVEL_ONE.getLevel());
                    //Query all three-level categories under the current parentId based on parentId
                    List<GoodsCategory> thirdLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(currentGoodsCategory.getParentId()), PetPalMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    //Query the parent level 2 
                    GoodsCategory secondCategory = PetPalMallCategoryService.getGoodsCategoryById(currentGoodsCategory.getParentId());
                    if (secondCategory != null) {
                        //Query all secondary categories under the current parentId based on parentId
                        List<GoodsCategory> secondLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondCategory.getParentId()), PetPalMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                        //Query the parent first-level category of the current second-level category
                        GoodsCategory firestCategory = PetPalMallCategoryService.getGoodsCategoryById(secondCategory.getParentId());
                        if (firestCategory != null) {
                            //After all the classified data is obtained, placed in the request object for the front end to read.
                            request.setAttribute("firstLevelCategories", firstLevelCategories);
                            request.setAttribute("secondLevelCategories", secondLevelCategories);
                            request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                            request.setAttribute("firstLevelCategoryId", firestCategory.getCategoryId());
                            request.setAttribute("secondLevelCategoryId", secondCategory.getCategoryId());
                            request.setAttribute("thirdLevelCategoryId", currentGoodsCategory.getCategoryId());
                        }
                    }
                }
            }
        }
        if (PetPalMallGoods.getGoodsCategoryId() == 0) {
            //list all level 1
            List<GoodsCategory> firstLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), PetPalMallCategoryLevelEnum.LEVEL_ONE.getLevel());
            if (!CollectionUtils.isEmpty(firstLevelCategories)) {
                //list all level 2 of the first class of level 1
                List<GoodsCategory> secondLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(firstLevelCategories.get(0).getCategoryId()), PetPalMallCategoryLevelEnum.LEVEL_TWO.getLevel());
                if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                    //list all level 3 of the first class of level 2 
                    List<GoodsCategory> thirdLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), PetPalMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                    request.setAttribute("firstLevelCategories", firstLevelCategories);
                    request.setAttribute("secondLevelCategories", secondLevelCategories);
                    request.setAttribute("thirdLevelCategories", thirdLevelCategories);
                }
            }
        }
        request.setAttribute("goods", PetPalMallGoods);
        request.setAttribute("path", "goods-edit");
        return "admin/petpal_mall_goods_edit";
    }

    /**
     * lists
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(PetPalMallGoodsService.getPetPalMallGoodsPage(pageUtil));
    }

    /**
     * add
     */
    @RequestMapping(value = "/goods/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody PetPalMallGoods PetPalMallGoods) {
        if (StringUtils.isEmpty(PetPalMallGoods.getGoodsName())
                || StringUtils.isEmpty(PetPalMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(PetPalMallGoods.getTag())
                || Objects.isNull(PetPalMallGoods.getOriginalPrice())
                || Objects.isNull(PetPalMallGoods.getGoodsCategoryId())
                || Objects.isNull(PetPalMallGoods.getSellingPrice())
                || Objects.isNull(PetPalMallGoods.getStockNum())
                || Objects.isNull(PetPalMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(PetPalMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(PetPalMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        String result = PetPalMallGoodsService.savePetPalMallGoods(PetPalMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * modify
     */
    @RequestMapping(value = "/goods/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody PetPalMallGoods PetPalMallGoods) {
        if (Objects.isNull(PetPalMallGoods.getGoodsId())
                || StringUtils.isEmpty(PetPalMallGoods.getGoodsName())
                || StringUtils.isEmpty(PetPalMallGoods.getGoodsIntro())
                || StringUtils.isEmpty(PetPalMallGoods.getTag())
                || Objects.isNull(PetPalMallGoods.getOriginalPrice())
                || Objects.isNull(PetPalMallGoods.getSellingPrice())
                || Objects.isNull(PetPalMallGoods.getGoodsCategoryId())
                || Objects.isNull(PetPalMallGoods.getStockNum())
                || Objects.isNull(PetPalMallGoods.getGoodsSellStatus())
                || StringUtils.isEmpty(PetPalMallGoods.getGoodsCoverImg())
                || StringUtils.isEmpty(PetPalMallGoods.getGoodsDetailContent())) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        String result = PetPalMallGoodsService.updatePetPalMallGoods(PetPalMallGoods);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * details
     */
    @GetMapping("/goods/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        PetPalMallGoods goods = PetPalMallGoodsService.getPetPalMallGoodsById(id);
        if (goods == null) {
            return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(goods);
    }

    /**
     * Modify sale status in batches
     */
    @RequestMapping(value = "/goods/status/{sellStatus}", method = RequestMethod.PUT)
    @ResponseBody
    public Result delete(@RequestBody Long[] ids, @PathVariable("sellStatus") int sellStatus) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        if (sellStatus != Constants.SELL_STATUS_UP && sellStatus != Constants.SELL_STATUS_DOWN) {
            return ResultGenerator.genFailResult("Abnormal status!");
        }
        if (PetPalMallGoodsService.batchUpdateSellStatus(ids, sellStatus)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("Fail to modify!");
        }
    }

}