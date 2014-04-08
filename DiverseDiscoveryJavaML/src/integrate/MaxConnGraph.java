package integrate;

import java.util.*;
import java.io.*;
import java.lang.*;

import util.Pair;
import util.Tfidf;
import extend.CCDExtend;

public class MaxConnGraph {
	public ArrayList<HashMap<String, Integer>> AL_Expansion;//扩展以后的微博词
	public HashMap<HashMap<String, Integer>, Integer> eqlClass = new HashMap<HashMap<String, Integer>, Integer>();
	public int sumofclass;
	public MaxConnGraph(ArrayList<HashMap<String, Integer>> al) {
		AL_Expansion = new ArrayList<HashMap<String, Integer>>();
		AL_Expansion.addAll(al);//复制内容，不指向同一个collection
	}
	public double calOverlap(HashMap<String, Integer> hm1, HashMap<String, Integer> hm2) {
		Set hmset = hm1.entrySet();
		Iterator it = hmset.iterator();

		int sm = 0;//记录Common words的数目
		while(it.hasNext()) {
			Map.Entry o = (Map.Entry)it.next();   
            //System.out.println(o.getKey()+"--"+o.getValue()); 
            if(hm2.containsKey((String)o.getKey())) {
            	sm = sm+1;
            }                                
		}
		return ((double)sm)/((hm1.size()+hm2.size()-sm));
		//return sm;
	}

	//#test
	public void test() {
		for(int i=0; i<AL_Expansion.size(); i++) {
			for(int j=i+1; j<AL_Expansion.size(); j++) {
				double d = calOverlap(AL_Expansion.get(i), AL_Expansion.get(j));
				if(d > 0.2)
					System.out.println(d);
			}
		}
	}
	
	public boolean overLapTheClasses(HashMap<HashMap<String, Integer>, Integer> hm, 
		HashMap<String, Integer> hm1, HashMap<String, Integer> hm2) {
		try {
			int idx1 = hm.get(hm1);
			int idx2 = hm.get(hm2);
			//如果本身就是一个类的，那么直接返回
			if(idx1 == idx2)
				return false;
			//System.out.println("combine!");
			//修改所有类idx2
			Set s = hm.entrySet();//需要修改hm中的
			Iterator it = s.iterator();
			while(it.hasNext()) {
				Map.Entry o = (Map.Entry)it.next();
				if((Integer)o.getValue() == idx2)
					hm.put((HashMap<String, Integer>)o.getKey(), idx1);//覆盖
			}
			return true;
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return true;
	}

	public int combine1(double threshold) {//不断合并
		
		for(int i=0; i<AL_Expansion.size(); i++) {//先为每一个微博设立单独的等价类
			eqlClass.put(AL_Expansion.get(i), i);
		}
		while(true) {
			int cbsum = 0;
			for(int i=0; i<AL_Expansion.size(); i++) {
				for(int j=i+1; j<AL_Expansion.size(); j++) {
					HashMap<String, Integer> hm1 = AL_Expansion.get(i);
					HashMap<String, Integer> hm2 = AL_Expansion.get(j);
					double cur = calOverlap(hm1, hm2);
					if(cur>threshold) {	
						//System.out.println(eqlClass.get(hm2));
						if(overLapTheClasses(eqlClass, hm1, hm2))
							cbsum ++;
						//System.out.println(eqlClass.get(hm2));
						//System.out.println("#");
					}
				}
			}
			//System.out.println(cbsum);
			if(cbsum == 0)
				break;
			Collection<Integer> al = eqlClass.values();
			Iterator it1 = al.iterator();
			while(it1.hasNext()) {
				int i = (Integer)it1.next();
				//System.out.println(i);
			}
			/*合并应该一层层来进行*/
		}
		//#Test
		int []be = new int[1000];
		for(int i=0; i<1000; i++)
			be[i] = 0;
		for(int i=0; i<AL_Expansion.size(); i++) {
			be[eqlClass.get(AL_Expansion.get(i))] = 1;
		}
		int sum = AL_Expansion.size();
		int idx = 1;
		
		String filepath = "./MH370.txt";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath),"utf-8"));
			ArrayList<String> al_sente = new ArrayList<String>();
			String str = null;
			while((str = br.readLine()) != null) {
				al_sente.add(str);
			}
			int idxinner;
			
			HashMap<HashMap<String, Integer>, Integer> _eqlClass = new HashMap<HashMap<String, Integer>, Integer>();
			for(int i=0; i<sum; i++) {
				if(be[i] != 0) {
					System.out.println("Class " + String.valueOf(idx));
					idxinner = 1;
					for(int j=0; j<AL_Expansion.size(); j++) {
						if(eqlClass.get(AL_Expansion.get(j)) == i) {//某个句子类号为idx *******
							System.out.println("("+String.valueOf(idxinner)+")"+al_sente.get(j));
							_eqlClass.put(AL_Expansion.get(j), idx-1);// 放这里有问题，这里修改了，之后查看的时候又看到了
							idxinner++;
						}
						
					}
					idx ++;
					
				}
			}
			eqlClass = _eqlClass;
			System.out.println();
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
		//System.out.println(sum);
		return idx-1;
	}
	
	public ArrayList<Pair> run() {
		/*
		 * 数组的每个元素代表合并后按顺序的一条微博
		 * 	每条微博由一个HashMap组成，一个词对应一个Pair<tf, idf>
		 */
		ArrayList<Pair> al = new ArrayList<Pair>();
		
		int idx = this.combine1(0.50);
		al = (new Tfidf()).tfidf(AL_Expansion, eqlClass, idx);
		
		this.sumofclass = idx;
		return al;
	}
	
	public int getSum() {
		return sumofclass;
	}
	
	
	public void combine2(double threshold) {//不断合并
		HashMap<HashMap<String, Integer>, Integer> eqlClass = new HashMap<HashMap<String, Integer>, Integer>();
		for(int i=0; i<AL_Expansion.size(); i++) {//先为每一个微博设立单独的等价类
			eqlClass.put(AL_Expansion.get(i), i);
		}
		while(true) {
			double dmax = -1;
			int idx1=0, idx2=0;
			for(int i=0; i<AL_Expansion.size(); i++) {//找到相似度最大的一个
				for(int j=i+1; j<AL_Expansion.size(); j++) {
					double cur = calOverlap(AL_Expansion.get(i), AL_Expansion.get(j));
					if(cur >= dmax) {
						dmax = cur;
						idx1 = i;
						idx2 = j;
					}
				} 
			}
			//进行合并
			if(dmax > threshold) {//即将要合并的两个集合，重叠度也要大于一个阈值
				//System.out.println("Combine!");
				Set s = AL_Expansion.get(idx2).entrySet();
				HashMap<String, Integer> hm = AL_Expansion.get(idx1);
				Iterator it = s.iterator();
				while(it.hasNext()) {
					Map.Entry o = (Map.Entry)it.next();
					if( hm.containsKey(o.getKey()) ) {//找到Common Words，合并
						int val = hm.get(o.getKey());
						hm.put((String)o.getKey(), val+(Integer)o.getValue());
					}
				}
				//修改中的Array AL_Expansion
				//System.out.println(dmax);
				
				AL_Expansion.set(idx1, hm);
				AL_Expansion.remove(idx2);
			}
			else
				break;
			
		}
		System.out.println(AL_Expansion.size());
	}

	
}