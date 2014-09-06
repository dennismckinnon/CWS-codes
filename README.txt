README FILE

Structure:
gsplit.py - Program you run

	USE: python gsplit.py N C

	It takes arguments N, C
	N: Number of qubits in the graph
	C: Number of processes to spawn
	
	The program accesses ./Input/graphs(N).txt and splits them into
	C temporary files in ./temp/graphs(N)_(i).txt where i is from 0
	to C-1.

	Using Popen, C instances of gpar2.py are opened each passed N,
	and ID, a unique identifier between 0 and C-1

gpar2.py - Implements the CWS algorithm

	USE: python gpar.py N ID

	It takes arguements N, ID
	N:  Number of qubits
	ID: Integer Identifier of which process instance it is

	The program runs the CWS algorithm, accessing files located in
	./Input/errorfile(N).txt and ./temp/graphs(N)_(ID).txt which
	store the errorfiles and the graphs respectfully. The program
	produces temporary files ./temp/cliquecode_(ID).txt which are
	passed to the java program "Clique" the output of which is
	piped into the file ./Output/maxcliques(N)_(ID).txt. In
	addition the associated graph for each maxclique output is
	stored in ./Output/gtrack(N)_(ID).txt There is an ordered 
	correspondance between the lines of these files (3:1)

	!!!!!!!!!!!!!!!!!!!!!!!!!!!WARNING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	The maxcliques file does not get overwritten when the program
	runs (I plan to fix this). This means that if the program is run
	multiple times on the same input data, these files must be
	removed in between or they will break the correspondance to
	gtrack
	!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

	The files ./temp/saveplace_(ID).txt must exist before running
	the program. They are the contingency feature for if the server
	stops before it is finished. If restarted It will pick up where
	it left off. file reads "0 0" if program sucessfully finished or
	hasn't yet started.

Additional Programs

./Output/findbig.py - Finds the maximum maxclique
	
	USE: python findbig.py N C

	It takes arguements N, C
	N: Number of qubits
	C: Number of processes which WERE used in gsplit.py

	The program accesses all files ./Output/maxcliques(N)_(ID).txt
	where ID is between 0 and C-1 and ./Output/gtrack(N)_(ID).txt
	and finds the maximum maxclique, prints it out along with the
	graph and errorset it is associated with.

./Output/findgofm.py -  Finds all graphs which a particular size maxclique

	USE: python findgofm.py N C M

	N: Number of qubits
	C: Number of prcoesses which were spawned by gsplit.py
	M: Size of maxclique you are looking for

	Like findbig.py but prints out all graphs which a certain size
	maxclique (Note since we do not use cliquer this is only reliable
	for the true maximum clique (for example a graph may have a clique
	of 12 but that doesn't show up since it has one of 16 too)

That's all folks!
