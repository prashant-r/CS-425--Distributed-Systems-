echo "Setting up Server wait for 5 seconds..."
java -jar RMIServer.jar 1234 &
for i in {1..5}; do 
  printf '\r%2d' $i
  sleep 1
done
printf '\n'
java -jar -DclientId=1 RMIClient.jar localhost 1234 PUT Apple Color is Red &
java -jar -DclientId=1 RMIClient.jar localhost 1234 GET Apple 
java -jar -DclientId=3 RMIClient.jar localhost 1234 DELETE Apple &
java -jar -DclientId=2 RMIClient.jar localhost 1234 PUT Pineapple Color is Yellowish-Green &
java -jar -DclientId=1 RMIClient.jar localhost 1234 GET Pineapple &
java -jar -DclientId=3 RMIClient.jar localhost 1234 PUT Apple Reddicious &
java -jar -DclientId=2 RMIClient.jar localhost 1234 DELETE Pineapple &
java -jar -DclientId=1 RMIClient.jar localhost 1234 GET Apple &
java -jar -DclientId=3 RMIClient.jar localhost 1234 GET Pineapple 


