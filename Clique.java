import java.util.*;
import java.io.*;

public class Clique {
    public static MClique read_graph(String filename) throws IOException {
		FileReader fileReader = new FileReader(filename);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line = bufferedReader.readLine();
		fileReader.close();
	
		String[] data = line.split("#");
		String[] V_E = data[0].split(" ");
		
		int V, i, j;
		V = Integer.parseInt(V_E[0]);
		MClique mc = new MClique(V);
		for(i = 0; i < V; i++)
			mc.add_vertex(i);

		for(i = 1; i < data.length; i++) {
			String[] line_a = data[i].split(":");
			int v = Integer.parseInt(line_a[0]);
			String[] s = line_a[1].split(" ");
			for(j = 0; j < s.length; j++) {
				int w = Integer.parseInt(s[j]);
				mc.add_edge(v, w);
				mc.add_edge(w, v);
			}
		}
	
		return(mc);
    }
    
    public static void copy_g(int[] Q, int[] Qmax) {
	    if(Q[0] > Qmax[0]) {
			for(int i = 0; i <= Q[0]; i++) {
				Qmax[i] = Q[i];
			}
	    }	
    }

    public static void buble_sort(int[] g, int[] deg) {
		int i, j, tmp;
		boolean swapped = true;
		i = 0;
		while(swapped) {
			swapped = false;
			i++;
			for(j = 1; j <= g[0] - i; j++) {
				if(deg[g[j]] < deg[g[j + 1]]) {
					tmp = g[j];
					g[j] = g[j + 1];
					g[j + 1] = tmp;
					swapped = true;
				}
			}
		}
    }
    
    public static long[] run(String[] args) throws IOException {
		long start_time = new Date().getTime();

		String file = args[0];
	
		MClique mc = read_graph(file);
       	int[] g0 = mc.get_g();
		int VV = g0[0];
		int EE = 0;

		int[] g = new int[g0[0] + 1];
		for(int i = 0; i <= g0[0]; i++)
			g[i] = g0[i];
	
		int[] tmpN = new int[g[0]];
		int[] tmp_R = new int[g[0] + 1];
		int[] Q, Qmax = new int[g[0] + 1];
		Qmax[0] = 0;
		
        //if(Integer.parseInt(args[1]) == 1) {
        if(true) {  // always use color partion
			System.out.println("Color Partition ....");
			mc.sort(g, tmpN, tmp_R);
		} else if(Integer.parseInt(args[1]) == 2) {
			System.out.println("Reverse Color Partition ....");
			mc.sort(g, tmpN, tmp_R);
			for(int i = 1; i <= g[0] / 2; i++) {
				int tmp = g[i];
				g[i] = g[g[0] + 1 - i];
				g[g[0] + 1 - i] = tmp;
			}
		} else if(Integer.parseInt(args[1]) == 3) {
			System.out.println("Random Partition ....");
		} else if(Integer.parseInt(args[1]) == 4) {
			System.out.println("Order Partition ....");
			int[] deg = new int[g[0]];
			mc.degree_all(g, deg);
			buble_sort(g, deg);
		}

		mc.set_g(g);
		EE += mc.E();	
		Q = mc.run("1x", Qmax[0]);
		copy_g(Q, Qmax);

		long[] result = new long[2];
		result[0] = mc.step();
		result[1] = new Date().getTime() - start_time;
	    
		System.out.format("max clique = %d. EE = %d, step = %d, time = %d \n",
						  Qmax[0], EE, mc.step(), result[1]);
		for(int i = 1; i <= Qmax[0]; i++) 
			System.out.format("%d ", Qmax[i]);
		System.out.format("\n");

		return(result);
    }

    public static void main(String[] args) throws IOException {
		long[] result = run(args);
    }
}
