package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Graph for the network
 * @author Tomato
 */
public class Graph {
	//static number of co-author
	final static int numofpaper = 16;
	//edges for the graph
	public double [][]graphedges;
	//nodes for the graph
	public double []graphnodes;
	
	/**
	 * constructor
	 */
	public Graph() {
		graphedges = new double[numofpaper][numofpaper];
	}
	
	public void readinEdge() {
		File file = new File("cite.txt");
        BufferedReader reader = null;
        String tempString = null;
        
        try {
			reader = new BufferedReader(new FileReader(file));
			
			for(int i=0; i<numofpaper; i++) {
				tempString = reader.readLine();
				if(tempString.equals("")) continue;
				String str[] = tempString.split(" ");
				for(int j=0; j<str.length; j++) {
					graphedges[i][Integer.parseInt(str[j])-1] = 1;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
}