package util;

import util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Tfidf {
	public ArrayList<Pair> tfidf(
			ArrayList<HashMap<String, Integer>> AL_Expansion, 
			HashMap<HashMap<String, Integer>, Integer> eqlClass, 
			int idx) {
		
		ArrayList<Pair> al = new ArrayList<Pair>();
		
		/*
		 * 计算 tf idf
		 */
		//每组微博进行tf idx的计算
		for(int i=0; i<AL_Expansion.size(); i++) {
			HashMap<String, Pair> hmofresult = new HashMap<String, Pair>();
			int numofThisWeibo = eqlClass.get(AL_Expansion.get(i));
			//if(numofThisWeibo == 93)
			//	System.out.println(i);
			//System.out.println("***");
			/*
			 * 计算这一类中总次数(为tf做准备)
			 */
			Set wbsetSum = eqlClass.entrySet();
			Iterator wbitSum = wbsetSum.iterator();
			int sumofstr = 0;
			while(wbitSum.hasNext()) {
				Map.Entry o = (Map.Entry)wbitSum.next();
				// 将同类微博的所有词都加起来
				if((Integer)o.getValue() == numofThisWeibo){
					// 遍历一条微博所有词
					HashMap<String, Integer> hmclass = (HashMap<String, Integer>)o.getKey();
					Set settmp = eqlClass.entrySet();
					Iterator itmp = settmp.iterator();
					while(itmp.hasNext()) {
						Map.Entry otmp = (Map.Entry)itmp.next();
						sumofstr += (Integer)otmp.getValue();
					}
				}
			}
			/*
			 * 对每条微博求类内词频、词频总数
			 */
			HashMap<String, Integer> hm = AL_Expansion.get(i);
			Set wbset = hm.entrySet();
			Iterator wbit = wbset.iterator();

			while(wbit.hasNext()) {
				Map.Entry o = (Map.Entry)wbit.next();   
	            String str = (String)o.getKey();
	            
	            double tfnumCla = 0; // tf分子
	            double idfCla = 1; // idf分母
	            
	            //计算tf总词频
	            Set classset = eqlClass.entrySet();
				Iterator classit = classset.iterator();
				
				while(classit.hasNext()) {
					Map.Entry claso = (Map.Entry)classit.next();//weibo的迭代器 HashMap<String, Integer>, Integer
					
					Set strset = ((HashMap<String, Integer>)claso.getKey()).entrySet();
					Iterator strit = strset.iterator();
					while(strit.hasNext()) {
						Map.Entry stro = (Map.Entry)strit.next();//词汇的迭代器 String, Integer
						// 如果是要找的单词，那么开始叠加
						if(((String)stro.getKey()).equals(str)) { 
							if((Integer)claso.getValue() == numofThisWeibo) //同时这个String在当前等价类下，叠加到tf分子
								tfnumCla = tfnumCla + (Integer)stro.getValue();
							else //该String出现在别的微博类中，叠加到idf分母
								idfCla = idfCla + 1;
						}
					}

				}
				
				//得到tf,idf
				double tf = tfnumCla / sumofstr;
				double idf = Math.log(idx / idfCla);
				
				hmofresult.put(str, new Pair(tf, idf));
			}
			al.add(new Pair(hmofresult, numofThisWeibo));
		}
		return al;
	}
	
}