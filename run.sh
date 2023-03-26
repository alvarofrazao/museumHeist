for i in $(seq 1 100)
do
echo -e "\nRun n.o " $i
/usr/bin/env /usr/lib/jvm/java-8-openjdk-amd64/bin/java -cp /tmp/cp_26hmiihzg2vgo0iu2zx5jzkel.jar src.main.Main
done