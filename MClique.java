//package graph;

import java.util.*;
import java.io.*;

public class MClique {
    long STEP = 0;
    int V0, E;
    int[][] G, C, Rp, Tmp_No;
    int[] g, Q, Qmax, RR, Rmin, gg, deg;

    public MClique(int V) {
		int i, j;
		G = new int[V][V];
		g = new int[V + 1];
		Q = new int[V + 1];
		Qmax = new int[V + 1];
		C = new int[V][V + 1];
		Rp = new int[V][V + 1];
		Tmp_No = new int[V][V + 1];
		RR = new int[V + 1];
		Rmin = new int[V + 1];
		gg = new int[V + 1];
		deg = new int[V + 1];
		
		V0 = V;
		g[0] = 0;	
    }

    public void add_vertex(int v) {
		g[0]++;
		g[g[0]] = v;
    }

    public void add_edge(int v, int w) {
		G[v][w] = 1;
    }

    public boolean is_edge(int v, int w) {
        if(G[v][w] == 1)
            return true;
        else
            return false;
    }
	
    public long step() {
		return STEP;
    }

    public int E() {
		int[] deg = new int[5000];

		degree_all(g, deg);
		E = 0;
		for(int i = 1; i <= g[0]; i++)
			E += deg[g[i]];

		return E;
    }
    
    public void show() {
		int i, j;

		System.out.format("   ");
		for(i = 1; i <= g[0]; i++) {
			System.out.format("%2d ", g[i]);
		}
		System.out.format("\n");
	
		for(i = 1; i <= g[0]; i++) {
			System.out.format("%2d: ", g[i]);
			
			for(j = 1; j <= g[0]; j++) {
				if(G[g[i]][g[j]] == 1)
					System.out.format("1  ");
				else
					System.out.format("0  ");
			}
			System.out.format("\n");
		}	
    }

    public void list_show() {
		int i, j, E;
		E = 0;

		for(i = 1; i <= g[0]; i++) {
			for(j = 0; j < g[0]; j++) {
				if(G[g[i]][j] == 1) E++;		
			}
		}

		E /= 2;
		System.out.format("%d %d", g[0], E);
		for(i = 1; i <= g[0]; i++) {
			System.out.format("#%d:", g[i]);
			for(j = 0; j < g[0]; j++) {
				if(G[g[i]][j] == 1) 
					System.out.format("%d ", j);
			}
		}
    }

    public String toString(int[] g1) {
		int i, j;
		StringBuffer out = new StringBuffer();
		
		for(i = 1; i <= g1[0]; i++) {
			out.append("#" + g1[i] + ":");
			for(j = 1; j <= g1[0]; j++) {
				if(G[g1[i]][g1[j]] == 1)
					out.append(g1[j] + " ");
			}
			out.deleteCharAt(out.length() - 1);
		}

		return out.toString();
    }
    
    public void del_vertex(int[] g1, int v) {
		int i = 1, j = 1;
		for(i = 1; i <= g1[0]; i++) {
			g1[j] = g1[i];
			if(g1[i] != v) 
				j++;	
		}
		g1[0]--;
    }   
	
    public int[] get_g() {
		return g;
    }

    public void set_g(int[] g1) {
		for(int i = 0; i <= g1[0]; i++) {
			g[i] = g1[i];
		}
    }
    
    public int[] sub_graph(int[] g1, int v) {
		int[] g = new int[g1[0]];
		g[0] = 0;
		for(int i = 1; i <= g1[0]; i++) {
			if(G[v][g1[i]] == 1) {
				g[0]++;
				g[g[0]] = g1[i];
			}
		}

		return g;
    }

    public void sub_graph(int[] g1, int v, int[] g) {
		int i;
		g[0] = 0;
		for(i = 1; i <= g1[0]; i++) {
			if(G[v][g1[i]] == 1) {
				g[0]++;
				g[g[0]] = g1[i];
			}
		}
	}

    public int degree_v(int[] g, int v) {
		int i, degree = 0;
		for(i = 1; i <= g[0]; i++) {
			if(G[g[i]][v] == 1) {
				degree++;
			}
		}
		return degree;
    }

    public void degree_all(int[] g, int[] deg) {
		for(int i = 1; i <= g[0]; i++) {
			deg[g[i]] = 0;
			for(int j = 1; j <= g[0]; j++) {
				if(G[g[i]][g[j]] == 1) 
					deg[g[i]]++;
			}
		}
    }
    
    public int min_degree(int[] g) {
		int min_d = 100000;
		for(int i = 1; i <= g[0]; i++) {
			int tmp = degree_v(g, g[i]);
			if(min_d > tmp) {
				min_d = tmp;
			}
		}	
		return min_d;
    }
    
    public int max_degree(int[] g) {
		int max_d = 0;
		for(int i = 1; i <= g[0]; i++) {
			int tmp = degree_v(g, g[i]);
			if(max_d < tmp) {
				max_d = tmp;
			}
		}

		return max_d;
    }

	
    public void min_degree_set(int[] g, int[] deg, int[] min_v) {
		int min_d = V0;
		int i, j;
	
		for(i = 1; i <= g[0]; i++) {
			if(min_d > deg[g[i]]) {
				min_d = deg[g[i]];
			}
		}
	
		min_v[0] = 0;
		for(i = 1; i <= g[0]; i++) {
			if(deg[g[i]] == min_d) {
				min_v[0]++;
				min_v[min_v[0]] = g[i];
			}
		}
    }

    public void adj(int[] g, int v, int[] g0) {
		g0[0] = 0;
		for(int i = 1; i <= g[0]; i++) {
			if(G[g[i]][v] == 1) {
				g0[0]++;
				g0[g0[0]] = g[i];
			}
		}
	}    
    
    public int min_ex_deg(int[] g) {
		int i, j, min_ex = g[0] * g[0];
		int q = 0;

		degree_all(g, deg);
		
		for(i = 1; i <= g[0]; i++) {
			adj(g, g[i], gg);
			int degree = 0;
			for(j = 1; j <= gg[0]; j++) {
				degree += deg[gg[j]];
			}
	    
			if(degree < min_ex) {
				min_ex = degree;
				q = g[i];
			}
		}

		return q;
    }
	
    
    public void sort(int[] g0, int[] No, int[] Rmin) {
		int p, i = g0[0];
		for(int j = 0; j <= g0[0]; j++)
			RR[j] = g0[j];

		degree_all(RR, deg);
		min_degree_set(RR, deg, Rmin);
		int Delta_G = 0;
		for(int j = 1; j <= RR[0]; j++) {
			if(Delta_G < deg[RR[j]])
				Delta_G = deg[RR[j]];
		}
	
		while(Rmin[0] != RR[0]) {
			if(Rmin[0] >= 2) {
				p = min_ex_deg(Rmin);
			} else {
				p = Rmin[1];
			}
			g0[i] = p;
		
			for(int j = 1; j <= RR[0]; j++)
				if(is_edge(RR[j], p))
					deg[RR[j]]--;
			
			del_vertex(RR, p);
			i--;

			min_degree_set(RR, deg, Rmin);
		}
	
		number_sort(Rmin, No);	
		for(int j = 1; j <= Rmin[0]; j++) {
			g0[j] = Rmin[j];
		}

		int m = 0;
		for(i = 1; i <= Rmin[0]; i++) {
			if(m < No[Rmin[i]]) {
				m = No[Rmin[i]];
			}
		}
	
		int mmax = Rmin[0] + Delta_G - m;
		m++;
		i = Rmin[0] + 1;
		while(i <= mmax) {
			if(i > g0[0]) return;
			No[g0[i]] = m;
			m++;
			i++;
		}

		for(i = mmax + 1; i <= g0[0]; i++) {
			No[g0[i]] = Delta_G + 1;
		}
    }

    public void number_sort(int[] R, int[] No) {
		int i, j, k, p, maxno = 0;
		boolean connect;
		
		// Reverse R;
	
		for(i = 1; i <= R[0] / 2; i++) {
			p = R[i];
			R[i] = R[R[0] + 1 - i];
			R[R[0] + 1 - i] = p;
		} 
	
		for(i = 0; i < R[0]; i++)
			C[i][0] = 0;
		
		while(R[0] > 0) {
			p = R[R[0]];
			k = 0;
			connect = true;
			while(connect) {
				connect = false;
				if(C[k][0] > 0) {
					for(i = 1; i <= C[k][0]; i++) {
						if(is_edge(C[k][i], p)) {
							connect = true;
							k++;
							break;
						}
					}
				}
			}
		
			if(k + 1 > maxno) {
				maxno = k + 1;
			}

			C[k][0]++;
			C[k][C[k][0]] = p;
			No[p] = k + 1;
			R[0]--;
		}

		k = 0;
		for(i = 0; i < maxno; i++) {
			for(j = 1; j <= C[i][0]; j++) {
				k++;		
				R[k] = C[i][j];
			}
		}
		R[0] = k;
    }    
	
    public void expand(int[] R, int[] No, int depth) {
		int i, p, k, bb = 10;
		
		while(R[0] > 0) {
			p = R[R[0]];
			if(Q[0] + No[p] <= Qmax[0])
				return;
			
			STEP++;
			Q[0]++;
			Q[Q[0]] = p;
			sub_graph(R, p, Rp[depth]);
			k = Rp[depth][0];
			
			if(k > 0) {
				number_sort(Rp[depth], Tmp_No[depth]);
				expand(Rp[depth], Tmp_No[depth], depth + 1);		
			} else if(Q[0] > Qmax[0]) {
				for(i = 0; i <= Q[0]; i++) 
					Qmax[i] = Q[i];
			}
			
			Q[0]--;	    
			R[0]--;
		}
	}

    public int[] run(String key, int max) {
		int i, j, max_d;
		int[] No = new int[V0];
		
     	sort(g, No, Rmin);
		Q[0] = 0;
		Qmax[0] = 0;

		String[] tmp = key.split("x");
		int keys_length = 0;
		if(tmp.length > 1) {
			String[] keys = key.split("x")[1].split(":");
			if(keys.length > 0) {
				for(i = 0; i < keys.length; i++) {
					Q[0]++;
					Q[i + 1] = Integer.parseInt(keys[i]);
					Qmax[0]++;
					Qmax[i + 1] = Integer.parseInt(keys[i]);
				}
				keys_length = keys.length;
			}
		}

		boolean cl = true;
		for(i = 1; i <= Rmin[0]; i++) {
			for(j = 1; j <= Rmin[0]; j++) {
				if(i != j && G[Rmin[i]][Rmin[j]] == 0) {
					cl = false;
					break;
				}
			}
		}
		if(cl) {
			for(i = 1; i <= Rmin[0]; i++) {
				Qmax[i + keys_length] = Rmin[i];
			}
			Qmax[0] += Rmin[0];
		}
	
		if(Qmax[0] < max)
			Qmax[0] = max;
		
		expand(g, No, 0);

		return Qmax;
    }
}
