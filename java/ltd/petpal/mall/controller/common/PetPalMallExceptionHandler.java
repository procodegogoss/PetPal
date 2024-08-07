package ltd.petpal.mall.controller.common;

import ltd.petpal.mall.common.PetPalMallException;
import ltd.petpal.mall.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Exception Handle
 */
@RestControllerAdvice
public class PetPalMallExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object handleException(Exception e, HttpServletRequest req) {
        Result result = new Result();
        result.setResultCode(500);
        //if  exception
        if (e instance of PetPalMallException) {
            result.setMessage(e.getMessage());
        } else {
            e.printStackTrace();
            result.setMessage("Unknown exception, please contact the administrator");
        }
        //Check if request is ajax. If is an ajax request, return the Result json string. If it is not, return the error view.
        String contentTypeHeader = req.getHeader("Content-Type");
        String acceptHeader = req.getHeader("Accept");
        String xRequestedWith = req.getHeader("X-Requested-With");
        if ((contentTypeHeader != null && contentTypeHeader.contains("application/json"))
                || (acceptHeader != null && acceptHeader.contains("application/json"))
                || "XMLHttpRequest".equalsIgnoreCase(xRequestedWith)) {
            return result;
        } else {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("message", e.getMessage());
            modelAndView.addObject("url", req.getRequestURL());
            modelAndView.addObject("stackTrace", e.getStackTrace());
            modelAndView.addObject("author", "Group 9");
            modelAndView.addObject("ltd", "PetPal Mall");
            modelAndView.setViewName("error/error");
            return modelAndView;
        }
    }
}
