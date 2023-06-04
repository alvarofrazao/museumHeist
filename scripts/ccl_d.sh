CODEBASE="http://l040101-ws04.ua.pt/"$1"/classes/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     -Djava.security.debug=access,failure\
     -Dsun.rmi.transport.tcp.responseTimeout=5000\
     -cp .:genclass.jar serverSide.main.ServerControlSite
