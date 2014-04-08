package main;
import java.util.ArrayList;
import java.util.HashMap;

import centroid.CentroidVector;
import cluster.FormDataset;
import cluster.MyKMeans;
import net.sf.javaml.classification.SOM;
import net.sf.javaml.clustering.AQBC;
import net.sf.javaml.clustering.Cobweb;
import net.sf.javaml.clustering.IterativeKMeans;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.clustering.KMedoids;
import net.sf.javaml.core.Dataset;
import extend.CCDExtend;
import integrate.MaxConnGraph;
import integrate.mergeClass;
import util.Pair;

public class Main {
	static public void main(String [] args)
	{
		String path = "./src/tree/CCD";
		//extend
		CCDExtend sim = new CCDExtend(path);
	    sim.expandSeg("./MH370Seg.txt");
	    
	    //integrate
	    MaxConnGraph cb = new MaxConnGraph(sim.retExpansion());
	    ArrayList<Pair> al = cb.run();
	    mergeClass m = new mergeClass();
	    m.merge(al, cb.sumofclass); //得到了合并以后的微博
	    
	    //cluster
	    ArrayList<HashMap<String, Pair>> centroids = new ArrayList<HashMap<String, Pair>>();
	    for(int i=0; i<cb.sumofclass; i++) {
	    	HashMap<String, Pair> hm = (new CentroidVector()).tfidfsort(m.getMergedClass().get(i));
	    	centroids.add(hm);
	    }
	    Dataset data = (new FormDataset()).form(centroids);
	    Dataset[] clusters = (new KMeans()).cluster(data);
	    
	    for(int i=0; i<clusters.length; i++) {
	    	for(int j=0; j<clusters[i].size(); j++) {
	    		System.out.println(clusters[i].instance(j).getID());
	    	}
	    	System.out.println();
	    }
	    
	    System.out.println(clusters);
	    
	    
	    
	    
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
