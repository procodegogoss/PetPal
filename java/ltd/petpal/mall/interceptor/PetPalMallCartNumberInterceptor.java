package ltd.petpal.mall.interceptor;

import ltd.petpal.mall.common.Constants;
import ltd.petpal.mall.controller.vo.PetPalMallUserVO;
import ltd.petpal.mall.dao.PetPalMallShoppingCartItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * shopping cart item quantity
 *
 * @author 
 */
@Component
public class PetPalMallCartNumberInterceptor implements HandlerInterceptor {

    @Autowired
    private PetPalMallShoppingCartItemMapper PetPalMallShoppingCartItemMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //The quantity in shopping cart will change, but the data in the session is not modified in these interfaces. Here we deal with it 
        if (null != request.getSession() && null != request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY)) {
            //If currently logged in, query the database and set the quantity value in the shopping cart.
            PetPalMallUserVO PetPalMallUserVO = (PetPalMallUserVO) request.getSession().getAttribute(Constants.MALL_USER_SESSION_KEY);
            //set the quantity value in the shopping cart.
            PetPalMallUserVO.setShopCartItemCount(PetPalMallShoppingCartItemMapper.selectCountByUserId(PetPalMallUserVO.getUserId()));
            request.getSession().setAttribute(Constants.MALL_USER_SESSION_KEY, PetPalMallUserVO);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
