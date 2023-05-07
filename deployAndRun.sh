echo "Performing Code Compilation"
sh ./buildAndGenerate.sh

xterm  -T "General Repository" -hold -e "./GeneralReposDeployAndRun.sh" &
sleep 5
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
xterm  -T "CollectionSite" -hold -e "./CollectionSiteDeployAndRun.sh" &
xterm  -T "ConcentrationSite" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
xterm  -T "AssaultParty(0)" -hold -e "./AssaultParty1DeployAndRun.sh" &
xterm  -T "AssaultParty(1)" -hold -e "./AssaultParty2DeployAndRun.sh" &
sleep 5
xterm  -T "Master" -hold -e "./mClientDeployAndRun.sh" &
xterm  -T "Ordinary" -hold -e "./oClientDeployAndRun.sh" &
