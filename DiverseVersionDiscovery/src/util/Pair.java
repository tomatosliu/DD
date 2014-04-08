package util;

import java.util.HashMap;

public class Pair {
	public double tf = 0;
	public double idf = 0;
	public HashMap<String, Pair> hm;
	public int classnum;
	
	public Pair(double tf, double idf) {
		this.tf = tf;
		this.idf = idf;
	}
	public Pair(HashMap<String, Pair> hm, int classnum) {
		this.hm = hm;
		this.classnum = classnum;
	}
}