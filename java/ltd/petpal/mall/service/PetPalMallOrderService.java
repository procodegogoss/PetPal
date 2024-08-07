package ltd.petpal.mall.service;

import ltd.petpal.mall.controller.vo.PetPalMallOrderDetailVO;
import ltd.petpal.mall.controller.vo.PetPalMallOrderItemVO;
import ltd.petpal.mall.controller.vo.PetPalMallShoppingCartItemVO;
import ltd.petpal.mall.controller.vo.PetPalMallUserVO;
import ltd.petpal.mall.entity.PetPalMallOrder;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;

import java.util.List;

public interface PetPalMallOrderService {
    /**
     * 
     *
     * @param pageUtil
     * @return
     */
    PageResult getPetPalMallOrdersPage(PageQueryUtil pageUtil);

    /**
     * modify order
     *
     * @param PetPalMallOrder
     * @return
     */
    String updateOrderInfo(PetPalMallOrder PetPalMallOrder);

    /**
     * distribute
     *
     * @param ids
     * @return
     */
    String checkDone(Long[] ids);

    /**
     * ship out warehouse
     *
     * @param ids
     * @return
     */
    String checkOut(Long[] ids);

    /**
     * check out 
     *
     * @param ids
     * @return
     */
    String closeOrder(Long[] ids);

    /**
     * save order 
     *
     * @param user
     * @param myShoppingCartItems
     * @return
     */
    String saveOrder(PetPalMallUserVO user, List<PetPalMallShoppingCartItemVO> myShoppingCartItems);

    /**
     * get order details
     *
     * @param orderNo
     * @param userId
     * @return
     */
    PetPalMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId);

    /**
     * get order details
     *
     * @param orderNo
     * @return
     */
    PetPalMallOrder getPetPalMallOrderByOrderNo(String orderNo);

    /**
     * order list
     *
     * @param pageUtil
     * @return
     */
    PageResult getMyOrders(PageQueryUtil pageUtil);

    /**
     * Cancel order manually
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String cancelOrder(String orderNo, Long userId);

    /**
     * confirm the receipt of goods
     *
     * @param orderNo
     * @param userId
     * @return
     */
    String finishOrder(String orderNo, Long userId);

    String paySuccess(String orderNo, int payType);

    List<PetPalMallOrderItemVO> getOrderItems(Long id);
}
