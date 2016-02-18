echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 PUTS"
echo "---------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.PUT APPLE ***********************"
java -jar -DclientId=1 -DserverChoice=1 RMIClient.jar PUT Apple Color is Red
echo "*******************  2.PUT BANANA *************************"
java -jar -DclientId=1 -DserverChoice=2 RMIClient.jar PUT Banana Color is Yellow
echo "*******************  3.PUT Orange ***********************"
java -jar -DclientId=1 -DserverChoice=3 RMIClient.jar PUT Orange Color is Orange
echo "******************   4.PUT KIWI  *************************"
java -jar -DclientId=1 -DserverChoice=4 RMIClient.jar PUT Kiwi Color is Green
echo "*******************  5.PUT PINEAPPLE *************************"
java -jar -DclientId=1 -DserverChoice=5 RMIClient.jar PUT Pineapple Color is Yellowish-Green
echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 GETS"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.GET APPLE ***********************"
java -jar -DclientId=1 -DserverChoice=5 RMIClient.jar GET Apple
echo "*******************  2.GET BANANA *************************"
java -jar -DclientId=1 -DserverChoice=2 RMIClient.jar GET Banana
echo "*******************  3.GET Orange ***********************"
java -jar -DclientId=1 -DserverChoice=3 RMIClient.jar GET Orange
echo "******************   4.GET KIWI  *************************"
java -jar -DclientId=1 -DserverChoice=1 RMIClient.jar GET Kiwi
echo "*******************  5.GET PINEAPPLE *************************"
java -jar -DclientId=1 -DserverChoice=4 RMIClient.jar GET Pineapple
echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 DELETES"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.DELETE APPLE ***********************"
java -jar -DclientId=1 -DserverChoice=1 RMIClient.jar DELETE Apple
echo "*******************  2.DELETE BANANA *************************"
java -jar -DclientId=1 -DserverChoice=3 RMIClient.jar DELETE Banana
echo "*******************  3.DELETE Orange ***********************"
java -jar -DclientId=1 -DserverChoice=4 RMIClient.jar DELETE Orange
echo "******************   4.DELETE KIWI  *************************"
java -jar -DclientId=1 -DserverChoice=2 RMIClient.jar DELETE Kiwi
echo "*******************  5.DELETE PINEAPPLE *************************"
java -jar -DclientId=1 -DserverChoice=5 RMIClient.jar DELETE Pineapple

echo "---------------------------------------------------------------------------------------------------------------------------"
echo "5 GETS TO TEST DELETE"
echo "-------------------------------------------------------------------------------------------------------------------------"
echo "*******************  1.GET APPLE ***********************"
java -jar -DclientId=1 -DserverChoice=5 RMIClient.jar GET Apple
echo "*******************  2.GET BANANA *************************"
java -jar -DclientId=1 -DserverChoice=1 RMIClient.jar GET Banana
echo "*******************  3.GET Orange ***********************"
java -jar -DclientId=1 -DserverChoice=2 RMIClient.jar GET Orange
echo "******************   4.GET KIWI  *************************"
java -jar -DclientId=1 -DserverChoice=3 RMIClient.jar GET Kiwi
echo "*******************  5.GET PINEAPPLE *************************"
java -jar -DclientId=1 -DserverChoice=4 RMIClient.jar GET Pineapple
echo ""
echo "//////// end of SIMULATION /////////////////////"