package ltd.petpal.mall.service;

import ltd.petpal.mall.controller.vo.PetPalMallUserVO;
import ltd.petpal.mall.entity.MallUser;
import ltd.petpal.mall.util.PageQueryUtil;
import ltd.petpal.mall.util.PageResult;

import javax.servlet.http.HttpSession;

public interface PetPalMallUserService {
    /**
     * backend page util
     *
     * @param pageUtil
     * @return
     */
    PageResult getPetPalMallUsersPage(PageQueryUtil pageUtil);

    /**
     * user register
     *
     * @param loginName
     * @param password
     * @return
     */
    String register(String loginName, String password);

    /**
     * login
     *
     * @param loginName
     * @param passwordMD5
     * @param httpSession
     * @return
     */
    String login(String loginName, String passwordMD5, HttpSession httpSession);

    /**
     * Modify user information and return the latest user information
     *
     * @param mallUser
     * @return
     */
    PetPalMallUserVO updateUserInfo(MallUser mallUser, HttpSession httpSession);

    /**
     * User disabling and undisabling (0-unlocked 1-locked)
     *
     * @param ids
     * @param lockStatus
     * @return
     */
    Boolean lockUsers(Integer[] ids, int lockStatus);
}
