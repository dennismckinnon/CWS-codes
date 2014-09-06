import java.util.*;
import java.io.*;

public class Rand_Graph {
   public static MClique create_rand(int V, int E) {
	MClique mc = new MClique(V);
	Random generator = new Random(new Date().getTime());
	for(int i = 0; i < V; i++) 
	    mc.add_vertex(i);

	int nE = 0, r1, r2;
	while(nE < E) {
	    r1 = Math.abs(generator.nextInt()) % V;
	    r2 = Math.abs(generator.nextInt()) % V;
	    if(r1 != r2 && !mc.is_edge(r1, r2)) {
	    	mc.add_edge(r1, r2);
		mc.add_edge(r2, r1);
		nE++;
	    }
	}
	return(mc);	
   }


    public static void main(String[] args) throws IOException {
	int V = Integer.parseInt(args[0]);
	float f = new Float(args[1]);
	int E;
	if(f < 1) {
	    E = (int)(V * (V - 1) * f / 2);
	} else {
	    E = (int)f;
	}
	MClique mc = create_rand(V, E);
	mc.list_show();
    }
}
