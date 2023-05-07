echo "Transfering data to the general repository node."
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'fuser -k 22250/tcp'
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password scp dirGenRepos.zip sd206@l040101-ws03.ua.pt:test/museumHeist
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'cd test/museumHeist ; unzip -uq dirGenRepos.zip'
echo "Executing program at the server general repository."
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'cd test/museumHeist/dirGenRepos ; java -cp .:genclass.jar serverSide.main.ServerGeneralRepos'
echo "Server shutdown."
echo "Transferring logfile to local machine"
sshpass -f password scp sd206@l040101-ws03.ua.pt:test/museumHeist/dirGenRepos/logfile.txt .