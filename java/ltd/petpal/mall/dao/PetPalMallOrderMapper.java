package ltd.petpal.mall.dao;

import ltd.petpal.mall.entity.PetPalMallOrder;
import ltd.petpal.mall.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PetPalMallOrderMapper {
    int deleteByPrimaryKey(Long orderId);

    int insert(PetPalMallOrder record);

    int insertSelective(PetPalMallOrder record);

    PetPalMallOrder selectByPrimaryKey(Long orderId);

    PetPalMallOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(PetPalMallOrder record);

    int updateByPrimaryKey(PetPalMallOrder record);

    List<PetPalMallOrder> findPetPalMallOrderList(PageQueryUtil pageUtil);

    int getTotalPetPalMallOrders(PageQueryUtil pageUtil);

    List<PetPalMallOrder> selectByPrimaryKeys(@Param("orderIds") List<Long> orderIds);

    int checkOut(@Param("orderIds") List<Long> orderIds);

    int closeOrder(@Param("orderIds") List<Long> orderIds, @Param("orderStatus") int orderStatus);

    int checkDone(@Param("orderIds") List<Long> asList);
}