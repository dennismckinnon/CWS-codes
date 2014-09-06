#!/usr/bin/python
import sys

N=int(sys.argv[1])
numpr=int(sys.argv[2])

max=0
for i in xrange(numpr):
	infile='maxcliques'+str(N)+'_'+str(i)+'.txt'
	g=open('gtrack'+str(N)+'_'+str(i)+'.txt','r')
	for line in open(infile,'r'):
		t=line.strip().split()
		if (t[0]=='max'):
			gline=g.readline().strip().split()
			b=int(t[3].replace(".",""))
			if(b>max):
				gmax=gline[1]
				max=b
print('The Largest is: '+str(max)+' and belongs to graph: '+gmax)