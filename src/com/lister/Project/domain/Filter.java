package com.lister.Project.domain;

/**
 * Contains the filter condition and the column to be filtered.
 */
public class Filter {
    private String col;
    private String condition;

    public Filter(String col, String condition){
        this.col = col;
        this.condition = condition;
    }

    public String getCol() {
        return col;
    }

    public String getCondition() {
        return condition;
    }

}
