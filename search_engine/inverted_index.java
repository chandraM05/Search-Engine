
package search_engine;

import java.util.HashMap;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

/*
 * @author chandra
 */
public class inverted_index {
    

    public void doc_tit_write()
    {
        BufferedWriter bw_it = null;
        FileWriter fw_it = null;
        
        
        try
        {
           
            File id_title = new File("/media/chandra/New Volume/ire/trash/temp_id_title"+Saxparser.index+".txt");
//            File id_title = new File("final/id_title_Map.txt");
            if (id_title.exists()==false) 
                id_title.createNewFile();
            fw_it = new FileWriter("/media/chandra/New Volume/ire/trash/temp_id_title"+Saxparser.index+".txt");
            bw_it = new BufferedWriter(fw_it);
            for(Map.Entry<String,String> Entry1 : Saxparser.id_tit.entrySet())
            {
                bw_it.write(Entry1.getKey()+":"+Entry1.getValue()+'\n');
                bw_it.flush();
            }
            bw_it.close();
            fw_it.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        } 
    }

    //writting the inverted index to file . Sorted strings in ascending order
    public void write_toFile()
    {
        BufferedWriter bw = null;
        FileWriter fw = null;
        
        //Inserting into treeMap .. to obtain the sorted order
        try
        {
            fw = new FileWriter("/media/chandra/New Volume/ire/trash/temp"+Saxparser.index+".txt");
            bw = new BufferedWriter(fw);

            //storing the numbers like doc id and frequencies in hexadecimal format to reduce index size
            for(Map.Entry<String, TreeMap<String, HashMap<String, Integer>>> Entry1 : Search_engine.database.entrySet())
            {
//              HashMap<String ,HashMap<String,Integer> >2D = Search_engine.database.get(s);
                bw.write(Entry1.getKey()+":");
                Boolean flag=true;
                for (Map.Entry<String, HashMap<String, Integer> > idfreq : Search_engine.database.get(Entry1.getKey()).entrySet()) 
                {
//                    String idp=Integer.toHexString(Integer.parseInt(idfreq.getKey()));
//                    bw.write(idp+" ");
                    if(!flag)
                        bw.write(";");
                    flag=false;
                    bw.write(idfreq.getKey());
//                    System.out.println("no of docs :"+idfreq.getKey());
                    for (HashMap.Entry<String, Integer> freq : idfreq.getValue().entrySet()) 
                    {
                        if(freq.getValue()!=0)
                        {
//                            String term_freq=Integer.toHexString(freq.getValue());
//                            bw.write(freq.getKey()+term_freq);
                            bw.write(freq.getKey()+freq.getValue());
                            bw.flush();
                        }
                    }
                    bw.flush();
//                    bw.write(" ");
                }
                bw.write("\n");
                bw.flush();
            }
            
         System.out.println("temporary "+Saxparser.index+" File write over");
            bw.close();
            fw.close();
        }catch (IOException e) {
            e.printStackTrace();
        } 

    }
}