package com.dao.mongdb.base;

import java.io.Serializable;
import java.util.List;

/**
 * @author 阿导
 * @version BUILD1001
 * @fileName com.dao.base.mongoutil.Page.java
 * @CopyRright (c) 2017-bxm：万物皆导
 * @created 2018-01-05 16:03:00
 * @modifier 阿导
 * @updated 2018-01-05 16:03:00
 * @description
 */
public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1571744502699069043L;
    /**
     * 当前页面大小
     */
    private Integer pageSize;
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 总页数
     */
    private Integer pages;

    /**
     * 是否有下一页
     */
    private Boolean hasNexPage;

    /**
     * 是否有上一页
     */
    private Boolean hasPrePage;

    /**
     * 是否为第一页
     */
    private Boolean isFirstPage;

    /**
     * 是否为最后一页
     */
    private Boolean isLastPage;

    /**
     * 当前页大小
     */
    private Integer size;

    /**
     * 结果集
     */
    private List<T> list;

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Boolean getHasNexPage() {
        return hasNexPage;
    }

    public void setHasNexPage(Boolean hasNexPage) {
        this.hasNexPage = hasNexPage;
    }

    public Boolean getHasPrePage() {
        return hasPrePage;
    }

    public void setHasPrePage(Boolean hasPrePage) {
        this.hasPrePage = hasPrePage;
    }

    public Boolean getFirstPage() {
        return isFirstPage;
    }

    public void setFirstPage(Boolean firstPage) {
        isFirstPage = firstPage;
    }

    public Boolean getLastPage() {
        return isLastPage;
    }

    public void setLastPage(Boolean lastPage) {
        isLastPage = lastPage;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Page() {
    }

    public Page(List<T> list, Integer pageNum, Integer pageSize, Long total) {
       this(list,pageNum,pageSize,total,false);
    }

    public Page(List<T> list, Integer pageNum, Integer pageSize, Long total, Boolean hasCount) {
        if(list==null&&total==null){
            this.list = null;
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.total = 0l;
            this.size=0;
            this.pages=0;
        }else {
            this.list = list;
            this.pageNum = pageNum;
            this.pageSize = pageSize;
            this.total = total;
            if(hasCount){
                this.size = list.size()!=0?list.size()-1:0;
            }else{
                this.size = list.size();
            }
            this.pages = Integer.valueOf(String.valueOf(Math.ceil(total / (double) pageSize)).split("\\.")[0]);
        }
        if (pageNum == 1) {
            this.isFirstPage = true;
        } else {
            this.isFirstPage = false;
        }
        if (pageNum == this.pages) {
            this.isLastPage = true;
        } else {
            this.isLastPage = false;
        }

        if (pages > pageNum) {
            this.hasNexPage = true;
        } else {
            this.hasNexPage = false;
        }
        if (pageNum > 1) {
            this.hasPrePage = true;
        } else {
            this.hasPrePage = false;
        }
    }

}
