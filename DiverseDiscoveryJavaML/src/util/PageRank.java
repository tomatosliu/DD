package util;

import util.Graph;

public class PageRank {
	final static int numofpapers = 16;
	final static double d  = 0.85;
	final static double threshold = 0.00000000000001;
	double PR[] = new double[numofpapers];
	int rank[] = new int[numofpapers];
	public void calPR(Graph graph) {
		//initiate
		for(int i=0; i<numofpapers; i++) {
			PR[i] = graph.graphnodes[i];
			rank[i] = i+1;
		}
		
		//Link Out
		double L[] = new double[numofpapers];
		for(int i=0; i<numofpapers; i++) {
			L[i] = 0;
			for(int j=0; j<numofpapers; j++) {
				if(i == j) continue;
				L[i] += graph.graphedges[j][i];
			}
		}
		
		//calculate PR
		int times = 0;
		while(true) {
			int sum = 0;
			
			for(int i=0; i<numofpapers; i++) {
				double tmpsum = 0;
				
				for(int j=0; j<numofpapers; j++) {
					if(i==j) continue;
					if(graph.graphedges[i][j] > 0) {
						tmpsum += PR[j]/L[j];
					}
				}
				
				tmpsum = tmpsum*d + (1-d);
				if(tmpsum-PR[i] < threshold
						&& tmpsum-PR[i] > -threshold)
					sum ++;
				PR[i] = tmpsum;
			}
			
			System.out.println(sum);
			for(int i=0; i<numofpapers; i++)
				System.out.println(PR[i]);
			System.out.println("********\n");
			if(sum == numofpapers)
				break;
		}
		
		//sort
		for(int i=0; i<numofpapers; i++) {
			for(int j=i+1; j<numofpapers; j++) {
				if(PR[i] < PR[j]) {
					double tmp = PR[i];
					PR[i] = PR[j];
					PR[j] = tmp;
					
					int tmp1 = rank[i];
					rank[i] = rank[j];
					rank[j] = tmp1;
				}
			}
		}
		
		//Out
		for(int i=0; i<numofpapers; i++) {
			System.out.println("No."+String.valueOf(i+1) +
					 " "+ "Paper: " + String.valueOf(rank[i]) + " " + "Score: " + String.valueOf(PR[i]));
		}
	}
}