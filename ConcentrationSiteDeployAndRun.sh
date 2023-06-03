echo "Transfering data to the concentration site node."
sshpass -f password ssh sd206@l040101-ws07.ua.pt 'fuser -k 22251/tcp'
sshpass -f password ssh sd206@l040101-ws07.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws07.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password scp dirCCS.zip sd206@l040101-ws07.ua.pt:test/museumHeist
echo "Decompressing data sent to the concentration site node."
sshpass -f password ssh sd206@l040101-ws07.ua.pt 'cd test/museumHeist ; unzip -uq dirCCS.zip'
echo "Executing program at the concentration site node."
sshpass -f password ssh sd206@l040101-ws07.ua.pt 'cd test/museumHeist/dirCCS ; ./ccs_d.sh'
