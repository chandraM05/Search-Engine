/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search_engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 *
 * @author chandra
 */
class MyEntry<K, V> implements Map.Entry<K, V> {

    private final K key;
    private V value;

    public MyEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        V old = this.value;
        this.value = value;
        return old;
    }
}

class term_class_var {

    String word;
    String offset;

    public term_class_var(String word, String offset) {
        this.offset = offset;
        this.word = word;
    }
}

public class QueryProcessing {

    static Stemmer p = new Stemmer();
//    static HashMap<String, HashMap<String, Long>> global_hm = new HashMap<>();
    public static String stop_word[] = {"abbr", "reflist", "redirect", "jsp", "web", "png", "coord", "gr", "com", "tr", "td", "nbsp", "http", "https", "www", "a", "about", "above", "across", "after", "again", "against", "all", "almost", "alone", "along", "already", "also", "although", "always", "among", "an", "and", "another", "any", "anybody", "anyone", "anything", "anywhere", "are", "area", "areas", "around", "as", "ask", "asked", "asking", "asks", "at", "away", "b", "back", "backed", "backing", "backs", "be", "became", "because", "become", "becomes", "been", "before", "began", "behind", "being", "beings", "best", "better", "between", "big", "both", "but", "by", "c", "came", "can", "cannot", "case", "cases", "certain", "certainly", "clear", "clearly", "come", "could", "d", "did", "differ", "different", "differently", "do", "does", "done", "down", "down", "downed", "downing", "downs", "during", "e", "each", "early", "either", "end", "ended", "ending", "ends", "enough", "even", "evenly", "ever", "every", "everybody", "everyone", "everything", "everywhere", "f", "face", "faces", "fact", "facts", "far", "felt", "few", "find", "finds", "first", "for", "four", "from", "full", "fully", "further", "furthered", "furthering", "furthers", "g", "gave", "general", "generally", "get", "gets", "give", "given", "gives", "go", "going", "good", "goods", "got", "great", "greater", "greatest", "group", "grouped", "grouping", "groups", "h", "had", "has", "have", "having", "he", "her", "here", "herself", "high", "high", "high", "higher", "highest", "him", "himself", "his", "how", "however", "i", "if", "important", "in", "interest", "interested", "interesting", "interests", "into", "is", "it", "its", "itself", "j", "just", "k", "keep", "keeps", "kind", "knew", "know", "known", "knows", "l", "large", "largely", "last", "later", "latest", "least", "less", "let", "lets", "like", "likely", "long", "longer", "longest", "m", "made", "make", "making", "man", "many", "may", "me", "member", "members", "men", "might", "more", "most", "mostly", "mr", "mrs", "much", "must", "my", "myself", "n", "necessary", "need", "needed", "needing", "needs", "never", "new", "new", "newer", "newest", "next", "no", "nobody", "non", "noone", "not", "nothing", "now", "nowhere", "number", "numbers", "o", "of", "off", "often", "old", "older", "oldest", "on", "once", "one", "only", "open", "opened", "opening", "opens", "or", "order", "ordered", "ordering", "orders", "other", "others", "our", "out", "over", "p", "part", "parted", "parting", "parts", "per", "perhaps", "place", "places", "point", "pointed", "pointing", "points", "possible", "present", "presented", "presenting", "presents", "problem", "problems", "put", "puts", "q", "quite", "r", "rather", "really", "right", "right", "room", "rooms", "s", "said", "same", "saw", "say", "says", "second", "seconds", "see", "seem", "seemed", "seeming", "seems", "sees", "several", "shall", "she", "should", "show", "showed", "showing", "shows", "side", "sides", "since", "small", "smaller", "smallest", "so", "some", "somebody", "someone", "something", "somewhere", "state", "states", "still", "still", "such", "sure", "t", "take", "taken", "than", "that", "the", "their", "them", "then", "there", "therefore", "these", "they", "thing", "things", "think", "thinks", "this", "those", "though", "thought", "thoughts", "three", "through", "thus", "to", "today", "together", "too", "took", "toward", "turn", "turned", "turning", "turns", "two", "u", "under", "until", "up", "upon", "us", "use", "used", "uses", "v", "very", "w", "want", "wanted", "wanting", "wants", "was", "way", "ways", "we", "well", "wells", "went", "were", "what", "when", "where", "whether", "which", "while", "who", "whole", "whose", "why", "will", "with", "within", "without", "work", "worked", "working", "works", "would", "x", "y", "year", "years", "yet", "you", "young", "younger", "youngest", "your", "yours", "z"};
    public static Set<String> stop_words = new HashSet<String>();
    static int wT = 10000, wB = 5, wC = 20, wE = 1, wR = 1, wI = 25;
//    public static HashSet<String> stop_words = new HashSet<>(Arrays.asList(SET_VALUES));
    static HashMap<String, Long> result_map = new HashMap<>();
    static ArrayList<String> titles = new ArrayList<String>();
    static ArrayList<term_class_var> terms = new ArrayList<term_class_var>();
    static ArrayList<term_class_var> doc_ids = new ArrayList<term_class_var>();
    static Long N = 2440575L;
    static Long startTime;

    static Comparator<term_class_var> c = new Comparator<term_class_var>() {
        public int compare(term_class_var u1, term_class_var u2) {
            return u1.word.compareTo(u2.word);
        }
    };

    static Comparator<Entry<String, Long>> valueComparator = new Comparator<Entry<String, Long>>() {

        @Override
        public int compare(Entry<String, Long> e1, Entry<String, Long> e2) {
            Long v1 = e1.getValue();
            Long v2 = e2.getValue();
//                        System.out.println("gh");
            if (v1.compareTo(v2) < 0) {
                return 1;
            } else if (v1.compareTo(v2) == 0) {
                return 0;
            } else {
                return -1;
            }

        }
    };

    public static void readTer(ArrayList a, String in_file) {

        try {
            File file = new File(in_file);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
//                System.out.println(line);
                String[] k_o = line.split(":");
                term_class_var ob = new term_class_var(k_o[0], k_o[1]);
//                ob.word=k_o[0];
//                ob.offset=Long.parseLong(k_o[1]);
                a.add(ob);
            }

            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            File file = new File(in_file);
//            FileReader fileReader = new FileReader(file);
//            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            StringBuffer stringBuffer = new StringBuffer();
//            String line;
//
//            while ((line = bufferedReader.readLine()) != null) {
//                stringBuffer.append(line);
//                String str = stringBuffer.toString();
//                StringTokenizer words = new StringTokenizer(str.toString(), ":");
//                String key = (words.nextToken()).toLowerCase();
//                String val = (words.nextToken()).toLowerCase();
//                term_class_var t = new term_class_var(key, val);
//                a.add(t);
//                stringBuffer.setLength(0);
//            }
//
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void processQuery() throws FileNotFoundException, IOException {
        String in_file = "/home/chandra/indexes/tertiarytitleindex.txt";
        readTer(doc_ids, in_file);
        in_file = "/home/chandra/indexes/tertiaryindex.txt";
        readTer(terms, in_file);
for(int i=0;i<=441;++i)
            stop_words.add(extract_info.stop_words[i]);
        titles.clear();
//        global_hm.clear();

        System.out.println("Enter Query");
        Scanner scanner = new Scanner(System.in);
        String query = scanner.nextLine();

        startTime = System.currentTimeMillis();

        StringBuilder str = new StringBuilder();
        int len = query.length();
        int i = 0;
        while (i < len) {
            if (query.charAt(i) == ':' || query.charAt(i) == ' ' || (query.charAt(i) >= 'A' && query.charAt(i) <= 'Z') || (query.charAt(i) >= 'a' && query.charAt(i) <= 'z') || (query.charAt(i) >= '0' && query.charAt(i) <= '9')) {
                str.append(query.charAt(i));
            } else {
                str.append(' ');
            }
            i++;
        }
//        System.out.println(str);
//        int field = 0;
//        if (str.toString().contains(":")) {
//            field = 1;
//        }
        StringTokenizer words = new StringTokenizer(str.toString(), " ");
        
        ArrayList<Entry<String, String>> myList = new ArrayList<Entry<String, String>>();
//        ArrayList<String> wtList = new ArrayList<String>();

        ArrayList<String> st_list = new ArrayList<String>();
        char flg = 'n';
        while (words.hasMoreTokens()) {
            String word = (words.nextToken()).toLowerCase();
            if (word.length() > 0) {
//          //    static int wT = 10000, wB = 5, wC = 20, wE = 1, wR = 1, wI = 25;

//                Integer[] wt1_list = new Integer[]{5, 20, 1, 1, 25, 10000};
//                System.out.println(word);
                if (stop_words.contains(word)) {
//                    System.out.println(word);
                    if (!st_list.contains(word)) {
                        st_list.add(word);
//                        System.out.println(word);
                    }
                }

//                System.out.println(word);
                if (str.toString().contains(":")) {

                    if (word.contains("b:") || (!word.contains(":") && flg == 'b')) {
//                    wB=1000;
                        flg = 'b';
                        if (word.contains(":")) {
                            word = word.substring(word.indexOf(':'));
                        }
                        p.add(word.toCharArray(), word.length());
                        word = p.stem();
                        Map.Entry<String, String> e = new MyEntry<String, String>(word, "b");
                        myList.add(e);
//                        System.out.println("b " + word);
//                        wtList.add("B");
                    }
                    if (word.contains("c:") || (!word.contains(":") && flg == 'c')) {
//                    wB=1000;
                        flg = 'c';
                        if (word.contains(":")) {
                            word = word.substring(word.indexOf(':'));
                        }
                        p.add(word.toCharArray(), word.length());
                        word = p.stem();
                        Map.Entry<String, String> e = new MyEntry<String, String>(word, "c");
                        myList.add(e);
                    }
                    if (word.contains("r:") || (!word.contains(":") && flg == 'r')) {
//                    wB=1000;
                        flg = 'r';
                        if (word.contains(":")) {
                            word = word.substring(word.indexOf(':'));
                        }
                        p.add(word.toCharArray(), word.length());
                        word = p.stem();
                         Map.Entry<String, String> e = new MyEntry<String, String>(word, "r");
                        myList.add(e);
                    }
                    if (word.contains("e:") || (!word.contains(":") && flg == 'e')) {
//                    wB=1000;
                        flg = 'e';
                        if (word.contains(":")) {
                            word = word.substring(word.indexOf(':'));
                        }
                        p.add(word.toCharArray(), word.length());
                        word = p.stem();
                        Map.Entry<String, String> e = new MyEntry<String, String>(word, "e");
                        myList.add(e);
                    }
                    if (word.contains("t:") || (!word.contains(":") && flg == 't')) {
//                    wB=1000;
                        flg = 't';
                        if (word.contains(":")) {
                            word = word.substring(word.indexOf(':'));
                        }
                        p.add(word.toCharArray(), word.length());
                        word = p.stem();
                        Map.Entry<String, String> e = new MyEntry<String, String>(word, "t");
                        myList.add(e);
                    }
                    if (word.contains("i:") || (!word.contains(":") && flg == 'i')) {
//                    wB=1000;
                        flg = 'i';
                        if (word.contains(":")) {
                            word = word.substring(word.indexOf(':'));
                        }
                        p.add(word.toCharArray(), word.length());
                        word = p.stem();
                        Map.Entry<String, String> e = new MyEntry<String, String>(word, "i");
                        myList.add(e);
//                        System.out.println("i " + word);
                    }
                } else {
                    p.add(word.toCharArray(), word.length());
                    word = p.stem();
                    Map.Entry<String, String> e = new MyEntry<String, String>(word, "n");
                    myList.add(e);
//                    System.out.println(e.getKey()+" "+e.getValue());
                }

            }
        }
//        System.out.println("myList");
//        for(int j=0;j<myList.size();j++){
//            System.out.println(myList.get(j).getKey()+" "+myList.get(j).getValue());
//        }
        System.out.println("myList");
        if (st_list.size() != myList.size()) {
//            System.out.println("yes11");
            for (int j = 0; j < st_list.size(); j++) {
//                System.out.println("yes");
                Map.Entry<String, String> e = new MyEntry<String, String>("of", "n");
//                System.out.println(e.getKey()+" "+e.getValue());
               for(int k=0;k<myList.size();k++)
                if (myList.get(k).getKey().equals(st_list.get(j))) {
//                    System.out.println("no");
//                    wtList.remove(myList.indexOf(st_list.get(j)));
                    myList.remove(k);

                }
            }
        }
//        System.out.println("myList");
//        for(int j=0;j<myList.size();j++){
//            System.out.println(myList.get(j).getKey()+" "+myList.get(j).getValue());
//        }

        FindDoc_id(myList);
        FindResult();

    }

    public static void FindDoc_id(ArrayList<Entry<String, String>> myList) throws FileNotFoundException, IOException {

        String pri_file = "/home/chandra/indexes/mainindex.txt";
        String sec_file = "/home/chandra/indexes/secondaryindex.txt";
        int flg_tit = 0;

        for (int i = 0; i < myList.size(); i++) {

            String key = (String) myList.get(i).getKey();
            int match = 1;
            int index = Collections.binarySearch(terms, new term_class_var((String) myList.get(i).getKey(), "0"), c);
            if (index < 0) {
                index = -(index + 2);
                match = 0;
            }
            long sec_ind = Long.parseLong(terms.get(index).offset);
            wT = 10000;
            wB = 5;
            wC = 20;
            wE = 1;
            wR = 1;
            wI = 25;
            int is_there = secondary(sec_ind, (String) myList.get(i).getKey(), (String) myList.get(i).getValue(), match, sec_file, pri_file, flg_tit);
        }
    }

    public static int secondary(Long sec_ind, String term, String f, int match, String file, String pri_file, int flg_tit) throws FileNotFoundException, IOException {
//        long sTime = System.currentTimeMillis();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(sec_ind);
        long pri_ind = -1;
        if (match == 1) {
            String str = raf.readLine();
            StringTokenizer words = new StringTokenizer(str.toString(), ":");
            String term1 = words.nextToken();
            pri_ind = Long.parseLong(words.nextToken());
        } else {
            for (int i = 0; i < 50; i++) {
                String str = raf.readLine();
                StringTokenizer words = new StringTokenizer(str.toString(), ":");
                String term1 = words.nextToken();
                if (term.equals(term1)) {
                    pri_ind = Long.parseLong(words.nextToken());
                    break;
                }
            }

        }
        if (pri_ind != -1) {

            primary(f, pri_ind, term, pri_file, flg_tit);
            return 1;
        }
        return -1;
    }

    public static void primary(String f, Long pri_ind, String term, String file, int flg_tit) throws FileNotFoundException, IOException {
//        long sTime = System.currentTimeMillis();
StringBuilder sb = new StringBuilder();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        raf.seek(pri_ind);
        
        char c1 = (char) raf.read();
        int cnt = 0;
        for(;;)  {
            if(c1 == '\n' || cnt >= 12000)
                break;            
            else if (c1 == ';') {
                cnt++;
            }
            sb.append(c1);
            c1 = (char) raf.read();
        }
        String str = sb.toString();
        if (flg_tit == 0) {
            processPosting(str, f);
        } else {
            StringTokenizer words = new StringTokenizer(str.toString(), ":");
                String term1 = words.nextToken();
                String title = words.nextToken();
            titles.add(title);
        }
    }

    public static void processPosting(String str, String f) {
//         long sTime = System.currentTimeMillis();
        String[] tokens = str.split("\\:");
        StringTokenizer posting_list = new StringTokenizer(tokens[1].toString(), ";");
        HashMap<String, Long> tmp = new HashMap<String, Long>();
        int champs = 0;
        wT = 9000;
        switch (f) {
            case "n":
                wT = 10000;
                break;
            case "b":
                wB = 10000;
                break;
            case "c":
                wC = 10000;
                break;
            case "r":
                wR = 10000;
                break;
            case "e":
                wE = 10000;
                break;
            case "i":
                wI = 10000;
                break;
            case "t":
                wB = 20000;
                break;
        }

        while (posting_list.hasMoreTokens()) {
            String doc_list = (posting_list.nextToken());
            int len = doc_list.length();
            int i = 0;
            String doc_id = "";
            Long weight = 0L;
            while (true) {

                while (i < len && doc_list.charAt(i) >= '0' && doc_list.charAt(i) <= '9') {
                    doc_id = doc_id + doc_list.charAt(i);
                    i++;
                }
                if (i < len && doc_list.charAt(i) == 'T') {
                    i++;
                    String freq = "";
                    while (i < len && doc_list.charAt(i) >= '0' && doc_list.charAt(i) <= '9') {
                        freq = freq + doc_list.charAt(i);
                        i++;
                    }
                    int fq = Integer.parseInt(freq);
                    weight = weight + wT * fq;
                }
                if (i < len && doc_list.charAt(i) == 'I') {
                    i++;
                    String freq = "";
                    while (i < len && doc_list.charAt(i) >= '0' && doc_list.charAt(i) <= '9') {
                        freq = freq + doc_list.charAt(i);
                        i++;
                    }
                    int fq = Integer.parseInt(freq);
                    weight = weight + wI * fq;

                }
                if (i < len && doc_list.charAt(i) == 'C') {
                    i++;
                    String freq = "";
                    while (i < len && doc_list.charAt(i) >= '0' && doc_list.charAt(i) <= '9') {
                        freq = freq + doc_list.charAt(i);
                        i++;
                    }
                    int fq = Integer.parseInt(freq);
                    weight = weight + wC * fq;

                }
                if (i < len && doc_list.charAt(i) == 'B') {
                    i++;
                    String freq = "";
                    while (i < len && doc_list.charAt(i) >= '0' && doc_list.charAt(i) <= '9') {
                        freq = freq + doc_list.charAt(i);
                        i++;
                    }
                    int fq = Integer.parseInt(freq);
                    weight = weight + wB * fq;

                }
                if (i < len && doc_list.charAt(i) == 'R') {
                    i++;
                    String freq = "";
                    while (i < len && doc_list.charAt(i) >= '0' && doc_list.charAt(i) <= '9') {
                        freq = freq + doc_list.charAt(i);
                        i++;
                    }
                    int fq = Integer.parseInt(freq);
                    weight = weight + wR * fq;

                }
                if (i < len && doc_list.charAt(i) == 'E') {
                    i++;
                    String freq = "";
                    while (i < len && doc_list.charAt(i) >= '0' && doc_list.charAt(i) <= '9') {
                        freq = freq + doc_list.charAt(i);
                        i++;
                    }
                    int fq = Integer.parseInt(freq);
                    weight = weight + wE * fq;
                }
                if (i == len) {
                    break;
                }
            }
            if (!result_map.containsKey(doc_id)) {
                result_map.put(doc_id, weight);
            } else {
                result_map.put(doc_id, result_map.get(doc_id) + weight);
            }
            champs++;
        }

    }

    public static void retrive_titles(List<Entry<String, Long>> listOfEntries) throws IOException {

        String pri_file = "/home/chandra/indexes/titleindex.txt";
        int flg_tit = 1;

        int count = 0;
        for (Entry<String, Long> entry : listOfEntries) {
            int match = 1;
            String key = entry.getKey();
            int index = Collections.binarySearch(doc_ids, new term_class_var(key, "0"), c);
            if (index < 0) {
                index = (-index - 2);
                match = 0;
            }
            long sec_ind = Long.parseLong(doc_ids.get(index).offset);
            int is_there = secondary(sec_ind, key, "n", match, "/home/chandra/indexes/secondarytitleindex.txt", pri_file, flg_tit);
//            hm.put(key, entry.getValue());
//            result_map.put(, hm);             
            count++;
            if (count == 10) {
                break;
            }
        }
//        result_map.put(term, hm);
        long endTime = System.currentTimeMillis();
        for (int i = 0; i < titles.size(); i++) {
            System.out.println(i + 1 + " " + titles.get(i));
            if (i == 9) {
                break;
            }
        }
        long totalTime = endTime - startTime;
        System.out.println("Resaponse time =  " + totalTime + "msec");

    }

    public static void FindResult() throws IOException {
        /*HashMap<String, Long> hm;
        Iterator it = global_hm.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            hm = (HashMap<String, Long>) pair.getValue();
            long df = hm.size();
            Iterator it1 = hm.entrySet().iterator();
            while (it1.hasNext()) {
                Map.Entry pair1 = (Map.Entry) it1.next();
                Double wt = (Math.log((Long) pair1.getValue()) / Math.log(10)) * (Math.log(N / df) / Math.log(10));
                String str = (String) pair1.getKey();
                if (result_map.containsKey(str)) {
                    result_map.put(str, result_map.get(str) + wt);
                } else {
                    result_map.put(str, wt);
                }
            }
        }*/

        //sort 
        // Sort method needs a List, so let's first convert Set to List in Java
//        long sTime = System.currentTimeMillis();
        Set<Entry<String, Long>> entries = result_map.entrySet();
        List<Entry<String, Long>> listOfEntries = new ArrayList<Entry<String, Long>>(entries);
        // sorting HashMap by values using comparator
        Collections.sort(listOfEntries, valueComparator);
        for (Entry<String, Long> entry : listOfEntries) {
        }
        // find titles
        retrive_titles(listOfEntries);
    }

}
