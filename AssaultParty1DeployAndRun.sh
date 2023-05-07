echo "Transfering data to the assault party 1 node."
sshpass -f password ssh sd206@l040101-ws01.ua.pt 'fuser -k 22255/tcp'
sshpass -f password ssh sd206@l040101-ws01.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws01.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password scp dirAP.zip sd206@l040101-ws01.ua.pt:test/museumHeist
echo "Decompressing data sent to the assault party node."
sshpass -f password ssh sd206@l040101-ws01.ua.pt 'cd test/museumHeist ; unzip -uq dirAP.zip'
echo "Executing program at the assault party node."
sshpass -f password ssh sd206@l040101-ws01.ua.pt 'cd test/museumHeist/dirAP ; java -cp .:genclass.jar serverSide.main.ServerAssaultParty 0'
