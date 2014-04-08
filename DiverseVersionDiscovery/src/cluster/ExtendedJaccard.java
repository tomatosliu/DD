package cluster;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Pair;

public class ExtendedJaccard {
	public double extendedJaccard(
			HashMap<String, Pair> hm1, 
			HashMap<String, Pair> hm2) {
		double sim = 0;
		double x2 = 0;
		double y2 = 0;
		double xy = 0;
		/*
		 * Calculate x^2
		 */
		Set set1 = hm1.entrySet();
		Iterator it1 = set1.iterator();
		while(it1.hasNext()) {
			Map.Entry o = (Map.Entry)it1.next();
			double idf = ((Pair)o.getValue()).idf;
			double tf = ((Pair)o.getValue()).tf;
			x2 += (tf * idf)*(tf * idf);
		}
		/*
		 * Calculate y^2
		 */
		Set set2 = hm2.entrySet();
		Iterator it2 = set2.iterator();
		while(it2.hasNext()) {
			Map.Entry o = (Map.Entry)it2.next();
			double idf = ((Pair)o.getValue()).idf;
			double tf = ((Pair)o.getValue()).tf;
			y2 += (tf * idf)*(tf * idf);
		}
		/*
		 * Calculate x*y
		 */
		Set set = hm1.entrySet();
		Iterator it = set.iterator();
		while(it.hasNext()) {
			Map.Entry o = (Map.Entry)it.next();
			//查看是否存在
			if(hm2.containsKey((String)o.getKey())) {
				double idf1 = hm2.get((String)o.getKey()).idf;
				double tf1 = hm2.get((String)o.getKey()).tf;
				double idf2 = ((Pair)o.getValue()).idf;
				double tf2 = ((Pair)o.getValue()).tf;
				xy += tf1*idf1*tf2*idf2;
			}
		}
		sim = xy / (x2+y2-xy);
		return sim;
	}
}