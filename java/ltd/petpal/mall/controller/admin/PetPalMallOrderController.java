package ltd.petpal.mall.controller.admin;

import ltd.petpal.mall.common.PetPalMallOrderStatusEnum;
import ltd.petpal.mall.common.ServiceResultEnum;
import ltd.petpal.mall.controller.vo.PetPalMallOrderItemVO;
import ltd.petpal.mall.entity.IndexConfig;
import ltd.petpal.mall.entity.PetPalMallOrder;
import ltd.petpal.mall.service.PetPalMallIndexConfigService;
import ltd.petpal.mall.service.PetPalMallOrderService;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.Result;
import ltd.petpal.mall.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author 
 */
@Controller
@RequestMapping("/admin")
public class PetPalMallOrderController {

    @Resource
    private PetPalMallOrderService PetPalMallOrderService;

    @GetMapping("/orders")
    public String ordersPage(HttpServletRequest request) {
        request.setAttribute("path", "orders");
        return "admin/petpal_mall_order";
    }

    /**
     * lists
     */
    @RequestMapping(value = "/orders/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam Map<String, Object> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        PageQueryUtil pageUtil = new PageQueryUtil(params);
        return ResultGenerator.genSuccessResult(PetPalMallOrderService.getPetPalMallOrdersPage(pageUtil));
    }

    /**
     * modify
     */
    @RequestMapping(value = "/orders/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestBody PetPalMallOrder PetPalMallOrder) {
        if (Objects.isNull(PetPalMallOrder.getTotalPrice())
                || Objects.isNull(PetPalMallOrder.getOrderId())
                || PetPalMallOrder.getOrderId() < 1
                || PetPalMallOrder.getTotalPrice() < 1
                || StringUtils.isEmpty(PetPalMallOrder.getUserAddress())) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        String result = PetPalMallOrderService.updateOrderInfo(PetPalMallOrder);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * details
     */
    @GetMapping("/order-items/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Long id) {
        List<PetPalMallOrderItemVO> orderItems = PetPalMallOrderService.getOrderItems(id);
        if (!CollectionUtils.isEmpty(orderItems)) {
            return ResultGenerator.genSuccessResult(orderItems);
        }
        return ResultGenerator.genFailResult(ServiceResultEnum.DATA_NOT_EXIST.getResult());
    }

    /**
     * order check 
     */
    @RequestMapping(value = "/orders/checkDone", method = RequestMethod.POST)
    @ResponseBody
    public Result checkDone(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        String result = PetPalMallOrderService.checkDone(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * order check out
     */
    @RequestMapping(value = "/orders/checkOut", method = RequestMethod.POST)
    @ResponseBody
    public Result checkOut(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        String result = PetPalMallOrderService.checkOut(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }

    /**
     * order closed
     */
    @RequestMapping(value = "/orders/close", method = RequestMethod.POST)
    @ResponseBody
    public Result closeOrder(@RequestBody Long[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("Parameter exception!");
        }
        String result = PetPalMallOrderService.closeOrder(ids);
        if (ServiceResultEnum.SUCCESS.getResult().equals(result)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult(result);
        }
    }


}