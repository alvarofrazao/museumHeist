CODEBASE="file:///home/"$1"/test/museumHeist/dirRegistry/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerRegisterRemoteObject 22253 localhost 22254
