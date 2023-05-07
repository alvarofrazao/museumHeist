fuser -k 22255/tcp
fuser -k 22256/tcp
fuser -k 22520/tcp
fuser -k 22258/tcp
fuser -k 22259/tcp
fuser -k 22250/tcp


xterm -T "GeneralRepos" -hold -e "cd dirGenRepos; java -cp .:genclass.jar serverSide.main.ServerGeneralRepos" &
sleep 1
xterm -T "AP1" -hold -e "cd dirAP; java -cp .:genclass.jar serverSide.main.ServerAssaultParty 0" &
sleep 1
xterm -T "AP2" -hold -e "cd dirAP; java -cp .:genclass.jar serverSide.main.ServerAssaultParty 1" &
sleep 1
xterm -T "CollectionSite" -hold -e "cd dirCCL; java -cp .:genclass.jar serverSide.main.ServerCollectionSite" &
sleep 1
xterm -T "ConcentrationSite" -hold -e "cd dirCCS; java -cp .:genclass.jar serverSide.main.ServerConcentrationSite" &
sleep 1
xterm -T "Museum" -hold -e "cd dirMuseum; java -cp .:genclass.jar serverSide.main.ServerMuseum" &
sleep 1

xterm -T "mClient" -hold -e "cd dirMClient; java -cp .:genclass.jar clientSide.main.ClientMasterThief" &
sleep 2
xterm -T "oClient" -hold -e "cd dirOClient; java -cp .:genclass.jar clientSide.main.ClientOrdinaryThief" &