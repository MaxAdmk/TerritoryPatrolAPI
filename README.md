# TerritoryPatrolAPI
TerritoryPatrolAPI is used to store information about security robots, their patrol and the territory they protect
To use this API you should install Postman.
You can use these request:
	GET:localhost:8080/patrol/{id}; localhost:8080/patrol; localhost:8080/robot/{id}; localhost:8080/robot; localhost:8080/territory/{id}; localhost:8080/territory;
	POST: localhost:8080/patrol; localhost:8080/robot; localhost:8080/territory - these request you use to create objects
localhost:8080/robot/load/patrol/information/{idOfRobot}/{idOfPatrolToLoad}; - this request you can use to load patrol information to robot
http://localhost:8080/territory/load/robots/information/{idOfTerritory}/{idOfRobotToLoad}; - this request you can use to load robots information to territory;
localhost:8080/territory/export?folderpath=... - this request you can use to export all Territories objects to CSV files. You have to put folder path in RequestParam
localhost:8080/robot/export?folderpath=... - this request you can use to export all Robots objects to CSV files. You have to put folder path in RequestParam
localhost:8080/patrol/export?folderpath=... - this request you can use to export all Patrols objects to CSV files. You have to put folder path in RequestParam
	DEL: localhost:8080/patrol/{id}; localhost:8080/robot/{id}; localhost:8080/territory/{id};
	PUT: localhost:8080/territory/update/{id}; localhost:8080/robot/update/{id}; localhost:8080/patrol/update/{id};