package cluster;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import util.Pair;
import centroid.CentroidVector;
public class HierarchicalCluster {
	public Node root = new Node();
	// 总数据备份：下标是微博顺序
	public ArrayList<Pair> al;
	// 等价类合并:下标是等价类序号
	public ArrayList<HashMap<String, Pair>> mergedClass = new ArrayList<HashMap<String, Pair>>();
	// 等价类的微博号：下标是等价类序号
	public ArrayList<ArrayList<Integer>> classet = new ArrayList<ArrayList<Integer>>();
	
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
	
	public Node mergeNode(Node n1, Node n2) {
		/*
		 * 两个结点合并
		 * 	sons指针改变
		 * 	clasHere合并
		 *  mergeHM合并
		 */
		Node n = new Node();
		n.mergeHM = mergeHM(n1.mergeHM, n2.mergeHM);
		n.clasHere.addAll(n1.clasHere);
		n.clasHere.addAll(n2.clasHere);
		n.sons.add(n1);
		n.sons.add(n2);
		return n;
	}
	/*
	 * @param ArrayList< Pair<HashMap, classnum> >
	 * 上面ArrayList依旧是原始Weibo的顺序
	 */
	public HierarchicalCluster(ArrayList<Pair> al, int sumofclass) {
		this.al = al;
		
		//System.out.println(sumofclass);
		for(int i=0; i<sumofclass; i++) {
			classet.add(new ArrayList<Integer>());
		}
		// 等价类的微博号
		for(int i=0; i<al.size(); i++) {
			if(al.get(i).classnum >= 92)
				System.out.println(i);
			classet.get(al.get(i).classnum).add(i);
		}
		//等价类合并
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
		// 初始化树
		for(int i=0; i<sumofclass; i++) {
			root.clasHere.add(i);
			
			Node n = new Node();
			n.clasHere.add(i);
			n.mergeHM = mergedClass.get(i);
			
			root.sons.add(n);
		}
	}
	
	
	
	public void hieCLuster() {
		/*
		 * 每层的结点数是相同的，没进行一次cluster从root下再添加一层
		 */
		int idx1 = -1, idx2 = -1;
		double sim = -1; 
		for(int i=0; i<root.sons.size(); i++) {
			// 对每两个类进行计算
			for(int j=i+1; j<root.sons.size(); j++) {
				// 计算两个mergeHM的Similarity
				HashMap<String, Pair> centroid1 = 
						(new CentroidVector()).tfidfsort(root.sons.get(i).mergeHM);
				HashMap<String, Pair> centroid2 = 
						(new CentroidVector()).tfidfsort(root.sons.get(j).mergeHM);
				double simtmp = (new ExtendedJaccard()).extendedJaccard(
						centroid1, centroid2);
				if(simtmp > sim) {
					idx1 = i;
					idx2 = j;
					sim = simtmp;
				}
			}
		}
		
		// 得到了一对需要合并的结点
		ArrayList<Node> _sons = new ArrayList<Node>();//备用位置
		for(int i=0; i<root.sons.size(); i++) {
			if(i == idx1 || i == idx2)
				continue;
			
			Node n = new Node();
			n.clasHere.addAll(root.sons.get(i).clasHere);
			n.mergeHM.putAll(root.sons.get(i).mergeHM);
			n.sons.add(root.sons.get(i));
			
			_sons.add(n);
		}
		Node n = mergeNode(root.sons.get(idx1), root.sons.get(idx2));
		_sons.add(n);
		root.sons = _sons;
	}
	
	
	public class Node {
		// 连接Sons的指针（用于索引）
		public ArrayList<Node> sons = new ArrayList<Node>();
		// 该节点聚类的等价类号（用于聚类计算）
		public ArrayList<Integer> clasHere = new ArrayList<Integer>();
		// 该结点微博的合并
		public HashMap<String, Pair> mergeHM = new HashMap<String, Pair>();
	}
}

