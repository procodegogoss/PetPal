package ltd.petpal.mall.service;

import ltd.petpal.mall.controller.vo.PetPalMallShoppingCartItemVO;
import ltd.petpal.mall.entity.PetPalMallShoppingCartItem;

import java.util.List;

public interface PetPalMallShoppingCartService {

    /**
     * save items to cart
     *
     * @param PetPalMallShoppingCartItem
     * @return
     */
    String savePetPalMallCartItem(PetPalMallShoppingCartItem PetPalMallShoppingCartItem);

    /**
     * Modify properties in shopping cart
     *
     * @param PetPalMallShoppingCartItem
     * @return
     */
    String updatePetPalMallCartItem(PetPalMallShoppingCartItem PetPalMallShoppingCartItem);

    /**
     * Get shopping cart details
     *
     * @param PetPalMallShoppingCartItemId
     * @return
     */
    PetPalMallShoppingCartItem getPetPalMallCartItemById(Long PetPalMallShoppingCartItemId);

    /**
     * delete item from cart 
     *
     * @param PetPalMallShoppingCartItemId
     * @return
     */
    Boolean deleteById(Long PetPalMallShoppingCartItemId);

    /**
     * Get list data in my shopping cart
     *
     * @param PetPalMallUserId
     * @return
     */
    List<PetPalMallShoppingCartItemVO> getMyShoppingCartItems(Long PetPalMallUserId);
}
