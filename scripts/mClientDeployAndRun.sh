echo "Transfering data to the master thief node."
sshpass -f password ssh sd206@l040101-ws08.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws08.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password scp dirMClient.zip sd206@l040101-ws08.ua.pt:test/museumHeist
echo "Decompressing data sent to the master thief node."
sshpass -f password ssh sd206@l040101-ws08.ua.pt 'cd test/museumHeist ; unzip -uq dirMClient.zip'
echo "Executing program at the master thief node."
sshpass -f password ssh sd206@l040101-ws08.ua.pt 'cd test/museumHeist/dirMClient ; ./mclient_com_d.sh'