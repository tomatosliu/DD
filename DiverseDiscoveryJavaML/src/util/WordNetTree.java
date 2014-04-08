package util;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.*;
public class WordNetTree {
	private WordNetNode[] nodes;	//存放WordNet树中所有的节点
    private HashMap<String, ArrayList<Integer>> HM_Word_Index; //key:词 value:对应的nodes下标，用;隔开
    private double maxSimilarity = Math.log(38);
    public WordNetTree(String filepath)
    {
    	try
    	{
    		ArrayList<String> wordsList = new ArrayList<String>();
            ArrayList<Integer> wordsDepth = new ArrayList<Integer>();
            
            //读入文件，先把所有的词和深度分别放入wordsList和wordsDepth
    		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(filepath),"utf-8"));
    		String s = null; 
    		String []t;
    		
    		wordsList.add("all");
    		wordsDepth.add(1);
    		
    		reader.read();
    		
    		while((s = reader.readLine()) != null)
    		{
    			t = s.split(">");
    			s = t[0];
    			
                int depth = 0;
                while (s.charAt(depth) == '.')
                {
                    depth++;
                }

                wordsList.add(s);

                wordsDepth.add(depth + 2);
                
    		}
    		reader.close();
    		
    		int numOfnodes = wordsList.size();
    		nodes = new WordNetNode[numOfnodes];
    		HM_Word_Index = new HashMap<String,ArrayList<Integer>>(numOfnodes * 2);
    		
    		
    		for (int i = 0; i < numOfnodes; i++)
            {
                nodes[i] = new WordNetNode();
                nodes[i].depth = wordsDepth.get(i);
                String[] terms;
                
             
                if (i == 0)
                {
                    terms = wordsList.get(i).split(";");
                }
                else
                {
                    terms = wordsList.get(i).substring(nodes[i].depth - 2).split(";");
                }
                for (int j = 0; j < terms.length; j++)
                {
                    terms[j] = terms[j].trim();
                    
                    
                    if (terms[j].length() == 0)
                        continue;

                    nodes[i].words.add(terms[j]);

                    if (!HM_Word_Index.containsKey(terms[j]))
                    {
                    	ArrayList<Integer> tempArray = new ArrayList<Integer>();
                    	tempArray.add(i);
                    	HM_Word_Index.put(terms[j], tempArray);
                    }
                    else
                    {
                    	ArrayList<Integer> tempArray = HM_Word_Index.get(terms[j]);
                    	tempArray.add(i);
                        HM_Word_Index.put(terms[j], tempArray);
                    }
                }
            }
            for (int i = 0; i < numOfnodes; i++)   //遍历所有结点
            {
                //添加子节点
                for (int j = i + 1; j < numOfnodes; j++)
                {
                    if (wordsDepth.get(j) == wordsDepth.get(i) + 1)
                    {
                        nodes[i].sons.add(j);
                        nodes[j].father = i;
                    }
                    if (wordsDepth.get(j) <= wordsDepth.get(i))
                    {
                        break;
                    }
                }
            }
    	}
    	catch(Exception e)
    	{
    		System.out.println(e.getMessage());
    	}
    }
    public ArrayList<String> getSimWords(String word) {
        ArrayList<Integer> index =  HM_Word_Index.get(word);
        ArrayList<String> arrStr = new ArrayList<String>();
        //查看所有包含word的结点，返回一个word的相似词集合
        if(index != null) {//有的字可能没有
            for(int i=0; i<index.size(); i++) {//遍历每一个结点
                int idx = index.get(i);
                ArrayList<String> simWords = nodes[idx].words;
                if(simWords != null) {//防止出现空的情况
                    for(int j=0; j<simWords.size(); j++) {//遍历每一个结点中的每一个词
                        String str = simWords.get(j);
                        if(!arrStr.contains(str)) {//过滤word本身
                            //System.out.println(str);
                            arrStr.add(str);
                        }
                    }
                }
            }
        }
        else
            return null;
        return arrStr;
    }
}
