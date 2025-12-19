package model;

import java.util.List;

public class PageResult<T> {
    private List<T> list;
    private int totalCount;
    private int totalPage;
    private int currentPage;
    private int pageSize;

    public PageResult(List<T> list, int totalCount, int currentPage, int pageSize) {
        this.list = list;
        this.totalCount = totalCount;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
    }

    public List<T> getList() {
        return list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
