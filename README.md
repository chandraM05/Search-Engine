____________________________________________________________________________________________________________________________________________
M Chandravati
Mtech CSE-20162046
____________________________________________________________________________________________________________________________________________
===============================================================
	           Indexing mechanism for search egine 
===============================================================

-> The code basically implement INDEX CREATION MODULE for seach engine

-> It reads the content of XML File and SAX Parser passes it

-> Character by character it scans the text and extracts words for indexing

-> sorted posting list on term frequency

-> Amended every word with D.F in indexing 

===============================================================
	           Files LIST and their Functionality
===============================================================

(i)  Search_engine.java : Contains main function takes Xml "wiki dump" as comandline input and calls Saxparser for parsing the corpus . 

(ii) Saxparser.java Contains parsing handling mechanism .

(iii) Parser.java : Remove scrap separates fields and send words for parsing into map

(iv) extract_info.java :Helps in parsing the content removing stop words stemming it using Stemmer.java and inserts into treemap .

(v) Stemmer.java stemm the word to its root form and return it to the calling function 

(vi) inverted_index.java writes o/p to the given comand line input file if file not exist it will create new file 

vii) kway_marge.java : This merges the complete index files created by writing the file

viii) merge_idtitle.java : This is main class to extract title corrsponding to the document id and save to file

ix) Post_scan.java : To handle kwaymerge,query processing operations etc .

x)Query_processor.java : used for proceesing user quries to give the  desired document 


================================================================
	            Implementation characteristics
================================================================


-> Used tree Hash map for SortedMap order of the keys will be sorted. so sorted according to word 0-9,A-Z

-> Not storing zeros in index file. helps in reducing 

-> K-way merging to implement scalability 

-> Stop words are removed to make index efficent except from title in the view for seacrch of titles like "to be not to be" type query 

-> Tfidf is used to get the results .

->Query time: <=0,2~0.7 msec per query Query can be single word (Sachin) or multi-word(Sachin Tendulkar India) or fielded query(t:sachin b:india), 
where t=title,b=body,c=category,e:external links,i:info-box

==============================================================
	            Output Format
==============================================================



#Index file

-> word-<df>?doc_id:<title count>,<category count>,<infobox count>,<body count>,<external link count>;....;

eg:-Sachin-6256:b5,,,1,; it represnt Sachin is in document 6256 5times time in body .

#Query processor

a) enter your query:<enter user query>

b) Time taken in Result Generation: ...milisec

# Assumption

	i) xml-wiki dump #present in parent directory of code directory

	ii) output folders should be present in parent directory