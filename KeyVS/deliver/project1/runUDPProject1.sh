echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 PUTS"
echo "---------------------------------------------------------------------------------------------------------------------------"
java -jar UDPServer.jar 6789 &
# Send it to the background
                        # Record PID
echo "*******************  1.PUT APPLE ***********************"
java -jar UDPClient.jar localhost 6789 PUT Apple Color is Red
echo "*******************  2.PUT BANANA *************************"
java -jar UDPClient.jar localhost 6789 PUT Banana Color is Yellow
echo "*******************  3.PUT Orange ***********************"
java -jar UDPClient.jar localhost 6789 PUT Orange Color is Orange
echo "******************   4.PUT KIWI  *************************"
java -jar UDPClient.jar localhost 6789 PUT Kiwi Color is Green
echo "*******************  5.PUT PINEAPPLE *************************"
java -jar UDPClient.jar localhost 6789 PUT Pineapple Color is Yellowish-Green
echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 GETS"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.GET APPLE ***********************"
java -jar UDPClient.jar localhost 6789 GET Apple 
echo "*******************  2.GET BANANA *************************"
java -jar UDPClient.jar localhost 6789 GET Banana 
echo "*******************  3.GET Orange ***********************"
java -jar UDPClient.jar localhost 6789 GET Orange 
echo "******************   4.GET KIWI  *************************"
java -jar UDPClient.jar localhost 6789 GET Kiwi 
echo "*******************  5.GET PINEAPPLE *************************"
java -jar UDPClient.jar localhost 6789 GET Pineapple 
echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 DELETES"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.DELETE APPLE ***********************"
java -jar UDPClient.jar localhost 6789 DELETE Apple 
echo "*******************  2.DELETE BANANA *************************"
java -jar UDPClient.jar localhost 6789 DELETE Banana 
echo "*******************  3.DELETE Orange ***********************"
java -jar UDPClient.jar localhost 6789 DELETE Orange 
echo "******************   4.DELETE KIWI  *************************"
java -jar UDPClient.jar localhost 6789 DELETE Kiwi 
echo "*******************  5.DELETE PINEAPPLE *************************"
java -jar UDPClient.jar localhost 6789 DELETE Pineapple 

echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 GETS TO TEST DELETE"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.GET APPLE ***********************"
java -jar UDPClient.jar localhost 6789 GET Apple 
echo "*******************  2.GET BANANA *************************"
java -jar UDPClient.jar localhost 6789 GET Banana 
echo "*******************  3.GET Orange ***********************"
java -jar UDPClient.jar localhost 6789 GET Orange 
echo "******************   4.GET KIWI  *************************"
java -jar UDPClient.jar localhost 6789 GET Kiwi 
echo "*******************  5.GET PINEAPPLE *************************"
java -jar UDPClient.jar localhost 6789 GET Pineapple 
echo ""
echo "//////// end of SIMULATION /////////////////////"
lsof -i UDP:6789
echo "Type kill -9 PID_LISTED_ABOVE to kill the UDP Server"







#######################################################################################################################
							#UDP

#######################################################################################################################
