package com.example.data;

/**
 * The Pager content for chunk-by-chunk traversal of large result sets.
 */
public class Pager {

    private final int pageSize;
    private final int total;
    private final int currentPage;
    private final int lastPage;

    public Pager(int pageSize, int total, int currentPage, int lastPage) {
        this.pageSize = pageSize;
        this.total = total;
        this.currentPage = currentPage;
        this.lastPage = lastPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotal() {
        return total;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getLastPage() {
        return lastPage;
    }
}
