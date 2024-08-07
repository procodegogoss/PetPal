package ltd.petpal.mall.controller.mall;

import ltd.petpal.mall.common.Constants;
import ltd.petpal.mall.common.PetPalMallException;
import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.controller.vo.PetPalMallOrderDetailVO;
import ltd.petpal.mall.controller.vo.PetPalMallShoppingCartItemVO;
import ltd.petpal.mall.controller.vo.PetPalMallUserVO;
import ltd.petpal.mall.entity.PetPalMallOrder;
import ltd.petpal.mall.service.PetPalMallOrderService;
import ltd.petpal.mall.service.PetPalMallShoppingCartService;
import ltd.petpal.mall.util.PageQueryUtil;
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
import java.util.Map;

@Controller
public class OrderController {

    @Resource
    private PetPalMallShoppingCartService PetPalMallShoppingCartService;
    @Resource
    private PetPalMallOrderService PetPalMallOrderService;

    @GetMapping("/orders/{orderNo}")
    public String orderDetailPage(HttpServletRequest request, @PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        PetPalMallOrderDetailVO orderDetailVO = PetPalMallOrderService.getOrderDetailByOrderNo(orderNo, user.getUserId());
        if (orderDetailVO == null) {
            return "error/error_5xx";
        }
        request.setAttribute("orderDetailVO", orderDetailVO);
        return "mall/order-detail";
    }

    @GetMapping("/orders")
    public String orderListPage(@RequestParam Map<String, Object> params, HttpServletRequest request, HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        params.put("userId", user.getUserId());
        if (StringUtils.isEmpty(params.get("page"))) {
            params.put("page", 1);
        }
        params.put("limit", Constants.ORDER_SEARCH_PAGE_LIMIT);
        //Encapsulate order data
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        request.setAttribute("orderPageResult", PetPalMallOrderService.getMyOrders(pageUtil));
        request.setAttribute("path", "orders");
        return "mall/my-orders";
    }

    @GetMapping("/saveOrder")
    public String saveOrder(HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        List<PetPalMallShoppingCartItemVO> myShoppingCartItems = PetPalMallShoppingCartService.getMyShoppingCartItems(user.getUserId());
        if (StringUtils.isEmpty(user.getAddress().trim())) {
            //no shipping address
            PetPalMallException.fail(ServiceResultEnum.NULL_ADDRESS_ERROR.getResult());
        }
        if (CollectionUtils.isEmpty(myShoppingCartItems)) {
            //If there is no data in shopping cart, redirected to the error page
            PetPalMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        }
        //Save order and return order number
        String saveOrderResult = PetPalMallOrderService.saveOrder(user, myShoppingCartItems);
        //to order details page
        return "redirect:/orders/" + saveOrderResult;
    }

    @PutMapping("/orders/{orderNo}/cancel")
    @ResponseBody
    public Result cancelOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String cancelOrderResult = PetPalMallOrderService.cancelOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(cancelOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(cancelOrderResult);
        }
    }

    @PutMapping("/orders/{orderNo}/finish")
    @ResponseBody
    public Result finishOrder(@PathVariable("orderNo") String orderNo, HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        String finishOrderResult = PetPalMallOrderService.finishOrder(orderNo, user.getUserId());
        if (ServiceResultEnum.SUCCESS.getResult().equals(finishOrderResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(finishOrderResult);
        }
    }

    @GetMapping("/selectPayType")
    public String selectPayType(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        PetPalMallOrder PetPalMallOrder = PetPalMallOrderService.getPetPalMallOrderByOrderNo(orderNo);
        //to check order status userId
        //to check order status
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", PetPalMallOrder.getTotalPrice());
        return "mall/pay-select";
    }

    @GetMapping("/payPage")
    public String payOrder(HttpServletRequest request, @RequestParam("orderNo") String orderNo, HttpSession httpSession, @RequestParam("payType") int payType) {
        PetPalMallUserVO user = (PetPalMallUserVO) httpSession.getAttribute(Constants.MALL_USER_SESSION_KEY);
        PetPalMallOrder PetPalMallOrder = PetPalMallOrderService.getPetPalMallOrderByOrderNo(orderNo);
        //check order status
     
        request.setAttribute("orderNo", orderNo);
        request.setAttribute("totalPrice", PetPalMallOrder.getTotalPrice());
        if (payType == 1) {
            return "mall/alipay";
        } else {
            return "mall/wxpay";
        }
    }

    @GetMapping("/paySuccess")
    @ResponseBody
    public Result paySuccess(@RequestParam("orderNo") String orderNo, @RequestParam("payType") int payType) {
        String payResult = PetPalMallOrderService.paySuccess(orderNo, payType);
        if (ServiceResultEnum.SUCCESS.getResult().equals(payResult)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(payResult);
        }
    }

}
