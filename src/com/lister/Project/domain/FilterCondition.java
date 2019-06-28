package com.lister.Project.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all the filters to be filtered.
 */
public class FilterCondition {
    private List<Filter> filterList;

    public FilterCondition(List<String> filterCol, List<String> condition) throws InvalidFiltersException {
        this.filterList = new ArrayList<>();

        for (int i = 0; i < filterCol.size(); i ++) {
            String col = filterCol.get(i);
            String cond = condition.get(i);

            if((col == "" && cond != "") || (col != "" && cond == "")) {
                throw new InvalidFiltersException();
            }

            Filter filter = (col == "" || cond == "") ? null : new Filter(filterCol.get(i), condition.get(i));
            if(filter != null) {
                this.filterList.add(filter);
            }
        }
    }

    public Filter getFilterAt(int index) {
        return this.filterList.get(index);
    }

    public int size() {
        return this.filterList.size();
    }


}
