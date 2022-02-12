package ex3;

// Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
// Modified by Fernando Porrino Serrano for academic purposes.

import java.util.ArrayList;

/**
 * Implementació d'una taula de hash sense col·lisions.
 * Original source code: https://gist.github.com/amadamala/3cdd53cb5a6b1c1df540981ab0245479
 */
public class HashTable {
    private int SIZE = 16;
    private int ITEMS = 0;
    private HashEntry[] entries = new HashEntry[SIZE];

    public int count() {
        return this.ITEMS;
    }

    public int size() {
        return this.SIZE;
    }

    /**
     * Permet afegir un nou element a la taula.
     *
     * @param key   La clau de l'element a afegir.
     * @param value El propi element que es vol afegir.
     */
    public void put(String key, String value) {
        int hash = getHash(key);                                                // troba el numero hash a partir de la clau
        final HashEntry hashEntry = new HashEntry(key, value);                 // crea la nova entry
        boolean keyExists = false;        /* Afegit  $4  per a evtar claus repetides   */

        if (entries[hash] == null) {                                             // si en aquest hash no hi ha cap entry
            entries[hash] = hashEntry;                                          // l'afegeix
            ITEMS++;  /* Afegit   $1   no sumava items   */
        } else {                                                                  // si existeix
            HashEntry temp = entries[hash];                                     // temp apunta a la linked de entries
            while (temp.next != null) {                                          // si el següent a temp no és null
                if (temp.key.equals(key)) keyExists = true;
                temp = temp.next;                                               // saltem al següent
            }
            if (!keyExists && !temp.key.equals(key)) {               /* Afegit  $4   si la clau no existeix la guarda */
                temp.next = hashEntry;                                              // saltem al següent que és el primer espai buit
                hashEntry.prev = temp;                                              // li indiquem a la var prev quin és el element previ
                ITEMS++;      /* Afegit   $1   no sumava items   */
            }
        }


    }

    /**
     * Permet recuperar un element dins la taula.
     *
     * @param key La clau de l'element a trobar.
     * @return El propi element que es busca (null si no s'ha trobat).
     */
    public String get(String key) {
        int hash = getHash(key);
        if (entries[hash] != null) {                 // si hi ha alguna entry
            HashEntry temp = entries[hash];         // apuntem temp a la linked

            while (!temp.key.equals(key))               // mentres la key de temp no sigui igual a la que busquem,
                /**/ if (temp.next == null)
                return null;      // $3 // Afegit ja que pot trobar elements a amb el mateix hash pero sense la clau que busca Ara retorna null si no troba la clau

            else temp = temp.next;                   // fem next
            return temp.value;                      // quan la troba la retorna
        }

        return null;
    }

    /**
     * Permet esborrar un element dins de la taula.
     *
     * @param key La clau de l'element a trobar.
     */
    public void drop(String key) {
        int hash = getHash(key);
        boolean existeix = true;
        if (entries[hash] != null) {
            HashEntry temp = entries[hash];
            while (!temp.key.equals(key)) {
                if (temp.next == null) {                /* afegits per solucionar quan esborrem un element que colisiona pero no existeix*/
                    existeix = false;
                    break;
                }
                temp = temp.next;
            }

            if (existeix) {
                if (temp.prev == null && temp.next == null)
                    entries[hash] = null;        /* Afegit   $5  per evitar que esborri totes les entries si queden elements posteriors */    //esborrar element únic (no col·lissió)
                else {
                    if (temp.next != null && temp.prev == null) {         /* Afegit $5  per els casos en que es el primer element de la llista */
                        entries[hash] = temp.next;
                    } else if (temp.next != null && temp.prev != null) {            /* Afegit  cas en que és un element intermig de la linked  */
                        temp.next.prev = temp.prev;   //esborrem temp, per tant actualitzem l'anterior al següent
                        temp.prev.next= temp.next;
                    } else if (temp.next == null) {                             /* Afegit si esborrem l'ultim element de la linked */
                        temp.prev.next = null;
                    }
                    //esborrem temp, per tant actualitzem el següent de l'anterior
                }
                /* Afegit    $2     */
                ITEMS--;
            }
        }
    }

    private int getHash(String key) {
        // piggy backing on java string
        // hashcode implementation.
        return key.hashCode() % SIZE;
    }

    private class HashEntry {
        String key;
        String value;

        // Linked list of same hash entries.
        HashEntry next;
        HashEntry prev;

        public HashEntry(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
            this.prev = null;
        }

        @Override
        public String toString() {
            return "[" + key + ", " + value + "]";
        }
    }

    @Override
    public String toString() {
        int bucket = 0;
        StringBuilder hashTableStr = new StringBuilder();
        for (HashEntry entry : entries) {
            if (entry == null) {
                bucket++;
                continue;
            }

            hashTableStr.append("\n bucket[")
                    .append(bucket)
                    .append("] = ")
                    .append(entry.toString());
            bucket++;
            HashEntry temp = entry.next;
            while (temp != null) {
                hashTableStr.append(" -> ");
                hashTableStr.append(temp.toString());
                temp = temp.next;
            }
        }
        return hashTableStr.toString();
    }

    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     *
     * @param key La clau que es farà servir per calcular col·lisions.
     * @return Una clau que, de fer-se servir, provoca col·lisió amb la que s'ha donat.
     */
    public String getCollisionsForKey(String key) {
        return getCollisionsForKey(key, 1).get(0);
    }

    /**
     * Permet calcular quants elements col·lisionen (produeixen la mateixa posició dins la taula de hash) per a la clau donada.
     *
     * @param key      La clau que es farà servir per calcular col·lisions.
     * @param quantity La quantitat de col·lisions a calcular.
     * @return Un llistat de claus que, de fer-se servir, provoquen col·lisió.
     */
    public ArrayList<String> getCollisionsForKey(String key, int quantity) {
        /*
          Main idea:
          alphabet = {0, 1, 2}

          Step 1: "000"
          Step 2: "001"
          Step 3: "002"
          Step 4: "010"
          Step 5: "011"
           ...
          Step N: "222"

          All those keys will be hashed and checking if collides with the given one.
        * */

        final char[] alphabet = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        ArrayList<Integer> newKey = new ArrayList();
        ArrayList<String> foundKeys = new ArrayList();

        newKey.add(0);
        int collision = getHash(key);
        int current = newKey.size() - 1;

        while (foundKeys.size() < quantity) {
            //building current key
            String currentKey = "";
            for (int i = 0; i < newKey.size(); i++)
                currentKey += alphabet[newKey.get(i)];

            if (!currentKey.equals(key) && getHash(currentKey) == collision)
                foundKeys.add(currentKey);

            //increasing the current alphabet key
            newKey.set(current, newKey.get(current) + 1);

            //overflow over the alphabet on current!
            if (newKey.get(current) == alphabet.length) {
                int previous = current;
                do {
                    //increasing the previous to current alphabet key
                    previous--;
                    if (previous >= 0) newKey.set(previous, newKey.get(previous) + 1);
                }
                while (previous >= 0 && newKey.get(previous) == alphabet.length);

                //cleaning
                for (int i = previous + 1; i < newKey.size(); i++)
                    newKey.set(i, 0);

                //increasing size on underflow over the key size
                if (previous < 0) newKey.add(0);

                current = newKey.size() - 1;
            }
        }

        return foundKeys;
    }

    public static void main(String[] args) {
        HashTable hashTable = new HashTable();

        // Put some key values.
        for (int i = 0; i < 30; i++) {
            final String key = String.valueOf(i);
            hashTable.put(key, key);
        }

        // Print the HashTable structure
        log("****   HashTable  ***");
        log(hashTable.toString());
        log("\nValue for key(20) : " + hashTable.get("20"));
    }

    private static void log(String msg) {
        System.out.println(msg);
    }
}