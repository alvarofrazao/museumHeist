echo "Transfering data to the museum node."
sshpass -f password ssh sd206@l040101-ws05.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws05.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password scp dirMuseum.zip sd206@l040101-ws05.ua.pt:test/museumHeist
echo "Decompressing data sent to the museum node."
sshpass -f password ssh sd206@l040101-ws05.ua.pt 'cd test/museumHeist ; unzip -uq dirMuseum.zip'
echo "Executing program at the museum node."
sshpass -f password ssh sd206@l040101-ws05.ua.pt 'cd test/museumHeist/dirMuseum ; ./museum_com_d.sh ruibsd206'
