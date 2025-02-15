package org.wingate.feuille.lib.table;

import java.util.ArrayList;
import java.util.List;

public class SubsFolder<T> {
    private List<T> list;

    public SubsFolder(List<T> list) {
        this.list = list;
    }

    public SubsFolder(T type){
        List<T> list;
        list = new ArrayList<>();
        this.list = list;
    }
}
