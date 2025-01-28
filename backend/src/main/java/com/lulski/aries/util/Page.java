package com.lulski.aries.util;

import java.util.List;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Util class to paginate json response
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP")
public class Page<T> {
    private final long totalItems;
    private final List<T> items;
    private final int currentPage;
    private final int pageSize;

    /**
     * main constructor
     *
     * @param totalItems
     * @param items
     * @param currentPage
     * @param pageSize
     */
    public Page(long totalItems, List<T> items, int currentPage, int pageSize) {
        this.totalItems = totalItems;
        this.items = items;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public long getTotalItems() {
        return totalItems;
    }

    @SuppressFBWarnings(value = "EI_EXPOSE_REP")
    public List<T> getItems() {
        return items;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }
}
