CODEBASE="file:///home/"$1"/test/museumHeist/dirMClient/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientMasterThief #localhost 22254 stat 3
