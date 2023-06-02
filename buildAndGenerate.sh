echo "Removing old class files"
rm */*.class */*/*.class

echo "Compiling source code."
javac -cp genclass.jar:infrastructure */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo " RMI Registry"
rm -rf dirRMIRegistry
mkdir dirRMIRegistry dirRMIRegistry/interfaces
cp interfaces/*.class dirRMIRegistry/interfaces

echo " Register Remote Objects"
rm -rf dirRegistry
mkdir dirRegistry dirRegistry/serverSide dirRegistry/serverSide/main dirRegistry/serverSide/objects \
      dirRegistry/interfaces dirRegistry/infrastructure
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces
cp infrastructure/ExecParameters.class dirRegistry/infrastructure
cp genclass.jar dirRegistry 

echo "	General Repository of Information"
rm -rf dirGenRepos
mkdir -p dirGenRepos dirGenRepos/serverSide dirGenRepos/serverSide/main dirGenRepos/serverSide/entities dirGenRepos/serverSide/objects \
         dirGenRepos/infrastructure dirGenRepos/interfaces
cp serverSide/main/ServerGeneralRepos.class dirGenRepos/serverSide/main
cp serverSide/entities/*.class dirGenRepos/serverSide/entities
cp serverSide/objects/GeneralRepos.class 
cp interfaces/GeneralReposInterface.class dirGenRepos/interfaces
cp infrastructure/*.class dirGenRepos/infrastructure
cp genclass.jar dirGenRepos

echo "	Concentration Site"
rm -rf dirCCS
mkdir -p dirCCS dirCCS/serverSide dirCCS/serverSide/main dirCCS/serverSide/entities dirCCS/serverSide/objects \
	dirCCS/interfaces dirCCS/infrastructure 
cp serverSide/main/ServerConcentrationSite.class dirCCS/serverSide/main
cp serverSide/entities/*.class dirCCS/serverSide/entities
cp serverSide/objects/ConcentrationSite.class dirCCS/serverSide/objects
cp infrastructure/*.class dirCCS/infrastructure
cp interfaces/*.class dirCCS/interfaces
cp genclass.jar dirCCS

echo "	Collection Site" 
rm -rf dirCCL
mkdir -p dirCCL dirCCL/serverSide dirCCL/serverSide/main dirCCL/serverSide/entities dirCCL/serverSide/objects \
	dirCCL/interfaces dirCCL/infrastructure
cp serverSide/main/ServerCollectionSite.class dirCCL/serverSide/main
cp serverSide/entities/*.class dirCCL/serverSide/entities
cp serverSide/objects/ControlCollectionSite.class dirCCL/serverSide/objects
cp interfaces/*.class dirCCL/interfaces
cp infrastructure/*.class dirCCL/infrastructure
cp genclass.jar dirCCL

echo "	Assault Party" 
rm -rf dirAP
mkdir -p dirAP dirAP/serverSide dirAP/serverSide/main dirAP/serverSide/entities dirAP/serverSide/objects \
	dirAP/interfaces dirAP/infrastructure
cp serverSide/main/ServerAssaultParty.class dirAP/serverSide/main
cp serverSide/entities/*.class dirAP/serverSide/entities
cp serverSide/objects/AssaultParty.class dirAP/serverSide/objects
cp interfaces/*.class dirAP/interfaces
cp infrastructure/*.class dirAP/infrastructure
cp genclass.jar dirAP

echo "	Museum" 
rm -rf dirMuseum
mkdir -p dirMuseum dirMuseum/serverSide dirMuseum/serverSide/main dirMuseum/serverSide/entities dirMuseum/serverSide/objects \
	dirMuseum/interfaces dirMuseum/infrastructure
cp serverSide/main/ServerMuseum.class dirMuseum/serverSide/main
cp serverSide/entities/*.class dirMuseum/serverSide/entities
cp serverSide/objects/Museum.class dirMuseum/serverSide/sharedRegions
cp interfaces/*.class dirMuseum/interfaces
cp infrastructure/*.class dirMuseum/infrastructure
cp genclass.jar dirMuseum

echo "	MasterThief"
rm -rf dirMClient
mkdir -p dirMClient dirMClient/clientSide dirMClient/clientSide/main dirMClient/clientSide/entities \
         dirMClient/interfaces dirMClient/infrastructure
cp clientSide/main/ClientMasterThief.class dirMClient/clientSide/main
cp clientSide/entities/mClient.class clientSide/entities/mStates.class dirMClient/clientSide/entities
cp interfaces/*.class dirMClient/interfaces
cp infrastructure/*.class dirMClient/infrastructure
cp genclass.jar dirMClient

echo "	OThieves"
rm -rf dirOClient
mkdir -p dirOClient dirOClient/clientSide dirOClient/clientSide/main dirOClient/clientSide/entities \
         dirOClient/interfaces dirOClient/infrastructure
cp clientSide/main/ClientOrdinaryThief.class dirOClient/clientSide/main
cp clientSide/entities/oClient.class clientSide/entities/oStates.class dirOClient/clientSide/entities
cp interfaces/*.class dirOClient/interfaces
cp infrastructure/*.class dirOClient/infrastructure
cp genclass.jar dirOClient

echo "Compressing execution environments."
echo " RMI Registry"
ŕm -rf dirRMIRegistry.zip
zip -rq dirRMIRegistry.zip dirRMIRegistry
echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry
echo "	General Repository of Information"
rm -f  dirGenRepos.zip
zip -rq dirGenRepos.zip dirGenRepos
echo "	Concentration Site"
rm -f  dirCCS.zip
zip -rq dirCCS.zip dirCCS
echo "	Collection Site"
rm -f  dirCCL.zip
zip -rq dirCCL.zip dirCCL
echo "	Assault Party"
rm -f  dirAP.zip
zip -rq dirAP.zip dirAP
echo "	Museum"
rm -f  dirMuseum.zip
zip -rq dirMuseum.zip dirMuseum
echo "	Master"
rm -f  dirMClient.zip
zip -rq dirMClient.zip dirMClient
echo "	OrdThieves"
rm -f  dirOClient.zip
zip -rq dirOClient.zip dirOClient

