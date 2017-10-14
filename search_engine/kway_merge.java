package search_engine;

/*
 * @author chandra
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;
import static search_engine.QueryProcessing.valueComparator;

public class kway_merge {
    
    private BufferedReader[] readers ;
    private Boolean[] isEmpty ;

    class Comp implements Comparator<key_posting> 
    { 
        @Override
        public int compare(key_posting x, key_posting y) 
        { 
            return x.key.compareTo(y.key);
        } 
    }
    
//    public String sortbyfreq(String postings)
//    {
//        String res = "";
//        int idxOfNextWord = 0,i=0;
//        ArrayList<String> post = new ArrayList();
//
//        for (; i < postings.length(); i++) {
//            if(postings.charAt(i)==';') 
//            {
//                post.add(postings.substring(idxOfNextWord, i));
//                idxOfNextWord = i+1;
//            }            
//        }
//        
//        post.add(postings.substring(idxOfNextWord));
//        TreeMap<Integer,String> temp = new TreeMap<Integer,String>();
//        System.out.println(post.toString());
////        for(String ii : post)
//        for(int k=0;k<post.size();k++)
//        {
//            String ii=post.get(k);
//            int j = ii.indexOf("F");
//            if(j==-1)
//                continue;
//            String term=ii.substring(0,j);
//            String imp=ii.substring(j+1);
//            int freq;
//            int l = imp.indexOf("I");
//            if(l!=-1)
//            {
//                freq = Integer.parseInt(imp.substring(0,l));
//                term+="I"+imp.substring(l+1);
//            }
//            else
//            {
//                System.out.println(imp);
//                freq = Integer.parseInt(imp);
//            }
//            temp.put(freq,term);
//        }
//        System.out.println(temp.size());
//        Boolean flag=true;
//        for(Map.Entry<Integer,String> e : temp.entrySet())
//        {
//            if(flag)
//            {
//                res+=e.getValue();
//                flag=false;
//            }
//            else
//                res+=";"+e.getValue();
//        }
//            
//        temp.clear();
//        return res;
//        
//    }
    
    public String sortbyfreq(String postings)
    {
        String res = "";
        String[] posting = postings.split(";");
        System.out.println(postings);
        TreeMap<Integer,String> temp = new TreeMap<Integer,String>();
        for(String i : posting)
        {
//            System.out.println(i);
            String[]  p = i.split("F");
            if(p.length<2)
                continue;
                
            int freq;
            if(p[1].indexOf("I")!=-1)
            {
                freq = Integer.parseInt(p[1].split("I")[0]);
            }
            else
            {
                System.out.println(p[0]+" "+p[1]);
                freq = Integer.parseInt(p[1]);
            }
            temp.put(freq,p[0]);
        }
        Boolean flag=true;
        for(Map.Entry<Integer,String> e : temp.entrySet())
        {
            if(flag)
            {
                res+=e.getValue();
                flag=false;
            }
            else
                res+=";"+e.getValue();
        }
            
        temp.clear();
        return res;
    }
    
//    public String sortbyfreq(String postings) {
//        String res = "";
//        int idxOfNextWord = 0, i = 0;
//        ArrayList<String> post = new ArrayList();
//
//        for (; i < postings.length(); i++) {
//            if (postings.charAt(i) == ';') {
//                post.add(postings.substring(idxOfNextWord, i));
//                idxOfNextWord = i + 1;
//            }
//        }
//
//        post.add(postings.substring(idxOfNextWord));
//        HashMap< String, long> temp = new HashMap<String, long>();
////        System.out.println("in="+post.toString());
////        for(String ii : post)
//        for (int k = 0; k < post.size(); k++) {
//            String ii = post.get(k);
//            int j = ii.indexOf("F");
//            if (j == -1) {
//                continue;
//            }
//            String term = ii.substring(0, j);
//            String imp = ii.substring(j + 1);
//            long freq;
//            if (imp.contains("I")) {
////                System.out.println(imp+" "+imp.split("I")[0]);
//                freq = long.parse(imp.split("I")[0]);
//                term += "I" + imp.split("I")[1];
//            } else {
////                System.out.println(imp);
//                freq = Integer.parseInt(imp);
//            }
//            temp.put(term, freq);
//        }
////        System.out.println(temp.size());
//        Set<Entry<String, long>> entries = temp.entrySet();
//        List<Entry<String, long>> listOfEntries = new ArrayList<Entry<String, long>>(entries);
//        // sorting HashMap by values using comparator
//        Collections.sort(listOfEntries, valueComparator);
//
//        Boolean flag = true;
//        for (Map.Entry<String, Integer> e : temp.entrySet()) {
//            if (flag) {
//                res += e.getKey();
//                flag = false;
//            } else {
//                res += ";" + e.getKey();
//            }
//        }
//
//        temp.clear();
////        System.out.println("out="+res);
//        return res;
//
//    }
    
    
    public void merge() {
    try
    {
        int no_temp_indexes=Saxparser.index,isnull=0;
        no_temp_indexes=2;
        System.out.println("Merging :"+no_temp_indexes+" Files");
        
        readers = new BufferedReader[no_temp_indexes+1];
        isEmpty = new Boolean[no_temp_indexes+1];
        Comparator<key_posting> compares = new Comp();

        int no_entries=0,ter_entry=0,pri_entries=0;
        

	for(int i=0;i<=no_temp_indexes;i++)
	{
            readers[i] = new BufferedReader(new InputStreamReader(new FileInputStream("/media/chandra/New Volume/ire/trash/temp"+i+".txt")));
            isEmpty[i] = false;
	}
	
        System.out.println("it bufferread complete:");
	File primary_index = new File("/media/chandra/New Volume/ire/Index/Primary_Index.txt");
	File secondry_index = new File("/media/chandra/New Volume/ire/Index/Second_Index.txt");
        File tertiary_index = new File("/media/chandra/New Volume/ire/Index/Tertiary_index.txt");

        
	if (primary_index.exists()==false) 
            primary_index.createNewFile();
	
	if (secondry_index.exists()==false) 
            secondry_index.createNewFile();
        
        if (tertiary_index.exists()==false) 
            tertiary_index.createNewFile();
//         System.out.println("it create file complete:");
	FileWriter fw_pri = new FileWriter("/media/chandra/New Volume/ire/Index/Primary_Index.txt");
	FileWriter fw_sec = new FileWriter("/media/chandra/New Volume/ire/Index/Second_Index.txt");
        FileWriter fw_ter = new FileWriter("/media/chandra/New Volume/ire/Index/Tertiary_index.txt");
	BufferedWriter bw_pri = new BufferedWriter(fw_pri);
	BufferedWriter bw_sec = new BufferedWriter(fw_sec);
        BufferedWriter bw_ter = new BufferedWriter(fw_ter);
        
//	System.out.println("Starting Merge");
	

        String line="";
        long sec_offset=0,ter_offset=0;
        
        PriorityQueue<key_posting> pq_entries =  new PriorityQueue<key_posting>(no_temp_indexes+1,compares); 
			
	for(int j=0;j<=no_temp_indexes;++j)
	{
		if(!isEmpty[j])
		{
                    
			line = readers[j].readLine();
			if(line!=null)
			{
				key_posting t = new key_posting();
				String[] k_p = line.split(":");
				t.posting=k_p[1];
				t.temp_doc_no=j;
                                t.key=k_p[0];
				pq_entries.add(t);
				++no_entries;
			}
			else
			{
				++isnull;
				isEmpty[j]=true;
//				System.out.print("Completed File"+j);
//				System.out.print("at: "+no_entries+"\n");
				readers[j].close();
			}
					 
		}		 
				 
        }
        
        key_posting k_p1 = pq_entries.poll();
        int out_last = k_p1.temp_doc_no;
        String out_posting = k_p1.posting,out_key = k_p1.key;
        String lout_posting=null,lout_key=null;
        
        while(isnull<=no_temp_indexes)
        {
            if(isEmpty[out_last])
            {
                key_posting ltop = pq_entries.poll();
                out_last=ltop.temp_doc_no;
                lout_posting=ltop.posting;
                lout_key=ltop.key;
               
            }
            else
            {
                line = readers[out_last].readLine();
                if(line==null)
                {
                    if(!isEmpty[out_last])
                        isnull++;
//                    System.out.println("Completed File"+out_last+"at "+no_entries);
                    isEmpty[out_last]=true;    
                    readers[out_last].close();
                }
                else
                {
                    key_posting t = new key_posting();
                    String[] k_p = line.split(":");
                    t.posting=k_p[1];
                    t.temp_doc_no=out_last;
                    t.key=k_p[0];
                    pq_entries.add(t);
                    ++no_entries;
                    
                   //System.out.println("Finised entering");
                    key_posting ltop1 = pq_entries.poll();
                    
                    
                    lout_posting=ltop1.posting;
                    lout_key=ltop1.key;
                    out_last=ltop1.temp_doc_no;
                }
            }
            if(lout_key.equals(out_key)&&out_key!=null)
             {
                // System.out.print("\tEqual case");
                 out_posting=out_posting+lout_posting;
             }
             else
             {
                if(pri_entries%50==0)
                {
                    bw_ter.write(out_key+":"+ter_offset+"\n");
                    bw_ter.flush();
                }
                 
                //Write into secondary index
                
                bw_sec.write(out_key+":"+sec_offset+"\n");
                bw_sec.flush();
                ter_offset+=out_key.length()+String.valueOf(sec_offset).length()+2;
                
                
//                String sb=sortbyfreq(out_posting);
                String sb=out_posting;
                bw_pri.write(out_key+">"+sb+"\n");
                bw_pri.flush();
                sec_offset+=out_key.length()+sb.length()+2;
                pri_entries++;
                //System.out.println("next is: "+lout_word+"last was"+out_word);
                  

                out_key=lout_key;
                out_posting=lout_posting;
                lout_key="";
                lout_posting="";
                
            }
        }
                bw_pri.close();
                bw_sec.close();
                bw_ter.close();
                fw_pri.close();
                fw_sec.close();
                fw_ter.close();
          System.out.print(" Records Written into merged file: "+no_entries+"\n");	 	
		        
        }catch(IOException e){
            System.out.print(" Error in merging the files ");	 	
	}
    }

}