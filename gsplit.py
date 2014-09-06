#!/usr/bin/python
import re
import numpy as np
import subprocess
import commands
import os
import sys
import time


#Purpose is to take the file ./Input/Graphs(N).txt and split it into numpr
#Smaller ones which can each be run on a separate process.
start=time.time()
N=int(sys.argv[1])
numpr=int(sys.argv[2])
c=-1
f=[]
for i in xrange(numpr):
	f.append(open('./temp/graphs'+str(N)+'_'+str(i)+'.txt','w+'))


for line in open('./Input/graphs'+str(N)+'.txt','r'):
	t=line.strip().split()
	if (t):
		if (t[0]=='Graph'):
			c=c+1
	if(c>=numpr):
		c=0
	f[c].write(line)

for i in xrange(numpr):
	f[i].close()
	
#File split Now Close temp files and open subprocesses
p=[]
for i in xrange(numpr):
	p.append(subprocess.Popen(['python','./gpar2.py',str(N),str(i)]))

print("Processes started")
for i in xrange(numpr):
	p[i].wait()
print('Finished')
end=time.time()
print('Runtime: '+str(end-start))
