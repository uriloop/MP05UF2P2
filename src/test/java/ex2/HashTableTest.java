package ex2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class HashTableTest {

    @Test
    void putOnEmptyTable() {
        HashTable ht = new HashTable();
        ht.put("1","1");
        Assertions.assertEquals(ht.get("1"),"1");
        Assertions.assertEquals(ht.count(),1);
        Assertions.assertEquals(ht.size(),16);
    }

    @Test
    void size() {

    }

    @Test
    void put() {
    }

    @Test
    void get() {
    }

    @Test
    void drop() {
    }

    @Test
    void testToString() {
    }
}