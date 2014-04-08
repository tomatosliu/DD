package cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Pair;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.core.SparseInstance;

public class FormDataset {
	public Dataset form(ArrayList<HashMap<String, Pair>> al) {
		Dataset data = new DefaultDataset();
		
		// 所有String行程词典
		ArrayList<String> dic = new ArrayList<String>();
		for(int i=0; i<al.size(); i++) {
			// 每条微博形成一个SparseInstance（index：String HashCode，ClassNum）
			//Instance instance = new SparseInstance(MAX_VALUE, i);
			Set set = al.get(i).entrySet();
			Iterator it = set.iterator();
			while(it.hasNext()) {
				Map.Entry o = (Map.Entry)it.next();
				String word = (String)(o.getKey());
				if(!dic.contains(word))
					dic.add(word);
				else
					System.out.println(word);
			}
		}
		System.out.println(dic.size());
		
		//行程dataset
		final int MAX_VALUE = 0x7fffffff;
		for(int i=0; i<al.size(); i++) {
			// 每条微博形成一个SparseInstance（index：String HashCode，ClassNum）
			Instance instance = new SparseInstance(dic.size(), i);
			//Instance instance = new DenseInstance(10);
			Set set = al.get(i).entrySet();
			Iterator it = set.iterator();
			while(it.hasNext()) {
				Map.Entry o = (Map.Entry)it.next();
				double tfidf = ((Pair)o.getValue()).idf * ((Pair)o.getValue()).tf;
				instance.put(dic.indexOf((String)(o.getKey())), tfidf*10000);
				//instance.add(tfidf);
			}
			data.add(instance);
		}
		return data;
	}
}