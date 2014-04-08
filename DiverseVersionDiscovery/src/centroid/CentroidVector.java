package centroid;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import util.Pair;

public class CentroidVector {
	// HashMap<String, Pair> ������������tf*idf���������������������������
	
	
	public HashMap<String, Pair> tfidfsort(HashMap<String, Pair> hm) {
		HashMap<String, Pair> gethm = new HashMap<String, Pair>();
		
		Map<String, Pair> _hm = hm;
		List<Map.Entry<String, Pair>> l = new ArrayList<Map.Entry<String, Pair>>(
	                _hm.entrySet());
		Collections.sort(l, new Comparator<Map.Entry<String, Pair>>() {
	            public int compare(Map.Entry<String, Pair> o1,
	                    Map.Entry<String, Pair> o2) {
	                //if (((Pair)(o2.getValue())).tf*((Pair)(o2.getValue())).idf 
	                //		> ((Pair)(o1.getValue())).tf*((Pair)(o1.getValue())).idf)
	                if (((Pair)(o2.getValue())).idf 
		                	> ((Pair)(o1.getValue())).idf)
	                	return 1;
	                else
	                	return -1;
	            }
	        });
		int i;
		Iterator<Entry<String, Pair>> it;
		for(it=l.iterator(),i=0;it.hasNext() && i<10; i++)
		{
			Entry<String, Pair> en = it.next();
			//System.out.println(it.next().getValue().tf*it.next().getValue().idf);
			//System.out.println(it.next().getKey());
			gethm.put(en.getKey(), en.getValue());
		}
		
		
		return gethm;
	}
	
}