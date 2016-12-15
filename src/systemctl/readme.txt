Files:
------------------------------
lab09.service - systemctl service file - needs to be put in /lib/systemd/system/.  After putting in, run:  'init q' as root.  Starting the service will launch screen with the startService.sh file.
startService.sh - script that compiles the java classes and runs the server
stopService.sh - script that kill screen, closing the server
