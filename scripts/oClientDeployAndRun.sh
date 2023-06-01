echo "Transfering data to the ordinary thieves node."
sshpass -f password ssh sd206@l040101-ws09.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws09.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password scp dirOClient.zip sd206@l040101-ws09.ua.pt:test/museumHeist
echo "Decompressing data sent to the ordinary thieves node."
sshpass -f password ssh sd206@l040101-ws09.ua.pt 'cd test/museumHeist ; unzip -uq dirOClient.zip'
echo "Executing program at the ordinary thieves node."
sshpass -f password ssh sd206@l040101-ws09.ua.pt 'cd test/museumHeist/dirOClient ; ./oclient_com_d.sh'
