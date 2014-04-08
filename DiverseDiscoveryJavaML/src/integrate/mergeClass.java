package integrate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Pair;

public class mergeClass {
	// 等价类合并:下标是等价类序号
	ArrayList<HashMap<String, Pair>> mergedClass = new ArrayList<HashMap<String, Pair>>();
	// 等价类的微博号：下标是等价类序号
	ArrayList<ArrayList<Integer>> classet = new ArrayList<ArrayList<Integer>>();	
	
	public void merge(ArrayList<Pair> al, int sumofclass) {
		for(int i=0; i<sumofclass; i++) {
			classet.add(new ArrayList<Integer>());
		}
		// 等价类的微博号
		for(int i=0; i<al.size(); i++) {
			if(al.get(i).classnum >= 100)
				System.out.println(i);
			classet.get(al.get(i).classnum).add(i);
		}
		
		for(int i=0; i<sumofclass; i++) {
			// 向第一条微博合并
			HashMap<String, Pair> hm = new HashMap<String, Pair>();
			//将第i类的第一条微博作为初始
			hm.putAll(al.get(classet.get(i).get(0)).hm);
			for(int j=1; j<classet.get(i).size(); j++) {
				hm = mergeHM(hm, al.get(classet.get(i).get(j)).hm);
			}
			mergedClass.add(hm);
			}
	}
	
	
	public HashMap<String, Pair> mergeHM(HashMap<String, Pair> hm1, HashMap<String, Pair> hm2) {
		HashMap<String, Pair> hm = new HashMap<String, Pair>();
		hm.putAll(hm1);
		
		Set set = hm2.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext()) {
			Map.Entry o = (Map.Entry)it.next();
			if(!hm1.containsKey((String)o.getKey())) {
				hm.put((String)o.getKey(), 
						new Pair(
								((Pair)o.getValue()).tf/2, 
								((Pair)o.getValue()).idf));
			}
			else
				hm.put((String)o.getKey(), 
						new Pair(
								(((Pair)o.getValue()).tf+hm1.get((String)o.getKey()).tf)/2, 
								((Pair)o.getValue()).idf));
		}
		return hm;
	}

	public ArrayList<HashMap<String, Pair>> getMergedClass() {
		return mergedClass;
	}
	
	public ArrayList<ArrayList<Integer>> getClassNum() {
		return classet;
	}
}