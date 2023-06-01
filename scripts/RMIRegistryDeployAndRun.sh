echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp dirRMIRegistry.zip sd206@l040101-ws10.ua.pt:test/museumHeist
echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'cd test/museumHeist ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'cd test/museumHeist/dirRMIRegistry ; cp interfaces/*.class /home/sd206/Public/classes/interfaces ; cp set_rmiregistry_d.sh /home/sd206'
echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd206@l040101-ws10.ua.pt './set_rmiregistry_d.sh sd206 22250'
