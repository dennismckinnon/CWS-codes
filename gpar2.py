#!/usr/bin/python
import re
import numpy as np
import subprocess
import commands
import os
import sys
import time

def bbconv(len,N,b):
	v=np.zeros(len,dtype=np.int)
	for i in xrange(1, len+1):
		y=len-(i);
		bp=b**y
		for j in xrange(1,b):
			if((N-bp)>=0):
				N=N-bp
				v[i-1]=v[i-1]+1
			else:
				break
	return v

def intbit(v,b):
	N=0
	s=v.shape[0]-1
	for x in v:
		N=N+x*(b**s)
		s=s-1
	return N

#Goal is to process the output files obtain the adjacency matrix and run the CWS algorithm

N=int(sys.argv[1])
prID=int(sys.argv[2])
outfile=open('/scratch/jmille16/tylers_code/Output/maxcliques'+str(N)+'_'+str(prID)+'.txt','w+')
gtrack=open('/scratch/jmille16/tylers_code/Output/gtrack'+str(N)+'_'+str(prID)+'.txt','w+')
cliquecode=('/scratch/jmille16/tylers_code/temp/cliquecode_'+str(prID)+'.txt')

print(str(prID))

#For restarting after a crash
sav=open('/scratch/jmille16/tylers_code/temp/saveplace_'+str(prID)+'.txt','r')
sa=sav.readline()
sv=sa.strip().split()
sav.close()
sv=map(int,sv)

c=0
size=0
gcount=1
#go through each graph
for line in open('/scratch/jmille16/tylers_code/temp/graphs'+str(N)+'_'+str(prID)+'.txt','r'):
	t=line.strip().split()
	if (t):
		#Each graph starts with the word graph in the file

		if (t[0]=='Graph' and gcount>=sv[0]):
			gcount=gcount+1
			size=int(t[3].replace(".",""))
			gnum=int(t[1].replace(",",""))
			c=0
		elif (t[0]=='Graph' and gcount<sv[0]):
			gcount=gcount+1
		elif (gcount<=sv[0]):
			pass
		else:
			#constructs the adjacency matrix from the file
			t=map(int,t)
			if (c==0):
				M=np.asarray(t)
				c=1
			else:
				a=np.asarray(t)
				M=np.vstack((M,a))
				c=c+1
			if(c==size):
				print('Graph: '+str(gnum))
				print(M)
				#Once the Matrix is complete loop through errors in error file.
				CL=np.zeros(2**N,dtype=np.int)
				D=np.zeros(2**N,dtype=np.int)
				ecount=1
				eset=0
				for eline in open('/scratch/jmille16/tylers_code/Input/errorfile'+str(N)+'.txt','r'):
					te=eline.strip()
					#The word Errorset denotes the END of an errorset so when it 
					#Is found, Submit CL and D to algorithm 2
					if (te=='Errorset' and ecount>=sv[1]):
						eset=eset+1
						sv[1]=0
						#CL and D complete ready to be used in algorithm 2
						V=[]
						V.append(0)
						E=[]
						infl=0
						for i in xrange(1,2**N):
							if(CL[i]==0 and D[i]==0):
								for v in V:
									v1=bbconv(N,v,2)
									s=bbconv(N,i,2)
									test=(v1+s)%2
									tet=intbit(test,2)
									if(CL[tet]==0):
										if(v!=0):
											infl=1
										E.append([v,i])
								V.append(i)
						#At this point V and E are complete. Write to file and call java program
						if(E and infl==1):
							vn=max(V)+1
							en=len(E)
							gr=open(cliquecode,'w+')
							gr.write(str(vn)+' '+str(en))
							for v in V:
								gr.write('#'+str(v)+':')
								for e in E:
									if(e[0]==v):
										gr.write(str(e[1])+' ')
									elif(e[1]==v):
										gr.write(str(e[0])+' ')
							gr.close()
							#CALL JAVA PROGRAM!
							#subprocess.call(['ls','-l'],stdout=outfile)
							subprocess.call(['java','-Xmx512m','-XX:+UseSerialGC','-classpath','/scratch/jmille16/tylers_code','Clique',cliquecode],stdout=outfile)
							gtrack.write('Graph: '+str(gnum)+' '+str(eset)+'\n')
							#RESET FOR NEXT ERROR SET
						CL=np.zeros(2**N,dtype=np.int)
						D=np.zeros(2**N,dtype=np.int)
						ecount=ecount+1
						#adjusts the saved place
						sav=open('/scratch/jmille16/tylers_code/temp/saveplace_'+str(prID)+'.txt','w+')
						sav.write(str(gcount-1)+' '+str(ecount))
						sav.close()
					elif (te=='Errorset' and ecount<sv[1]):
						ecount=ecount+1
					elif (ecount<sv[0]):
						pass
					else:
						#This Is algorithm 1 It runs for each error in the errorset
						ERR=np.zeros(N,dtype=np.int)
						ERRX=np.zeros(N,dtype=np.int)
						spot=-1
						for ch in te:
							spot=spot+1
							if (ch=='X' or ch=='Y'):
								ERR=(ERR+M[spot])%2
								ERRX[spot]=(ERR[spot]+1)%2
							if (ch=='Y' or ch=='Z'):
								ERR[spot]=(ERR[spot]+1)%2
						ei=intbit(ERR,2)
						CL[ei]=1
						if ei==0:
							for i in xrange(0,2**N):
								v=bbconv(N,i,2)
								if(np.dot(ERRX,v)!=0):
									D[i]=1
#That should do it
sav=open('/scratch/jmille16/tylers_code/temp/saveplace_'+str(prID)+'.txt','w+')
sav.write('0 0')
sav.close()
