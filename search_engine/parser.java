
package search_engine;

/*
 * @author chandra
 */
public class parser {
    
    String title;
    static String id;
    String Rawdata;
    
    public parser(String message,String title_page,String id_page) 
    {
	super();
        this.id=id_page;
        this.title=title_page;
        this.Rawdata = message;
	
    }
    
    //to check whether the string is numeric
    public boolean isNumeric(String s) {
    return java.util.regex.Pattern.matches("\\d+", s);
    }
    
    public void parse_body()
    {
        extract_info ei=new extract_info();
        
        int length = title.length() , info_braces_count = 0;
        String tit="";
        for(int i=0;i<length;i++)
        {
            char ch=title.charAt(i);
            if((ch >='a'&&ch<='z')||(ch>='A'&&ch<='Z')|| (ch>=48&&ch<58))
                tit+=ch;
            else if((ch==39||ch==44||ch==48)&&tit.length()==1)
                {}
        }
        if(tit.length()>0)
        {
            ei.field_title(tit);
            tit="";
        }
        //title extraction complete
        
        Boolean ref_flag=false,cat_flag=false,ext_flag=false,info_flag=false;
        String Body="",infobox="",ref="",extlinks="",category="";
        
        length = Rawdata.length();

        
        //text processing
        for(int i=0;i<length;i++)
        {
            char ch = Rawdata.charAt(i);
            
            if(ch=='{')
            {
                int j=i;
                while(Rawdata.charAt(++j)==' ');
                if(Rawdata.charAt(j)=='{')
                {
                    while(Rawdata.charAt(++j)==' ');
                    if(j+4<length&&Rawdata.substring(j,j+4).equalsIgnoreCase("cite"))
                    {
                        //skipping the data within cite
                        i=j+4;
                        int bracescheck=2;
                        while(bracescheck>0)
                        {
                            ch=Rawdata.charAt(++i);
                            if(ch=='{')
                                bracescheck++;
                            else if(ch=='}')
                               bracescheck--;
                        }
                    }
                    else if(info_flag==false&&j+7<length&&Rawdata.substring(j,j+7).equalsIgnoreCase("infobox"))
                    {
                        info_flag=true;
                        i=j+7;
                        info_braces_count=2;
                    }
                }
            }
            else if(ch=='=')
            {
                int j=i;
                while(Rawdata.charAt(++j)==' ');
                if(Rawdata.charAt(j)=='=')
                {
                    while(Rawdata.charAt(++j)==' ');
                    if(ext_flag==false&&j+14<length&&Rawdata.substring(j,j+14).contains("External links"))
                    {
                        ref_flag=false;
                        ext_flag=true;
                        info_flag=false;
                        i=j+16;
                    }
                    else if(ref_flag==false&&j+10<length&&Rawdata.substring(j,j+10).equalsIgnoreCase("references"))
                    {
                        ref_flag=true;
                        info_flag=false;
                        i=j+9;
                    }
                }
            }
            else if(ch=='[')
            {
                int j=i;
                while(Rawdata.charAt(++j)==' ');
                if(Rawdata.charAt(j)=='[')
                {
                    while(Rawdata.charAt(++j)==' ');
                    if(j+8<length&&Rawdata.substring(j,j+8).equalsIgnoreCase("category"))
                    {
                        ref_flag=false;
                        ext_flag=false;
                        info_flag=false;
                        cat_flag=true;
                        i=j+9;
                    }
                }
            }
            ch=Rawdata.charAt(i);
            if(info_flag)
            {
                if(ch=='}')
                {
                        info_braces_count--;
                        if(info_braces_count==0)
                            info_flag=false;
                }
                    else if(ch=='{')
                        info_braces_count++;
                    else
                    {
                        if((ch >='a'&&ch<='z')||(ch>='A'&&ch<='Z')|| (ch>=48&&ch<58))
                                infobox+=ch;
                        else if((ch==39||ch==44)&&infobox.length()==1) //for strings like O'neil
                         {}
                        else
                        {
                            ei.extract(infobox,"I");
                            infobox="";
                        }
                    }
            }
                else if(ref_flag)
                {
                    if((ch >='a'&&ch<='z')||(ch>='A'&&ch<='Z')|| (ch>=48&&ch<58))
                            ref+=ch;
                    else if((ch==39||ch==44)&&ref.length()==1) //for strings like O'neil
                     {}
                    else
                    {
                        ei.extract(ref,"R");
                        ref="";
                    }
                }
                else if(ext_flag)
                {
                    if((ch >='a'&&ch<='z')||(ch>='A'&&ch<='Z')|| (ch>=48&&ch<58))
                            extlinks+=ch;
                    else if((ch==39||ch==44)&&extlinks.length()==1) //for strings like O'neil
                     {}
                    else
                    {
                        ei.extract(extlinks,"E");
                        extlinks="";
                    }
                }
                else if(cat_flag)
                {
                    if((ch >='a'&&ch<='z')||(ch>='A'&&ch<='Z')|| (ch>=48&&ch<58))
                            category+=ch;
                    else if((ch==39||ch==44)&&category.length()==1) //for strings like O'neil
                     {}
                    else
                    {
                        ei.extract(category,"C");
                        category="";
                    }
                }
                else
                {
                    if((ch >='a'&&ch<='z')||(ch>='A'&&ch<='Z')|| (ch>=48&&ch<58))
                            Body+=ch;
                    else if((ch==39||ch==44)&&Body.length()==1) //for strings like O'neil
                     {}
                    else
                    {
                        ei.extract(Body,"B");
                        Body="";
                    }
                }
            }
          //end of parsing  
        if(Body.length()>0)
        {
            ei.extract(Body,"B");
            Body="";
        }
        if(category.length()>0)
        {
            ei.extract(category,"C");
            category="";
        }
            
    }//enf of method parse_body
} //end of class