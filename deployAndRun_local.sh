echo "Performing Code Compilation"
sh ./buildAndGenerate.sh

echo "Starting deployment"

xterm -T "RMI Registry" -hold -e "./RMIRegistryDeployAndRun.sh" &

sleep 15

xterm -T "Register Object" -hold -e "./RegistryDeployAndRun.sh" &

sleep 10

xterm  -T "General Repository" -hold -e "./GeneralReposDeployAndRun.sh" &
sleep 5
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
xterm  -T "CollectionSite" -hold -e "./CollectionSiteDeployAndRun.sh" &
xterm  -T "ConcentrationSite" -hold -e "./ConcentrationSiteDeployAndRun.sh" &
xterm  -T "AssaultParty(0)" -hold -e "./AssaultParty1DeployAndRun.sh" &
xterm  -T "AssaultParty(1)" -hold -e "./AssaultParty2DeployAndRun.sh" &
sleep 20
xterm  -T "Master" -hold -e "./mClientDeployAndRun.sh" &
xterm  -T "Ordinary" -hold -e "./oClientDeployAndRun.sh" &
