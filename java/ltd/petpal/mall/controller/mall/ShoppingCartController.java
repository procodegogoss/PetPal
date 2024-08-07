package ltd.petpal.mall.controller.mall;

import ltd.petpal.mall.common.Constants;
import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.controller.vo.PetPalMallShoppingCartItemVO;
import ltd.petpal.mall.controller.vo.PetPalMallUserVO;
import ltd.petpal.mall.entity.PetPalMallShoppingCartItem;
import ltd.petpal.mall.service.PetPalMallShoppingCartService;
import ltd.petpal.mall.util.Result;
import ltd.petpal.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ShoppingCartController {

    @Resource
    private PetPalMallShoppingCartService PetPalMallShoppingCartService;

    @GetMapping("/shop-cart")
    public String cartListPage(HttpServletRequest request,
                               HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        int itemsTotal = 0;
        int priceTotal = 0;
        List<PetPalMallShoppingCartItemVO> myShoppingCartItems = PetPalMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (!CollectionUtils.isEmpty(myShoppingCartItems)) {
            //items total
            itemsTotal = myShoppingCartItems.stream().mapToInt(PetPalMallShoppingCartItemVO::getGoodsCount).sum();
            if (itemsTotal < 1) {
                return "error/error_5xx";
            }
            //total amount
            for (PetPalMallShoppingCartItemVO PetPalMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += PetPalMallShoppingCartItemVO.getGoodsCount() * PetPalMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("itemsTotal", itemsTotal);
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/cart";
    }

    @PostMapping("/shop-cart")
    @ResponseBody
    public Result savePetPalMallShoppingCartItem(@RequestBody PetPalMallShoppingCartItem PetPalMallShoppingCartItem,
                                                 HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        PetPalMallShoppingCartItem.setUserId(user.getUserId());
        //count items
        String saveResult = PetPalMallShoppingCartService.savePetPalMallCartItem(PetPalMallShoppingCartItem);
        //add success
        if (ServiceResultEnum.SUCCESS.getResult().equals(saveResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //add fail
        return ResultGenerator.genFailResult(saveResult);
    }

    @PutMapping("/shop-cart")
    @ResponseBody
    public Result updatePetPalMallShoppingCartItem(@RequestBody PetPalMallShoppingCartItem PetPalMallShoppingCartItem,
                                                   HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        PetPalMallShoppingCartItem.setUserId(user.getUserId());
        //count items
        String updateResult = PetPalMallShoppingCartService.updatePetPalMallCartItem(PetPalMallShoppingCartItem);
        //modify success
        if (ServiceResultEnum.SUCCESS.getResult().equals(updateResult)) {
            return ResultGenerator.genSuccessResult();
        }
        //modify fail 
        return ResultGenerator.genFailResult(updateResult);
    }

    @DeleteMapping("/shop-cart/{PetPalMallShoppingCartItemId}")
    @ResponseBody
    public Result updatePetPalMallShoppingCartItem(@PathVariable("PetPalMallShoppingCartItemId") Long PetPalMallShoppingCartItemId,
                                                   HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        Boolean deleteResult = PetPalMallShoppingCartService.deleteById(PetPalMallShoppingCartItemId);
        //delete success
        if (deleteResult) {
            return ResultGenerator.genSuccessResult();
        }
        // delete fail 
        return ResultGenerator.genFailResult(ServiceResultEnum.OPERATE_ERROR.getResult());
    }

    @GetMapping("/shop-cart/settle")
    public String settlePage(HttpServletRequest request,
                             HttpSession httpSession) {
        int priceTotal = 0;
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<PetPalMallShoppingCartItemVO> myShoppingCartItems = PetPalMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //not direct to payment page
            return "/shop-cart";
        } else {
            //total amount
            for (PetPalMallShoppingCartItemVO PetPalMallShoppingCartItemVO : myShoppingCartItems) {
                priceTotal += PetPalMallShoppingCartItemVO.getGoodsCount() * PetPalMallShoppingCartItemVO.getSellingPrice();
            }
            if (priceTotal < 1) {
                return "error/error_5xx";
            }
        }
        request.setAttribute("priceTotal", priceTotal);
        request.setAttribute("myShoppingCartItems", myShoppingCartItems);
        return "mall/order-settle";
    }
}
