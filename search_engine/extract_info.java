
package search_engine;

import java.util.TreeMap;
import java.util.HashMap;
/*
 * @author chandra
 */

//for stopwords removal ,stemming and storing into hash database
public class extract_info {
    //list of stopwords
    public static String stop_words[]= {"abbr","reflist","redirect","jsp","web","png","coord","gr","com","tr","td","nbsp","http","https","www","a","about","above","across","after","again","against","all","almost","alone","along","already","also","although","always","among","an","and","another","any","anybody","anyone","anything","anywhere","are","area","areas","around","as","ask","asked","asking","asks","at","away","b","back","backed","backing","backs","be","became","because","become","becomes","been","before","began","behind","being","beings","best","better","between","big","both","but","by","c","came","can","cannot","case","cases","certain","certainly","clear","clearly","come","could","d","did","differ","different","differently","do","does","done","down","down","downed","downing","downs","during","e","each","early","either","end","ended","ending","ends","enough","even","evenly","ever","every","everybody","everyone","everything","everywhere","f","face","faces","fact","facts","far","felt","few","find","finds","first","for","four","from","full","fully","further","furthered","furthering","furthers","g","gave","general","generally","get","gets","give","given","gives","go","going","good","goods","got","great","greater","greatest","group","grouped","grouping","groups","h","had","has","have","having","he","her","here","herself","high","high","high","higher","highest","him","himself","his","how","however","i","if","important","in","interest","interested","interesting","interests","into","is","it","its","itself","j","just","k","keep","keeps","kind","knew","know","known","knows","l","large","largely","last","later","latest","least","less","let","lets","like","likely","long","longer","longest","m","made","make","making","man","many","may","me","member","members","men","might","more","most","mostly","mr","mrs","much","must","my","myself","n","necessary","need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody","non","noone","not","nothing","now","nowhere","number","numbers","o","of","off","often","old","older","oldest","on","once","one","only","open","opened","opening","opens","or","order","ordered","ordering","orders","other","others","our","out","over","p","part","parted","parting","parts","per","perhaps","place","places","point","pointed","pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","q","quite","r","rather","really","right","right","room","rooms","s","said","same","saw","say","says","second","seconds","see","seem","seemed","seeming","seems","sees","several","shall","she","should","show","showed","showing","shows","side","sides","since","small","smaller","smallest","so","some","somebody","someone","something","somewhere","state","states","still","still","such","sure","t","take","taken","than","that","the","their","them","then","there","therefore","these","they","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to","today","together","too","took","toward","turn","turned","turning","turns","two","u","under","until","up","upon","us","use","used","uses","v","very","w","want","wanted","wanting","wants","was","way","ways","we","well","wells","went","were","what","when","where","whether","which","while","who","whole","whose","why","will","with","within","without","work","worked","working","works","would","x","y","year","years","yet","you","young","younger","youngest","your","yours","z"};
    
    public void field_title(String data)
    {
        String str=data.trim();
        if(str.length()<1)
            return ;
        str=data.toLowerCase();
        Stemmer stem_title = new Stemmer();
	stem_title.add(str.toCharArray(),str.length());
	str=stem_title.stem();
        int len=str.length();
        if(len<1||len>12)
            return ;
        if(Search_engine.database.get(str)==null)
        {
            TreeMap<String, HashMap<String,Integer>> innermap = new TreeMap<String, HashMap<String,Integer>>();
            Search_engine.database.put(str,innermap);
//            Search_engine.sorted_words.add(str);
        }
        if(!Search_engine.database.get(str).containsKey(parser.id))
        {
            HashMap<String,Integer> freqmap = new HashMap<String,Integer>();
            freqmap.put("T",0);
            freqmap.put("B",0);
            freqmap.put("I",0);
            freqmap.put("R",0);
            freqmap.put("E",0);
            freqmap.put("C",0);
            freqmap.put("F",0);
            Search_engine.database.get(str).put(parser.id,freqmap);
        }
        HashMap<String,Integer> temp = new HashMap<String,Integer>();
        temp=Search_engine.database.get(str).get(parser.id);
        Search_engine.database.get(str).get(parser.id).put("T",temp.get("T")+1);
        Search_engine.database.get(str).get(parser.id).put("F",temp.get("F")+1);
    }
        
    public void extract(String data,String op)
    {
        String str=data.trim();
        if(str.length()<3)          //ignore < 3 length strings
            return ;
        str=data.toLowerCase();
        if(Search_engine.stop_words.contains(str)) //stop words removal
            return;
        Stemmer stem_title = new Stemmer();         //stemming the words
	stem_title.add(str.toCharArray(),str.length());
	str=stem_title.stem();
        int len=str.length();
        if(len<3||len>12)           //only stemmed words between the range are considered
            return ;
        
        //Adding to the database
        if(Search_engine.database.get(str)==null)
        {
            TreeMap<String, HashMap<String,Integer>> innermap = new TreeMap<String, HashMap<String,Integer>>();
            Search_engine.database.put(str,innermap);
//            Search_engine.sorted_words.add(str);
        }
        if(!Search_engine.database.get(str).containsKey(parser.id))
        {
            HashMap<String,Integer> freqmap = new HashMap<String,Integer>();
            freqmap.put("T",0);
            freqmap.put("B",0);
            freqmap.put("I",0);
            freqmap.put("R",0);
            freqmap.put("E",0);
            freqmap.put("C",0);
            freqmap.put("F",0);
            Search_engine.database.get(str).put(parser.id,freqmap);
        }
        HashMap<String,Integer> temp = new HashMap<String,Integer>();
        temp=Search_engine.database.get(str).get(parser.id);
//        temp.put("title",temp.get("title"));
        Search_engine.database.get(str).get(parser.id).put(op,temp.get(op)+1);
        Search_engine.database.get(str).get(parser.id).put("F",temp.get("F")+1);
//        System.out.println(str);
    }
    
}