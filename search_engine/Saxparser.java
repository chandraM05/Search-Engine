
package search_engine;

import java.io.IOException;
import java.util.TreeMap;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/*
 * @author chandra
 */
//saxparser separates tags . store the required data in a string builder and ignore the rest
public class Saxparser {
    static int no_docs=0,sec_no_docs=0;
    public static int index=0;
    public static TreeMap<String,String> id_tit = new TreeMap<String,String>();
    public String sec_tit_index="",ter_tit_index="";
    long sec_off=0,ter_off=0;

    
    public void SAXparser()
    {
    try 
    {
    		
	SAXParserFactory factory = SAXParserFactory.newInstance();
	SAXParser saxParser = factory.newSAXParser();
        
        inverted_index ii = new inverted_index();
        
//        Saxparser it= new Saxparser();
//        it.Initialize_it();
        
	DefaultHandler handler = new DefaultHandler() {

	boolean page = false;
	boolean title = false;
	boolean id = false;
	boolean text = false;
	boolean mediawiki = false;
	boolean id_flag = false ;

	StringBuilder currentText = new StringBuilder();

        String title_page;
        String id_page ;
        
        
	public void startElement(String uri, String localName,String qName,Attributes attributes) throws SAXException 
	{
            if (qName.equalsIgnoreCase("mediawiki")) 
            {
		mediawiki = true;
            }
            if (qName.equalsIgnoreCase("page")) 
            {
		page = true;
		
                if(no_docs%5000==0)
                {
                     ii.write_toFile();
                     Search_engine.database.clear();
                     ii.doc_tit_write();
                     id_tit.clear();
                     ++index;
                
                     ter_tit_index="";
                }
            }
            else if (qName.equalsIgnoreCase("title")) 
            {
                title = true;			
            }
            else if (qName.equalsIgnoreCase("id")&&id_flag==false) 
            {
		id = true;
		id_flag = true ;
            }
            else if (qName.equalsIgnoreCase("text")) 
            {
		text = true;
            }

	}

	@Override
	public void characters(char[] ch, int start, int length)
	{
            if (text || id || title) 
	   	currentText.append(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName) throws SAXException 
	{
            try
            {
		String str=currentText.toString();
                
		if(qName.equalsIgnoreCase("mediawiki")) 
		{
                    ii.write_toFile();
                    Search_engine.database.clear();
                    ii.doc_tit_write();
                    id_tit.clear();
                    mediawiki=false;
		}
		if(qName.equalsIgnoreCase("page")) 
		{
                    ++no_docs;
                    page=false;
                    id_flag=false;
		}
		if(qName.equalsIgnoreCase("title")) 
                {
                    title_page = str;
                    title=false;
		}
                else if(qName.equalsIgnoreCase("id"))
		{
                     str=str.trim();
                     if(str.length()!=0)
                        id_page = new String(str);
                        
		    id=false;
                    
		}
		else if(qName.equalsIgnoreCase("text")) 
		{
                    parser pr=new parser(str,title_page,id_page);
                    id_tit.put(id_page,title_page);
		    text=false;
                    pr.parse_body();
		}
		
		currentText.setLength(0);
        }catch (Exception e){}
		
      }

     };

       saxParser.parse(Search_engine.input_file, handler);
       
       System.out.println("no of docs :"+no_docs);
        
	}catch (IOException | ParserConfigurationException | SAXException e) {}
    }
}