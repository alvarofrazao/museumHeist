echo "Removing old class files"
rm */*.class */*/*.class

echo "Compiling source code."
javac -cp genclass.jar:infrastructure */*.java */*/*.java
echo "Distributing intermediate code to the different execution environments."
echo "	General Repository of Information"
rm -rf dirGenRepos
mkdir -p dirGenRepos dirGenRepos/serverSide dirGenRepos/serverSide/main dirGenRepos/serverSide/entities dirGenRepos/serverSide/sharedRegions \
         dirGenRepos/serverSide/stubs dirGenRepos/infrastructure
cp serverSide/main/ServerGeneralRepos.class dirGenRepos/serverSide/main
cp serverSide/entities/GeneralReposClientProxy.class dirGenRepos/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/GeneralRepos.class dirGenRepos/serverSide/sharedRegions
cp serverSide/entities/mStates.class serverSide/entities/oStates.class dirGenRepos/serverSide/entities
cp infrastructure/*.class dirGenRepos/infrastructure
cp genclass.jar dirGenRepos

echo "	Concentration Site"
rm -rf dirCCS
mkdir -p dirCCS dirCCS/serverSide dirCCS/serverSide/main dirCCS/serverSide/entities dirCCS/serverSide/sharedRegions \
	dirCCS/serverSide/stubs dirCCS/infrastructure 
cp serverSide/main/ServerConcentrationSite.class dirCCS/serverSide/main
cp serverSide/entities/ccsClientProxy.class dirCCS/serverSide/entities
cp serverSide/sharedRegions/ConcentrationSiteInterface.class serverSide/sharedRegions/ConcentrationSite.class dirCCS/serverSide/sharedRegions
cp serverSide/entities/mStates.class serverSide/entities/oStates.class \
   dirCCS/serverSide/entities
cp serverSide/stubs/GeneralReposStub.class dirCCS/serverSide/stubs
cp infrastructure/*.class dirCCS/infrastructure
cp genclass.jar dirCCS

echo "	Collection Site" 
rm -rf dirCCL
mkdir -p dirCCL dirCCL/serverSide dirCCL/serverSide/main dirCCL/serverSide/entities dirCCL/serverSide/sharedRegions \
	dirCCL/serverSide/stubs dirCCL/infrastructure
cp serverSide/main/ServerCollectionSite.class dirCCL/serverSide/main
cp serverSide/entities/cclClientProxy.class dirCCL/serverSide/entities
cp serverSide/sharedRegions/ControlCollectionSiteInterface.class serverSide/sharedRegions/ControlCollectionSite.class dirCCL/serverSide/sharedRegions
cp serverSide/entities/mStates.class serverSide/entities/oStates.class \
   dirCCL/serverSide/entities
cp serverSide/stubs/GeneralReposStub.class dirCCL/serverSide/stubs
cp infrastructure/*.class dirCCL/infrastructure
cp genclass.jar dirCCL

echo "	Assault Party" 
rm -rf dirAP
mkdir -p dirAP dirAP/serverSide dirAP/serverSide/main dirAP/serverSide/entities dirAP/serverSide/sharedRegions \
	dirAP/serverSide/stubs dirAP/infrastructure
cp serverSide/main/ServerAssaultParty.class dirAP/serverSide/main
cp serverSide/entities/aPClientProxy.class dirAP/serverSide/entities
cp serverSide/sharedRegions/AssaultPartyInterface.class serverSide/sharedRegions/AssaultParty.class dirAP/serverSide/sharedRegions
cp serverSide/entities/mStates.class serverSide/entities/oStates.class \
   dirAP/serverSide/entities
cp serverSide/stubs/GeneralReposStub.class dirAP/serverSide/stubs
cp infrastructure/*.class dirAP/infrastructure
cp genclass.jar dirAP

echo "	Museum" 
rm -rf dirMuseum
mkdir -p dirMuseum dirMuseum/serverSide dirMuseum/serverSide/main dirMuseum/serverSide/entities dirMuseum/serverSide/sharedRegions \
	dirMuseum/serverSide/stubs dirMuseum/infrastructure
cp serverSide/main/ServerMuseum.class dirMuseum/serverSide/main
cp serverSide/entities/museumClientProxy.class dirMuseum/serverSide/entities
cp serverSide/sharedRegions/MuseumInterface.class serverSide/sharedRegions/Museum.class dirMuseum/serverSide/sharedRegions
cp serverSide/entities/mStates.class serverSide/entities/oStates.class \
   dirMuseum/serverSide/entities
cp serverSide/stubs/GeneralReposStub.class dirMuseum/serverSide/stubs
cp infrastructure/*.class dirMuseum/infrastructure
cp genclass.jar dirMuseum

echo "	MasterThief"
rm -rf dirMClient
mkdir -p dirMClient dirMClient/clientSide dirMClient/clientSide/main dirMClient/clientSide/entities \
         dirMClient/clientSide/stubs dirMClient/infrastructure
cp clientSide/main/ClientMasterThief.class dirMClient/clientSide/main
cp clientSide/entities/mClient.class clientSide/entities/mStates.class dirMClient/clientSide/entities
cp clientSide/stubs/AssaultPartyStub.class clientSide/stubs/ControlCollectionSiteStub.class  clientSide/stubs/ConcentrationSiteStub.class  clientSide/stubs/MuseumStub.class dirMClient/clientSide/stubs
cp infrastructure/*.class dirMClient/infrastructure
cp genclass.jar dirMClient

echo "	OThieves"
rm -rf dirOClient
mkdir -p dirOClient dirOClient/serverSide dirOClient/serverSide/main dirOClient/clientSide dirOClient/clientSide/main dirOClient/clientSide/entities \
         dirOClient/clientSide/stubs dirOClient/infrastructure
cp clientSide/main/ClientOrdinaryThief.class dirOClient/clientSide/main
cp clientSide/entities/oClient.class clientSide/entities/oStates.class dirOClient/clientSide/entities
cp clientSide/stubs/AssaultPartyStub.class clientSide/stubs/ControlCollectionSiteStub.class  clientSide/stubs/ConcentrationSiteStub.class  clientSide/stubs/MuseumStub.class dirOClient/clientSide/stubs
cp infrastructure/*.class dirOClient/infrastructure
cp genclass.jar dirOClient

echo "Compressing execution environments."
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

