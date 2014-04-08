package cluster;

import net.sf.javaml.clustering.Clusterer;
import net.sf.javaml.clustering.KMeans;
import net.sf.javaml.core.Dataset;

public class MyKMeans {
	public Dataset[] cluster(Dataset data) {
		Clusterer km = new KMeans();
        return km.cluster(data);
	}
}
