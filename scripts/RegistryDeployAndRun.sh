echo "Transfering data to the registry node."
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password scp dirRegistry.zip sd206@l040101-ws10.ua.pt:test/museumHeist
echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'cd test/museumHeist ; unzip -uq dirRegistry.zip'
echo "Executing program at the registry node."
sshpass -f password ssh sd206@l040101-ws10.ua.pt 'cd test/museumHeist/dirRegistry ; ./registry_com_d.sh sd206'
