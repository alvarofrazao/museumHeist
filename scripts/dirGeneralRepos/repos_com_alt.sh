CODEBASE="file:///home/"$1"/test/museumHeist/dirGeneralRepos/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerGeneralRepos 22250 localhost 22254
