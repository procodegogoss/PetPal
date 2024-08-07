package ltd.petpal.mall.dao;

import ltd.petpal.mall.entity.PetPalMallOrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PetPalMallOrderItemMapper {
    int deleteByPrimaryKey(Long orderItemId);

    int insert(PetPalMallOrderItem record);

    int insertSelective(PetPalMallOrderItem record);

    PetPalMallOrderItem selectByPrimaryKey(Long orderItemId);

    /**
     * 
     *
     * @param orderId
     * @return
     */
    List<PetPalMallOrderItem> selectByOrderId(Long orderId);

    /**
     * 
     *
     * @param orderIds
     * @return
     */
    List<PetPalMallOrderItem> selectByOrderIds(@Param("orderIds") List<Long> orderIds);

    /**
     * 
     *
     * @param orderItems
     * @return
     */
    int insertBatch(@Param("orderItems") List<PetPalMallOrderItem> orderItems);

    int updateByPrimaryKeySelective(PetPalMallOrderItem record);

    int updateByPrimaryKey(PetPalMallOrderItem record);
}