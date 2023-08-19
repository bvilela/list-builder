package br.com.bvilela.listbuilder.dto.util;

import java.util.List;

public class CircularList<T> {

    private final List<T> list;
    private int index;

    public CircularList(List<T> list, int initialIndex) {
        this.list = list;
        this.index = initialIndex;
    }

    public T next() {
        if (this.index > this.list.size() - 1) {
            this.index = 0;
        } else {
            this.index++;
        }
        return list.get(this.index);
    }
}
