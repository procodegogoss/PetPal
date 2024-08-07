package ltd.petpal.mall.service.impl;

import ltd.petpal.mall.common.*;
import ltd.petpal.mall.controller.vo.*;
import ltd.petpal.mall.dao.PetPalMallGoodsMapper;
import ltd.petpal.mall.dao.PetPalMallOrderItemMapper;
import ltd.petpal.mall.dao.PetPalMallOrderMapper;
import ltd.petpal.mall.dao.PetPalMallShoppingCartItemMapper;
import ltd.petpal.mall.entity.PetPalMallGoods;
import ltd.petpal.mall.entity.PetPalMallOrder;
import ltd.petpal.mall.entity.PetPalMallOrderItem;
import ltd.petpal.mall.entity.StockNumDTO;
import ltd.petpal.mall.service.PetPalMallOrderService;
import ltd.petpal.mall.util.BeanUtil;
import ltd.petpal.mall.util.NumberUtil;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class PetPalMallOrderServiceImpl implements PetPalMallOrderService {

    @Autowired
    private PetPalMallOrderMapper PetPalMallOrderMapper;
    @Autowired
    private PetPalMallOrderItemMapper PetPalMallOrderItemMapper;
    @Autowired
    private PetPalMallShoppingCartItemMapper PetPalMallShoppingCartItemMapper;
    @Autowired
    private PetPalMallGoodsMapper PetPalMallGoodsMapper;

    @Override
    public PageResult getPetPalMallOrdersPage(PageQueryUtil pageUtil) {
        List<PetPalMallOrder> PetPalMallOrders = PetPalMallOrderMapper.findPetPalMallOrderList(pageUtil);
        int total = PetPalMallOrderMapper.getTotalPetPalMallOrders(pageUtil);
        PageResult pageResult = new PageResult(PetPalMallOrders, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    @Transactional
    public String updateOrderInfo(PetPalMallOrder PetPalMallOrder) {
        PetPalMallOrder temp = PetPalMallOrderMapper.selectByPrimaryKey(PetPalMallOrder.getOrderId());
        //not empty and orderStatus>=0 and some information can be modified before 
        if (temp != null && temp.getOrderStatus() >= 0 && temp.getOrderStatus() < 3) {
            temp.setTotalPrice(PetPalMallOrder.getTotalPrice());
            temp.setUserAddress(PetPalMallOrder.getUserAddress());
            temp.setUpdateTime(new Date());
            if (PetPalMallOrderMapper.updateByPrimaryKeySelective(temp) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            }
            return ServiceResultEnum.DB_ERROR.getResult();
        }
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkDone(Long[] ids) {
        //Query all orders,  status, modify status and update time
        List<PetPalMallOrder> orders = PetPalMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (PetPalMallOrder PetPalMallOrder : orders) {
                if (PetPalMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += PetPalMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (PetPalMallOrder.getOrderStatus() != 1) {
                    errorOrderNos += PetPalMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //order status is normal. can complete the distribution and modify the order status and update time.
                if (PetPalMallOrderMapper.checkDone(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //The order cannot be shipped out of the warehouse at this time.
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "The status of the order is not paid successful,shippment cannot be performed.";
                } else {
                    return "There are too many unpaid orders, cannot process";
                }
            }
        }
        //No results, return error
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String checkOut(Long[] ids) {
        //Query all orders,  status, modify status and update time
        List<PetPalMallOrder> orders = PetPalMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (PetPalMallOrder PetPalMallOrder : orders) {
                if (PetPalMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += PetPalMallOrder.getOrderNo() + " ";
                    continue;
                }
                if (PetPalMallOrder.getOrderStatus() != 1 && PetPalMallOrder.getOrderStatus() != 2) {
                    errorOrderNos += PetPalMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //order status is normal. can complete the distribution and modify the order status and update time.
                if (PetPalMallOrderMapper.checkOut(Arrays.asList(ids)) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //The order cannot be shipped out of the warehouse at this time.
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "The status of the order is not paid successful,shippment cannot be performed.";
                } else {
                    return "There are too many unpaid orders, cannot process";
                }
            }
        }
        //No results, return error
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String closeOrder(Long[] ids) {
        //Query all orders,  status, modify status and update time
        List<PetPalMallOrder> orders = PetPalMallOrderMapper.selectByPrimaryKeys(Arrays.asList(ids));
        String errorOrderNos = "";
        if (!CollectionUtils.isEmpty(orders)) {
            for (PetPalMallOrder PetPalMallOrder : orders) {
                // isDeleted=1 is closed order 
                if (PetPalMallOrder.getIsDeleted() == 1) {
                    errorOrderNos += PetPalMallOrder.getOrderNo() + " ";
                    continue;
                }
                //The order has been closed or completed and cannot be closed.
                if (PetPalMallOrder.getOrderStatus() == 4 || PetPalMallOrder.getOrderStatus() < 0) {
                    errorOrderNos += PetPalMallOrder.getOrderNo() + " ";
                }
            }
            if (StringUtils.isEmpty(errorOrderNos)) {
                //order status is normal. can complete the distribution and modify the order status and update time.
                if (PetPalMallOrderMapper.closeOrder(Arrays.asList(ids), PetPalMallOrderStatusEnum.ORDER_CLOSED_BY_JUDGE.getOrderStatus()) > 0) {
                    return ServiceResultEnum.SUCCESS.getResult();
                } else {
                    return ServiceResultEnum.DB_ERROR.getResult();
                }
            } else {
                //cannot process close the order 
                if (errorOrderNos.length() > 0 && errorOrderNos.length() < 100) {
                    return errorOrderNos + "cannot process close the order ";
                } else {
                    return "The order cannot be closed";
                }
            }
        }
        //No results, return error
        return ServiceResultEnum.DATA_NOT_EXIST.getResult();
    }

    @Override
    @Transactional
    public String saveOrder(PetPalMallUserVO user, List<PetPalMallShoppingCartItemVO> myShoppingCartItems) {
        List<Long> itemIdList = myShoppingCartItems.stream().map(PetPalMallShoppingCartItemVO::getCartItemId).collect(Collectors.toList());
        List<Long> goodsIds = myShoppingCartItems.stream().map(PetPalMallShoppingCartItemVO::getGoodsId).collect(Collectors.toList());
        List<PetPalMallGoods> PetPalMallGoods = PetPalMallGoodsMapper.selectByPrimaryKeys(goodsIds);
        Map<Long, PetPalMallGoods> PetPalMallGoodsMap = PetPalMallGoods.stream().collect(Collectors.toMap(PetPalMallGoods::getGoodsId, Function.identity(), (entity1, entity2) -> entity1));
        //to check stock 
        for (PetPalMallShoppingCartItemVO shoppingCartItemVO : myShoppingCartItems) {
            //The associated product data in the shopping cart does not exist in the detected product, and an error reminder is returned directly.
            if (!PetPalMallGoodsMap.containsKey(shoppingCartItemVO.getGoodsId())) {
                PetPalMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
            }
            //If the quantity is greater than the inventory, an error reminder will be returned directly.
            if (shoppingCartItemVO.getGoodsCount() > PetPalMallGoodsMap.get(shoppingCartItemVO.getGoodsId()).getStockNum()) {
                PetPalMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
            }
        }
        //delete the option of adding to the cart 
        if (!CollectionUtils.isEmpty(itemIdList) && !CollectionUtils.isEmpty(goodsIds) && !CollectionUtils.isEmpty(PetPalMallGoods)) {
            if (PetPalMallShoppingCartItemMapper.deleteBatch(itemIdList) > 0) {
                List<StockNumDTO> stockNumDTOS = BeanUtil.copyList(myShoppingCartItems, StockNumDTO.class);
                int updateStockNumResult = PetPalMallGoodsMapper.updateStockNum(stockNumDTOS);
                if (updateStockNumResult < 1) {
                    PetPalMallException.fail(ServiceResultEnum.SHOPPING_ITEM_COUNT_ERROR.getResult());
                }
                //Generate order number
                String orderNo = NumberUtil.genOrderNo();
                int priceTotal = 0;
                //save order 
                PetPalMallOrder PetPalMallOrder = new PetPalMallOrder();
                PetPalMallOrder.setOrderNo(orderNo);
                PetPalMallOrder.setUserId(user.getUserId());
                PetPalMallOrder.setUserAddress(user.getAddress());
                //total amount 
                for (PetPalMallShoppingCartItemVO PetPalMallShoppingCartItemVO : myShoppingCartItems) {
                    priceTotal += PetPalMallShoppingCartItemVO.getGoodsCount() * PetPalMallShoppingCartItemVO.getSellingPrice();
                }
                if (priceTotal < 1) {
                    PetPalMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                PetPalMallOrder.setTotalPrice(priceTotal);
                //todo The order body field is used as description information for generating payment orders. 
                // It is not connected to the third-party payment interface for the time being, so this field is temporarily set to an empty string.
                String extraInfo = "";
                PetPalMallOrder.setExtraInfo(extraInfo);
                //Generate line items and save line item records
                if (PetPalMallOrderMapper.insertSelective(PetPalMallOrder) > 0) {
                    //Generate a snapshot of all line items and save them to the database
                    List<PetPalMallOrderItem> PetPalMallOrderItems = new ArrayList<>();
                    for (PetPalMallShoppingCartItemVO PetPalMallShoppingCartItemVO : myShoppingCartItems) {
                        PetPalMallOrderItem PetPalMallOrderItem = new PetPalMallOrderItem();
                        //Use the BeanUtil tool class to copy the properties in PetPalMallShoppingCartItemVO to the PetPalMallOrderItem object
                        BeanUtil.copyProperties(PetPalMallShoppingCartItemVO, PetPalMallOrderItem);
                        //useGeneratedKeys is used in the insert() method of the PetPalMallOrderMapper file, so the orderId can be obtained.
                        PetPalMallOrderItem.setOrderId(PetPalMallOrder.getOrderId());
                        PetPalMallOrderItems.add(PetPalMallOrderItem);
                    }
                    //save to database
                    if (PetPalMallOrderItemMapper.insertBatch(PetPalMallOrderItems) > 0) {
                        //After all operations are successful, the order number is returned for the Controller method to the order details.
                        return orderNo;
                    }
                    PetPalMallException.fail(ServiceResultEnum.ORDER_PRICE_ERROR.getResult());
                }
                PetPalMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
            }
            PetPalMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
        }
        PetPalMallException.fail(ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult());
        return ServiceResultEnum.SHOPPING_ITEM_ERROR.getResult();
    }

    @Override
    public PetPalMallOrderDetailVO getOrderDetailByOrderNo(String orderNo, Long userId) {
        PetPalMallOrder PetPalMallOrder = PetPalMallOrderMapper.selectByOrderNo(orderNo);
        if (PetPalMallOrder != null) {
            //todo Verify whether the order is placed by the current userId, otherwise an error will be reported
            List<PetPalMallOrderItem> orderItems = PetPalMallOrderItemMapper.selectByOrderId(PetPalMallOrder.getOrderId());
            //Get order data
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<PetPalMallOrderItemVO> PetPalMallOrderItemVOS = BeanUtil.copyList(orderItems, PetPalMallOrderItemVO.class);
                PetPalMallOrderDetailVO PetPalMallOrderDetailVO = new PetPalMallOrderDetailVO();
                BeanUtil.copyProperties(PetPalMallOrder, PetPalMallOrderDetailVO);
                PetPalMallOrderDetailVO.setOrderStatusString(PetPalMallOrderStatusEnum.getPetPalMallOrderStatusEnumByStatus(PetPalMallOrderDetailVO.getOrderStatus()).getName());
                PetPalMallOrderDetailVO.setPayTypeString(PayTypeEnum.getPayTypeEnumByType(PetPalMallOrderDetailVO.getPayType()).getName());
                PetPalMallOrderDetailVO.setPetPalMallOrderItemVOS(PetPalMallOrderItemVOS);
                return PetPalMallOrderDetailVO;
            }
        }
        return null;
    }

    @Override
    public PetPalMallOrder getPetPalMallOrderByOrderNo(String orderNo) {
        return PetPalMallOrderMapper.selectByOrderNo(orderNo);
    }

    @Override
    public PageResult getMyOrders(PageQueryUtil pageUtil) {
        int total = PetPalMallOrderMapper.getTotalPetPalMallOrders(pageUtil);
        List<PetPalMallOrder> PetPalMallOrders = PetPalMallOrderMapper.findPetPalMallOrderList(pageUtil);
        List<PetPalMallOrderListVO> orderListVOS = new ArrayList<>();
        if (total > 0) {
            //Data conversion: Convert entity classes to vo
            orderListVOS = BeanUtil.copyList(PetPalMallOrders, PetPalMallOrderListVO.class);
            //Set the value displayed in order status
            for (PetPalMallOrderListVO PetPalMallOrderListVO : orderListVOS) {
                PetPalMallOrderListVO.setOrderStatusString(PetPalMallOrderStatusEnum.getPetPalMallOrderStatusEnumByStatus(PetPalMallOrderListVO.getOrderStatus()).getName());
            }
            List<Long> orderIds = PetPalMallOrders.stream().map(PetPalMallOrder::getOrderId).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(orderIds)) {
                List<PetPalMallOrderItem> orderItems = PetPalMallOrderItemMapper.selectByOrderIds(orderIds);
                Map<Long, List<PetPalMallOrderItem>> itemByOrderIdMap = orderItems.stream().collect(groupingBy(PetPalMallOrderItem::getOrderId));
                for (PetPalMallOrderListVO PetPalMallOrderListVO : orderListVOS) {
                    //Encapsulates the order item data for each order list object
                    if (itemByOrderIdMap.containsKey(PetPalMallOrderListVO.getOrderId())) {
                        List<PetPalMallOrderItem> orderItemListTemp = itemByOrderIdMap.get(PetPalMallOrderListVO.getOrderId());
                        //Convert the PetPalMallOrderItem object list to the PetPalMallOrderItemVO object list
                        List<PetPalMallOrderItemVO> PetPalMallOrderItemVOS = BeanUtil.copyList(orderItemListTemp, PetPalMallOrderItemVO.class);
                        PetPalMallOrderListVO.setPetPalMallOrderItemVOS(PetPalMallOrderItemVOS);
                    }
                }
            }
        }
        PageResult pageResult = new PageResult(orderListVOS, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    @Override
    public String cancelOrder(String orderNo, Long userId) {
        PetPalMallOrder PetPalMallOrder = PetPalMallOrderMapper.selectByOrderNo(orderNo);
        if (PetPalMallOrder != null) {
            //todo Verify whether the order is placed by the current userId, otherwise an error will be reported
            //todo check order status
            if (PetPalMallOrderMapper.closeOrder(Collections.singletonList(PetPalMallOrder.getOrderId()), PetPalMallOrderStatusEnum.ORDER_CLOSED_BY_MALLUSER.getOrderStatus()) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String finishOrder(String orderNo, Long userId) {
        PetPalMallOrder PetPalMallOrder = PetPalMallOrderMapper.selectByOrderNo(orderNo);
        if (PetPalMallOrder != null) {
            //todo Verify whether the order is placed by the current userId, otherwise an error will be reported
            //todo check order status
            PetPalMallOrder.setOrderStatus((byte) PetPalMallOrderStatusEnum.ORDER_SUCCESS.getOrderStatus());
            PetPalMallOrder.setUpdateTime(new Date());
            if (PetPalMallOrderMapper.updateByPrimaryKeySelective(PetPalMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public String paySuccess(String orderNo, int payType) {
        PetPalMallOrder PetPalMallOrder = PetPalMallOrderMapper.selectByOrderNo(orderNo);
        if (PetPalMallOrder != null) {
            //todo check order status: no modification if it is not in the pending payment state.
            PetPalMallOrder.setOrderStatus((byte) PetPalMallOrderStatusEnum.OREDER_PAID.getOrderStatus());
            PetPalMallOrder.setPayType((byte) payType);
            PetPalMallOrder.setPayStatus((byte) PayStatusEnum.PAY_SUCCESS.getPayStatus());
            PetPalMallOrder.setPayTime(new Date());
            PetPalMallOrder.setUpdateTime(new Date());
            if (PetPalMallOrderMapper.updateByPrimaryKeySelective(PetPalMallOrder) > 0) {
                return ServiceResultEnum.SUCCESS.getResult();
            } else {
                return ServiceResultEnum.DB_ERROR.getResult();
            }
        }
        return ServiceResultEnum.ORDER_NOT_EXIST_ERROR.getResult();
    }

    @Override
    public List<PetPalMallOrderItemVO> getOrderItems(Long id) {
        PetPalMallOrder PetPalMallOrder = PetPalMallOrderMapper.selectByPrimaryKey(id);
        if (PetPalMallOrder != null) {
            List<PetPalMallOrderItem> orderItems = PetPalMallOrderItemMapper.selectByOrderId(PetPalMallOrder.getOrderId());
            //get order data
            if (!CollectionUtils.isEmpty(orderItems)) {
                List<PetPalMallOrderItemVO> PetPalMallOrderItemVOS = BeanUtil.copyList(orderItems, PetPalMallOrderItemVO.class);
                return PetPalMallOrderItemVOS;
            }
        }
        return null;
    }
}