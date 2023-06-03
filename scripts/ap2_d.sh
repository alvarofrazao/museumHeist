CODEBASE="http://l040101-ws02.ua.pt/"$1"/classes/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     -cp .:genclass.jar serverSide.main.ServerAssaultParty 1
