package ltd.petpal.mall.controller.admin;

import ltd.petpal.mall.common.NewBeeMallCategoryLevelEnum;
import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.entity.GoodsCategory;
import ltd.petpal.mall.service.NewBeeMallCategoryService;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.Result;
import ltd.petpal.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author 
 */
@Controller
@RequestMapping("/admin")
public class PetPalMallGoodsCategoryController {

    @Resource
    private PetPalMallCategoryService PetPalMallCategoryService;

    @GetMapping("/categories")
    public String categoriesPage(HttpServletRequest request, @RequestParam("categoryLevel") Byte categoryLevel, @RequestParam("parentId") Long parentId, @RequestParam("backParentId") Long backParentId) {
        if (categoryLevel == null || categoryLevel < 1 || categoryLevel > 3) {
            return "error/error_5xx";
        }
        request.setAttribute("path", "petpal_mall_category");
        request.setAttribute("parentId", parentId);
        request.setAttribute("backParentId", backParentId);
        request.setAttribute("categoryLevel", categoryLevel);
        return "admin/petpal_mall_category";
    }

    /**
     * list
     */
    @RequestMapping(value = "/categories/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(PetPalMallCategoryService.getCategorisPage(pageUtil));
    }

    /**
     * list
     */
    @RequestMapping(value = "/categories/listForSelect", method = RequestMethod.GET)
    @ResponseBody
    public Result listForSelect(@RequestParam("categoryId") Long categoryId) {
        if (categoryId == null || categoryId < 1) {
            return ResultGenerator.genFailResult("Parameter missing!");
        }
        GoodsCategory category = PetPalMallCategoryService.getGoodsCategoryById(categoryId);
        //neither level one nor two , no data will be returned.
        if (category == null || category.getCategoryLevel() == PetPalMallCategoryLevelEnum.LEVEL_THREE.getLevel()) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        Map categoryResult = new HashMap(2);
        if (category.getCategoryLevel() == PetPalMallCategoryLevelEnum.LEVEL_ONE.getLevel()) {
            //level classification, return all level two under current level one, and all level 3 under the first level 2 classification list.
            //Query all level 2  of the first entity in the level 1  list
            List<GoodsCategory> secondLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), PetPalMallCategoryLevelEnum.LEVEL_TWO.getLevel());
            if (!CollectionUtils.isEmpty(secondLevelCategories)) {
                //Query all level 3 of the level 2 list
                List<GoodsCategory> thirdLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(secondLevelCategories.get(0).getCategoryId()), PetPalMallCategoryLevelEnum.LEVEL_THREE.getLevel());
                categoryResult.put("secondLevelCategories", secondLevelCategories);
                categoryResult.put("thirdLevelCategories", thirdLevelCategories);
            }
        }
        if (category.getCategoryLevel() == PetPalMallCategoryLevelEnum.LEVEL_TWO.getLevel()) {
            //if level 2, return all level 3 lists under the current category.
            List<GoodsCategory> thirdLevelCategories = PetPalMallCategoryService.selectByLevelAndParentIdsAndNumber(Collections.singletonList(categoryId), PetPalMallCategoryLevelEnum.LEVEL_THREE.getLevel());
            categoryResult.put("thirdLevelCategories", thirdLevelCategories);
        }
        return ResultGenerator.genSuccessResult(categoryResult);
    }

    /**
     * add
     */
    @RequestMapping(value = "/categories/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestBody GoodsCategory goodsCategory) {
        if (Objects.isNull(goodsCategory.getCategoryLevel())
                || StringUtils.isEmpty(goodsCategory.getCategoryName())
                || Objects.isNull(goodsCategory.getParentId())
                || Objects.isNull(goodsCategory.getCategoryRank())) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        String result = PetPalMallCategoryService.saveCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


    /**
     * modify
     */
    @RequestMapping(value = "/categories/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody GoodsCategory goodsCategory) {
        if (Objects.isNull(goodsCategory.getCategoryId())
                || Objects.isNull(goodsCategory.getCategoryLevel())
                || StringUtils.isEmpty(goodsCategory.getCategoryName())
                || Objects.isNull(goodsCategory.getParentId())
                || Objects.isNull(goodsCategory.getCategoryRank())) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        String result = PetPalMallCategoryService.updateGoodsCategory(goodsCategory);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * details
     */
    @GetMapping("/categories/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        GoodsCategory goodsCategory = PetPalMallCategoryService.getGoodsCategoryById(id);
        if (goodsCategory == null) {
            return ResultGenerator.genFailResult(" No data!");
        }
        return ResultGenerator.genSuccessResult(goodsCategory);
    }

    /**
     * delete
     */
    @RequestMapping(value = "/categories/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        if (PetPalMallCategoryService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("Fail to delete");
        }
    }


}