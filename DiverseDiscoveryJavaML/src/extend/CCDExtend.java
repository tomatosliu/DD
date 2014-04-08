package extend;

import java.util.*;
import java.io.*;
import java.lang.*;

/* 
	基于CCD的微博词扩展
*/

import util.WordNetTree;
import util.StopWord;
public class CCDExtend {
	private WordNetTree wordNetTree;
	private ArrayList<HashMap<String, Integer>> HM_Seg;//每条微博对应一个HM_Seg的元素
	private ArrayList<HashMap<String, Integer>> Not_ExtendHM;//不进行扩展
	public CCDExtend(String filepath) {
		wordNetTree = new WordNetTree(filepath);
		HM_Seg = new ArrayList<HashMap<String, Integer>>();
		Not_ExtendHM = new ArrayList<HashMap<String, Integer>>();
	}
	//expand the Seg
	public void expandSeg(String filepath) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath),"utf-8"));
			String tmp = null;
			String []t;
			
			//按每条微博为单位，进行填入HM_Seg
			HashMap<String, Integer> hm = new HashMap<String, Integer>();
			while((tmp = br.readLine()) != null) {
				if(tmp.equals("#")) {//如果是‘#’说明是一条微博的末尾，结束并开始一条新的微博
					HM_Seg.add(hm);

					hm = new HashMap<String, Integer>();
					continue;
				}
				//转换每行的两个元素，插入hashmap中
				t = tmp.split(" ");
				try {
					if(!(new StopWord()).contains(t[0]))
						hm.put(t[0], Integer.parseInt(t[1]));
				} catch(NumberFormatException e) {
					System.out.println(e.getMessage());
				}
			}
			Not_ExtendHM.addAll(HM_Seg);
			
			
			//对每个句子进行expansion
			for(int i=0; i<HM_Seg.size(); i++) {
				HashMap<String, Integer> hmExpansion = HM_Seg.get(i);
				HashMap<String, Integer> hmtmp = new HashMap<String, Integer>();
				hmtmp.putAll(hmExpansion);

				Set hmset = hmExpansion.entrySet();
				Iterator it = hmset.iterator();
				while(it.hasNext()) {//每个词都进行扩展
					Map.Entry o = (Map.Entry)it.next();   
                    ArrayList<String> simArrStr = wordNetTree.getSimWords((String)o.getKey()); 
                    if(simArrStr != null) {
                    	int b = simArrStr.size();
	                    for(int j=0; j<b; j++) {//检查并插入扩展词
	                    	//记得更新HM_Seg	  
	                    	String str = simArrStr.get(j);
	                    	if((new StopWord()).contains(str))
	                    		continue;
	                    	if(hmExpansion.containsKey(str)) {
	                    		int tmpval = hmExpansion.get(str);
	                    		tmpval = tmpval + (Integer)o.getValue();
	                    		hmtmp.put(str, tmpval);//put直接覆盖
	                    	}
	                    	else {
	                    		try {
	                    			hmtmp.put(str, (Integer)o.getValue());//破坏了interator
	                    		} catch(NumberFormatException e) {
	                    			System.out.println(e.getMessage());
	                    		}
	                    	}
	                    }  
	                } 
	                else {
	                	hmtmp.remove(o.getKey());
	                }                          
				}
				HM_Seg.set(i, hmtmp);//set覆盖ArrayList
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}

	}
	public ArrayList<HashMap<String, Integer>> retExpansion() {
		return HM_Seg;
	}
	public ArrayList<HashMap<String, Integer>> retNotExpansion() {
		return Not_ExtendHM;
	}
}