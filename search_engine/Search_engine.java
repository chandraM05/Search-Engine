
package search_engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

//import search_engine.SAXparser;
/*
 * @author chandra
 */
public class Search_engine {
    
//    public static HashMap<String, HashMap<String, HashMap<String,Integer>>> database = new HashMap<String,HashMap<String,HashMap<String,Integer>>>();
    public static Set<String> stop_words = new HashSet<String>();
    public static TreeMap<String,TreeMap<String, HashMap<String,Integer>>> database = new TreeMap<String,TreeMap<String, HashMap<String,Integer>>>();
    
//    public static String input_file="/media/chandra/New Volume/ire/wiki_doc.xml";
    public static String input_file="wiki.xml";
    public static String output_file="wiki_new.txt";
    
    public static void main(String[] args) throws IOException 
    {
//        input_file= args[1];
//        output_file=args[2];
        
        for(int i=0;i<=441;++i)
            stop_words.add(extract_info.stop_words[i]);
        
//        Saxparser sax= new Saxparser();
//        sax.SAXparser();

        post_scan ps = new post_scan();
        ps.merge_all();

    }
    
}