# Yakboks! Server
Welcome to Yakboks! the game! In this game, you need to come up with the funniest, cleverest and most creative answers, to random prompts that you and your friends are given. One of you will become the judge. The judge then have to decide which of them are the best overall, and the chosen winner will be awarded points for their work of art! The judge role will be given to another player after each round, so think of what answer would make that person laugh. Don't worry! The judge does not know who answered what, until they have chosen the best answer.  After everyone has been the judge, the game will end, and the person with the most points will be the winner!

### Introduction
This is the server side for the game, which is required to be installed and running in order to make for the game to work. When the server is running, players/clients are able to join it and then play the game. The server will be running on the PC in the background with a given IP-address that the players connect to.

The server contains multi-threadding and networking on a basic level and the communication protocol is TCP, as the program requires an established connection between server and client in order to transmit data. 

The client-side of the game can be found here: https://github.com/LukasGadeMEA20/Group302-PCSS-client

The server side is developed by August V. Clarkfeldt, Bjørn Troldahl, Christopher Reimers, Daniel S. Rossing, Lukas G. Ravnsborg, Mikkel S. Lauridsen

### How to install and use
Step 1: Install [IntelliJ IDEA](https://www.jetbrains.com/idea/download/#section=windows) and follow the install instructions.

Step 2: Download this GitHub repository as a ZIP-file under the Code tab and unzip it into a folder.

Step 3: In IntelliJ IDEA, press File > Open... and locate the folder and click OK.

Step 4: Locate ```Main``` under src > com.company > ... and run it.

Step 5: When the server is running, it will print a line of text in the console which includes the IP-address, the port and what time it started running.  
Example: Server IP: ```123.45.678.90``` Port: ```8000```

Step 6: Write down the IP-address and port. These are used for the clients to connect to the server and join the game.

Step 7: Play the game! To join the server, go to the [client-side GitHub page](https://github.com/LukasGadeMEA20/Group302-PCSS-client) and follow the instructions there.

### Future work - for both Client and Server
The program could really use a lot of code clean up and some optimization different places. The server also does not handle a user exiting the program while connected to the server very well, and will tank your computer quite a lot if you do so, so I would recommend not doing that. If you were to do close the program while connected to the server, you will have to close and open the server. We tried finding out where that problem could be solved, but the only solution we could come up with, was creating a try-catch around every to/fromClient, with it catching the exception "SocketException". We did not know if that was the way to go, which it probably was, but that could be a good spot to start looking at.
