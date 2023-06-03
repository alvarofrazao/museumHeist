CODEBASE="file:///home/"$1"/test/museumHeist/dirOClient/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientOrdinaryThief #localhost 22254 stat 3
