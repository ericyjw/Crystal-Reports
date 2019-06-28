package com.lister.Project.domain;

/**
 * Contains the column to be sorted and the order to be sorted.
 */
public class SortCondition {
    private String col;
    private String sortDirection;

    public SortCondition(String col, String sortDirection) {
        this.col = col;
        this.sortDirection = sortDirection;
    }

    public String getCol() {
        return col;
    }

    public String getSortDirection() {
        return sortDirection;
    }
}
