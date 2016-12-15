This application will run on cs262.cs.calvin.edu, which is configured to run your information servers. All teams will use this same Postgres installation. You can access the server from the Lab using Linux ssh and scp.

- To initialize the database for your team, do the following. See the sample code in Lab09/src/database/.
    % su postgres
         ...enter our postgres password here...
    postgres% createdb UNIQUE_DB_NAME
         ...choose a unique database name for your team (e.g., cs262aFitness)...
    postgres% psql UNIQUE_DB_NAME < service/database/YOUR_DB_COMMAND_FILE.sql
         ...this command file shouldn't include queries...
    postgres% psql UNIQUE_DB_NAME
         ...try out some SQL queries here...
    postgres% exit

- To configure and run this application as a RESTful web service do the following.
    1. Load your (full) webservice application into /var/cs262/PROJECT_NAME. (use a unique name, e.g., cs262aFitness)
    2. Edit your application (see MainResource#main()) to use the appropriate DB_URI and PORT for your team.
       The PORT for each team is:
            A - 8081
            B - 8082
            C - 8083
            D - 8084
            E - 8085
            F - 8086
            G - 8087
    3. Copy the sample scripts from the Lab09 repo (Lab09/src/manual/*) into your application and edit them as follows.
        a. env.sh - Set a unique name and path.
        b. start-java.sh - Edit the commands at the bottom to compile and run your application.
    4. Execute start.sh.
  This will start your service on the machine. It will run until you execute the stop.sh script.
  Note: a single user can only run one instance of this startup script with the same Name.

When configured and running, your webservice should respond to URLs similar to the active one you used in homework 2:

    http://cs262.cs.calvin.edu:8089/monopoly/players
        ...set the port and path appropriately...


