Files:
-----------------
env.sh - sets environment variables such as project name, and base directory. 
  This should not be called directly, but is sourced from all the other scripts.
start.sh - spins up screen and executes start-java.sh .
start-java.sh - does the actual work of compiling java classes and starting 
  the server.  This should not be called directly, but is called from start.sh.
stop.sh - shuts down screen, closing the server.
