echo "Transfering data to the general repository node."
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password scp dirGeneralRepos.zip sd206@l040101-ws03.ua.pt:test/museumHeist
echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'cd test/museumHeist ; unzip -uq dirGeneralRepos.zip'
echo "Executing program at the general repository node."
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'cd test/museumHeist/dirGeneralRepos ; ./repos_com_d.sh sd206'
echo "Server shutdown."
sshpass -f password ssh sd206@l040101-ws03.ua.pt 'cd test/museumHeist/dirGeneralRepos ; less stat'
echo "Transferring logfile to local machine"
sshpass -f password scp sd206@l040101-ws03.ua.pt:test/museumHeist/dirGenRepos/logfile.txt .
echo "Transfer complete"