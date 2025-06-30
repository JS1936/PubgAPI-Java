## Pubg-API-Java ##
In-progress. Actively updated

## Purpose ## 
This project started because I wanted to know how many bots were in the pubg games I played. Upon seeing what the telemetry data looked like, I realized there were a lot of other statistics I could learn about, too. 

## Functionalities ##
As of October 2022, the Pubg-API-Java project has the following primary functionalities:

| WHAT    |   HOW   | WHERE | EXAMPLE OUTPUT |
|-------- | ------- | ----- | -------------- |
|0: countBotsAndPeople | Manually | BotCounts.java | [Screenshot](https://github.com/JS1936/PubgAPI-Java/blob/work2/documentation/samples/screenshot_countBotsAndPeople_example.png) |
|1: calculateKillCounts	| Manually | KillCounts.java | [Screenshot](https://github.com/JS1936/PubgAPI-Java/blob/work2/screenshot_killCounts.png)|
|2: printPlayersByTeam | JSON | MatchManager.java | [Screenshot](https://github.com/JS1936/PubgAPI-Java/blob/work2/Screenshot_PubgAPI-Java_printPlayersByTeam(still%20need%20to%20update%20maximum%20team%20capacity).png) |
|3: winnerWeapons |	JSON | MatchManager.java | [Screenshot](https://github.com/JS1936/PubgAPI-Java/blob/work2/screenshot_winnerWeapons_example.png) |
|4: ranking (of a specific person)|	JSON | Ranking.java | [Screenshot](https://github.com/JS1936/PubgAPI-Java/blob/work2/Screenshot_PubgAPI-Java_ranking.png) |
|5: calculateKillCountsJSON	|	JSON | KillCountsJSON.java | [Screenshot](https://github.com/JS1936/PubgAPI-Java/blob/work2/Screenshot_PubgAPI-Java_partialOutputForKillCountsJSON.png) |
|6: printMapsPlayed | JSON | MapManager.java, MatchManager.java| [Screenshot](https://github.com/JS1936/PubgAPI-Java/blob/work2/screenshot_printMapsPlayed_example.png) |

### Known Issues ###
* 0: None
* 1: "killCounts" - ambiguity in API's definition
* 2: Need to update calculation of team size
* 3: None
* 4: None
* 5: "killCounts" - ambiguity in API's definition
* 6: None

## User's Guide ##
* [How do I make an API request?](https://github.com/JS1936/PubgAPI-Java/files/10244822/Q_.How.do.I.make.an.API.request_.pdf)
* [What file(s) should I modify?](https://github.com/JS1936/PubgAPI-Java/files/10245164/Q_.What.file.s.should.I.modify.pdf)
* [What does it mean when a file is “ugly”, “pretty”, or becomes “prettified”?](https://github.com/JS1936/PubgAPI-Java/files/10245177/PubgAPI-Java.Q.pdf)


## Classes Breakdown ##

### Acquire Data ###
* API.java //getAPIkey(), getAPIplatform()
* API_Request.java //getPlayer(), getConnection(), getTimestamp(), connectToAPI(URL url), storeResponseToSpecifiedFileLocation(String dstPath)
* Main_API.java //main. As of 17 Nov 2022, program runs from Main_API.java.

### Managers ###
* FileManager.java //makePretty, storeFileAsString, getFile, writeToFileAndConsole
* JSONManager.java //getJSONObject, returnObject, returnMultipleObjects
* MapManager.java //printMapNames (and getMapName, dashed out because present in MatchManager.java)
* MatchManager.java //printMatchInfo, getMatchID, getPlayerPerspective, getMatchType, 

### Calculate Statistics ###
* BotCounts.java //countBotsAndPeople(File prettyFile) ![img.png](screenshot_countBotsAndPeople_example.png)
* KillCounts.java //printKillCountsToHistoryAndConsole, printKillCounts, calculateKillCounts
* KillCountsJSON.java //printKillCountsJSON, calculateKillCountsJSON
* Main.java //main, pseudoMain, getInfo, printOptionsToChooseFrom, initiateFunctionalities, getInput, getRequestType, getTeamSizeForOfficialMatch, weaponFrequencies, winnerWeapons, printPlayersByTeam, getMapName
* Ranking.java //ranking
* MapManager.java and MatchManager.java //printMapNames; MatchManager methods being reworked

![img_1.png](screenshot_printMapsPlayed_example.png)


### Inefficient ###
* Request.java //getRequest, getTypes, getScopes, getRequest_type, getRequest_scope

## [Updates](https://github.com/JS1936/PubgAPI-Java/blob/work2/UpdatesRecord.md)

## Areas for Improvement ##
* Efficient memory usage/storage
* Readability / simplicity
    * Removal of unneeded code and/or comments
    * Clarity with naming
    * Brevity / clarity of commit messages
    * Inclusion of dates when making updates
    * Consistent organization of classes
* API request process
    * Ease of API request --> project request transference (currently is unclear and likely labor-intensive for outside viewer)
    * Current state of API requests is very limited in how the request can vary
* Comprehensiveness and speed of testcases
* Inclusion of more example files (with explanations for how to install/run the program, step-by-step)
* Inclusion of sample description of end-user personas


