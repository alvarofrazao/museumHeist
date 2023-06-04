echo "Transfering data to the collection site node."
sshpass -f password ssh sd206@l040101-ws04.ua.pt 'fuser -k 22258/tcp'

sshpass -f password ssh sd206@l040101-ws04.ua.pt 'mkdir -p test/museumHeist'
sshpass -f password ssh sd206@l040101-ws04.ua.pt 'rm -rf test/museumHeist/*'
sshpass -f password scp dirCCL.zip sd206@l040101-ws04.ua.pt:test/museumHeist
echo "Decompressing data sent to the collection site node."
sshpass -f password ssh sd206@l040101-ws04.ua.pt 'cd test/museumHeist ; unzip -uq dirCCL.zip'
echo "Executing program at the collection site node."
sshpass -f password ssh sd206@l040101-ws04.ua.pt 'cd test/museumHeist/dirCCL ; ./ccl_d.sh'
