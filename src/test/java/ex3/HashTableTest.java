package ex3;

import ex2.HashTable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HashTableTest {


    @Test
    void count() {
        ex2.HashTable ht = new ex2.HashTable();
        ht.put("0", "0");
        Assertions.assertEquals(ht.count(), 1);          //  comprovem que es conta un element sol
        ht.put("11", "11");
        Assertions.assertEquals(ht.count(), 2);          // comprovem que tot i estar en el mateix hash, els elements es conten
        ht.drop("11");
        Assertions.assertEquals(ht.count(), 1);          // comprovem que després d'esborrar un element segueix funcionant
    }

    @Test
    void size() {
        // ????
    }

    @Test
    void put() {
        // comprovar que passa quan :     (Afegim una clau que ja existeix V, que no exsteix V, que no existeix pero existeix el seu hash V, que existeix i existeix el seu hash)
        ex2.HashTable ht = new ex2.HashTable();
        ht.put("0", "0");
        Assertions.assertEquals(ht.count(), 1);         // Comprovem que la conta de items funciona al sumar un nou element HashTable.  (Falla sense les correccions) $1
        Assertions.assertEquals(ht.get("0"), "0");       // Comprovem que ens retorna l'element corrresponent
        // afegim elements que coincideixen en hash
        ht.put("0", "1");
        Assertions.assertEquals(ht.count(), 1);          // Comprovem que no ha afegit una clau repetida amb el count
        Assertions.assertEquals(ht.get("0"), "0");       // Comprovem que retorna el valor del primer put
        ht.drop("0");
        Assertions.assertEquals(ht.get("0"), null);       // Comprovem que retorna null perque ha esborrat el unic put que hi habia
        ht.put("11", "11");
        ht.put("22", "22");
        Assertions.assertEquals(ht.get("11"), "11");    // comprovem que ha afegit un element nou  tot i estar linkat perque ja existeix el seu hash
        Assertions.assertEquals(ht.get("22"), "22");    // comprovem que ha afegit un element nou  tot i estar linkat perque ja existeix el seu hash


    }

    @Test
    void get() {
        // comprovar que passa quan :     (Demanem una clau que ja existeix, que no exsteix, que no existeix pero existeix el seu hash, que existeix i existeix el seu hash)

        ex2.HashTable ht = new ex2.HashTable();
        ht.put("0", "0");
        ht.put("1", "1");
        Assertions.assertEquals(ht.get("0"), "0");       // Comprovem que ens retorna l'element corrresponent
        Assertions.assertEquals(ht.get("1"), "1");
        Assertions.assertEquals(ht.get("2"), null);         // Comprovem si retorna null quan demanem un element que no existeix amb una hash que actualment esta buida
        Assertions.assertEquals(ht.get("11"), null);         // Comprovem si retorna null quan demanem un element que existeix el seu hash pero no existeix la clau.  (Falla )  $3
        ht.put("11", "11");
        ht.put("22", "22");
        Assertions.assertEquals(ht.get("11"), "11");         // Comprovem si ens retorna un element que ja existeix el seu hash i està lincat
        Assertions.assertEquals(ht.get("22"), "22");         // Comprovem si ens retorna un element que ja existeix el seu hash i  està doblement lincat
        Assertions.assertEquals(ht.get("33"), null);         // Comprovem que passa si demanem un numero que no existeix pero que coincideix en hash amb 3 elements
    }

    @Test
    void drop1() {

        // comprovar que passa quan :     (S'esborra una clau que existeix, que no exsteix, que no existeix pero existeix el seu hash)
        ex2.HashTable ht = new ex2.HashTable();
        ht.put("0", "0");                              // Afegim dos elements per comprovar que també està sumant
        ht.put("1", "1");
        ht.drop("1");
        Assertions.assertEquals(ht.count(), 1);         // Comprovem que la conta de items funciona al treure un element (Falla sense les correccions) $2
        Assertions.assertEquals(ht.get("1"), null);      // comprovem que retorna null perque l'element ja no esta
        // afegim unes claus que sabem que coincideixen en hash
        ht.put("11", "11");
        ht.put("22", "22");
        ht.drop("0"); // esborrem un dels elements que estan lincats
        Assertions.assertEquals(ht.count(), 2);       //  comprovem que hi ha tants elements com hi ha d'haver despres desborrar un element lincat
        Assertions.assertEquals(ht.get("0"), null);   // comprovem que realment ja no esta aquest element a la taula
        Assertions.assertEquals(ht.get("11"), "11");    // Comprovem que no ha esborrat la resta d'elements lincats
    }

    @Test
    void drop2() {
        ex2.HashTable ht = new HashTable();
        ht.put("1", "1");
        ht.put("0", "0");
        ht.put("11", "11");
        ht.put("22", "22");
        Assertions.assertEquals(ht.toString(), "\n bucket[0] = [0, 0] -> [11, 11] -> [22, 22]\n" +
                " bucket[1] = [1, 1]");
        ht.drop("1");
        Assertions.assertEquals(ht.toString(), "\n bucket[0] = [0, 0] -> [11, 11] -> [22, 22]");
        ht.drop("0");
        Assertions.assertEquals(ht.toString(), "\n bucket[0] = [11, 11] -> [22, 22]");   // Al comprovar si esborra dona error correccions  $5
        ht.put("0","0");
        Assertions.assertEquals(ht.toString(),"\n bucket[0] = [11, 11] -> [22, 22] -> [0, 0]");
        ht.drop("0");
        Assertions.assertEquals(ht.toString(),"\n bucket[0] = [11, 11] -> [22, 22]");   // Comprovem quan esborrem l'ultim element de la linked list
        ht.drop("1");
        Assertions.assertEquals(ht.toString(),"\n bucket[0] = [11, 11] -> [22, 22]");   // Comprovem quan esborrem un element sense colisio que no existeix
        ht.drop("0");
        Assertions.assertEquals(ht.toString(),"\n bucket[0] = [11, 11] -> [22, 22]");   // Comprovem quan esborrem un element amb colisio que no existeix
        ht.put("0","0");
        ht.drop("33");
        Assertions.assertEquals(ht.toString(),"\n bucket[0] = [11, 11] -> [22, 22] -> [0, 0]");  // Comprovem quan esborrem un element amb colisio triple que no existeix
        ht.drop("22");
        Assertions.assertEquals(ht.toString(),"\n bucket[0] = [11, 11] -> [0, 0]");  // Comprovem quan esborrem un element amb colisio triple en una posicio intermitja


    }

}