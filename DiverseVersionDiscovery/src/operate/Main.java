package operate;
import java.util.ArrayList;
import java.util.HashMap;

import cluster.HierarchicalCluster;
import extend.CCDExtend;
import integrate.MaxConnGraph;
import util.Pair;
import util.WordNetTree;
import util.StopWord;
public class Main {
	static public void main(String [] args)
	{
		String path = "./src/tree/CCD";
		CCDExtend sim = new CCDExtend(path);
		
	    

	    sim.expandSeg("./MH370Seg.txt");
	    MaxConnGraph cb = new MaxConnGraph(sim);
	    ArrayList<Pair> al = cb.run();
	    
	    HierarchicalCluster hc = new HierarchicalCluster(al, cb.getSum());
	    for(int i=0; i<135; i++)
	    	hc.hieCLuster();
	    System.out.println();
	    //String word1="恐怖分子";
	    //String word2="自行车";
	    //sim.getSimilarity(word1, word2);
	    //System.out.println(sim.getSimilarity(word1, word2));
	    //WordNetTree wnt = new WordNetTree(path);
	    //wnt.getSimWords(word1);
	    
	    //String str = "马航";
	    //if((new StopWord()).contains(str))
	    //	System.out.println(str);
	}
}
