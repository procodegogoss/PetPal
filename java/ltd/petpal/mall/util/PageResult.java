package ltd.petpal.mall.util;

import java.io.Serializable;
import java.util.List;

/**
 * page util
 *
 * @author 13
 */
public class PageResult implements Serializable {

    //totalCount
    private int totalCount;
    //page items count 
    private int pageSize;
    //totalPage
    private int totalPage;
    //current page
    private int currPage;
    //list 
    private List<?> list;

    /**
     * pages 
     *
     * @param list       
     * @param totalCount 
     * @param pageSize   
     * @param currPage   
     */
    public PageResult(List<?> list, int totalCount, int pageSize, int currPage) {
        this.list = list;
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currPage = currPage;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrPage() {
        return currPage;
    }

    public void setCurrPage(int currPage) {
        this.currPage = currPage;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<?> list) {
        this.list = list;
    }

}
