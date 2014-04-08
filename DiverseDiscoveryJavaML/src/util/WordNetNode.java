package util;

import java.util.ArrayList;

public class WordNetNode {	//WordNet树节点
	protected ArrayList<String> words;	//words集合，所有的words构成一个同义词集合，即一个concept,一个WordNet树节点
    protected int father;	//该节点父节点在WordNetTree中 Index的下标
    protected ArrayList<Integer> sons;     //子节点集合，存放在nodes中的下标
    protected int depth;  //深度
    protected WordNetNode()
    {
    	father = depth = -1;
    	words = new ArrayList<String>();
    	sons = new ArrayList<Integer>();
    }

    protected ArrayList<String> retTheConcept() {
        return words;
    }
}

