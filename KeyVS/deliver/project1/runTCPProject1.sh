echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 PUTS"
echo "---------------------------------------------------------------------------------------------------------------------------"
java -jar TCPServer.jar 1234 &
# Send it to the background
# Record PID
echo "*******************  1.PUT APPLE ***********************"
java -jar TCPClient.jar localhost 1234 PUT Apple Color is Red
echo "*******************  2.PUT BANANA *************************"
java -jar TCPClient.jar localhost 1234 PUT Banana Color is Yellow
echo "*******************  3.PUT Orange ***********************"
java -jar TCPClient.jar localhost 1234 PUT Orange Color is Orange
echo "******************   4.PUT KIWI  *************************"
java -jar TCPClient.jar localhost 1234 PUT Kiwi Color is Green
echo "*******************  5.PUT PINEAPPLE *************************"
java -jar TCPClient.jar localhost 1234 PUT Pineapple Color is Yellowish-Green
echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 GETS"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.GET APPLE ***********************"
java -jar TCPClient.jar localhost 1234 GET Apple
echo "*******************  2.GET BANANA *************************"
java -jar TCPClient.jar localhost 1234 GET Banana
echo "*******************  3.GET Orange ***********************"
java -jar TCPClient.jar localhost 1234 GET Orange
echo "******************   4.GET KIWI  *************************"
java -jar TCPClient.jar localhost 1234 GET Kiwi
echo "*******************  5.GET PINEAPPLE *************************"
java -jar TCPClient.jar localhost 1234 GET Pineapple
echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 DELETES"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.DELETE APPLE ***********************"
java -jar TCPClient.jar localhost 1234 DELETE Apple
echo "*******************  2.DELETE BANANA *************************"
java -jar TCPClient.jar localhost 1234 DELETE Banana
echo "*******************  3.DELETE Orange ***********************"
java -jar TCPClient.jar localhost 1234 DELETE Orange
echo "******************   4.DELETE KIWI  *************************"
java -jar TCPClient.jar localhost 1234 DELETE Kiwi
echo "*******************  5.DELETE PINEAPPLE *************************"
java -jar TCPClient.jar localhost 1234 DELETE Pineapple

echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 GETS TO TEST DELETE"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.GET APPLE ***********************"
java -jar TCPClient.jar localhost 1234 GET Apple
echo "*******************  2.GET BANANA *************************"
java -jar TCPClient.jar localhost 1234 GET Banana
echo "*******************  3.GET Orange ***********************"
java -jar TCPClient.jar localhost 1234 GET Orange
echo "******************   4.GET KIWI  *************************"
java -jar TCPClient.jar localhost 1234 GET Kiwi
echo "*******************  5.GET PINEAPPLE *************************"
java -jar TCPClient.jar localhost 1234 GET Pineapple
echo ""
echo "//////// end of SIMULATION /////////////////////"
lsof -i tcp:1234
echo "Type kill -9 PID_LISTED_ABOVE to kill the TCP Server"


