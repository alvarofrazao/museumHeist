CODEBASE="file:///home/"$1"/test/museumHeist/dirMuseum/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.Museum #22003 localhost 22000
