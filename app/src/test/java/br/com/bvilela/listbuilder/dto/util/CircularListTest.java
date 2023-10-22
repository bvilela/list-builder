package br.com.bvilela.listbuilder.dto.util;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CircularListTest {

    private static final String P1 = "Person1";
    private static final String P2 = "Person2";
    private static final String P3 = "Person3";
    private static final String P4 = "Person4";
    private static final String P5 = "Person5";

    private final List<String> list = List.of(P1, P2, P3, P4, P5);

    @Test
    void circularListInitialIndexFirst() {
        CircularList<String> circularList = new CircularList<>(list,0);
        assertEquals(P2, circularList.next());
        assertEquals(P3, circularList.next());
        assertEquals(P4, circularList.next());
        assertEquals(P5, circularList.next());
        assertEquals(P1, circularList.next());
    }

    @Test
    void circularListInitialIndexLast() {
        CircularList<String> circularList = new CircularList<>(list,list.size()-1);
        assertEquals(P1, circularList.next());
        assertEquals(P2, circularList.next());
        assertEquals(P3, circularList.next());
        assertEquals(P4, circularList.next());
        assertEquals(P5, circularList.next());
        assertEquals(P1, circularList.next());
    }

}
