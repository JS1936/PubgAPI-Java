## Pubg-API-Java ##
In-progress. Actively updated

## Purpose ## 
This project started because I wanted to know how many bots were in the pubg games I played. Upon seeing what the telemetry data looked like, I realized there were a lot of other statistics I could learn about, too. 

## Functionalities ##
As of October 2022, the Pubg-API-Java project has the following primary functionalities:

| WHAT      |   HOW   | WHERE |
|---------- | ------- | ----- |
|0: countBotsAndPeople | Manually | BotCounts.java |
|1: calculateKillCounts	| Manually | KillCounts.java |	
|2: printPlayersByTeam | JSON | MatchManager.java |
|3: winnerWeapons |	JSON | MatchManager.java |
|4: ranking (of a specific person)|	JSON | Ranking.java |
|5: calculateKillCountsJSON	|	JSON | KillCountsJSON.java |
|6: printMapsPlayed |	JSON | MapManager.java, MatchManager.java|

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
* BotCounts.java //countBotsAndPeople(File prettyFile) ![img.png](img.png)
* KillCounts.java //printKillCountsToHistoryAndConsole, printKillCounts, calculateKillCounts
* KillCountsJSON.java //printKillCountsJSON, calculateKillCountsJSON
* Main.java //main, pseudoMain, getInfo, printOptionsToChooseFrom, initiateFunctionalities, getInput, getRequestType, getTeamSizeForOfficialMatch, weaponFrequencies, winnerWeapons, printPlayersByTeam, getMapName
* Ranking.java //ranking

### Inefficient ###
* Request.java //getRequest, getTypes, getScopes, getRequest_type, getRequest_scope

## Updates ##

1. Can now store history of searches (via "requestHistory" in Main.cpp)
2. Can make multiple requests per program run
3. Can now access [sample files](https://github.com/JS1936/PubgAPI-Java/tree/work/src/main/resources) showing original and "prettified" versions of a file
4. Resolved [file duplication error](https://github.com/JS1936/PubgAPI-Java/files/9720547/PubgAPI-Java_.Update.4.Details.pdf) in FileManager.java
5. Can now reference [draft1 of User-View](https://github.com/JS1936/PubgAPI-Java/files/9836661/Draft.1_.User-View.Requester.Selector.1.pdf) (Requester && Selector && !Analyzer) | 18 Oct 2022 |
6. Can now reference [draft1 of User-Input Preset](https://github.com/JS1936/PubgAPI-Java/blob/work2/src/main/resources/Draft%201_%20User-Input%20Preset%20(!Requester%20%26%26%20Selector%20%26%26%20Analyzer).pdf) (!Requester, Selector, Analyzer) | 19 Oct 2022 |
7. Can now reference [draft1 of Basic End-User Descriptions](https://github.com/JS1936/PubgAPI-Java/files/9836733/PubgAPI-Java_.Draft1.of.Basic.End-User.Descriptions.pdf) | 21 Oct 2022 |  
-7.1. Can now reference [draft2](https://github.com/JS1936/PubgAPI-Java/blob/work2/src/main/resources/Drafts/PubgAPI-Java_%20Draft2%20of%20Basic%20End-User%20Descriptions.pdf) | 2 Nov 2022 |
8. Can now reference [draft1 of Use Case Diagram](https://github.com/JS1936/PubgAPI-Java/blob/work2/src/main/resources/Drafts/Draft1-%20Use%20Case%20Diagram%20-%20Pubg-API-Java%20Program.pdf) | 28 Oct 2022 |
9. Can now automatically get match telemetries from request to API for player's recent history. Previously: separate events | 17 Nov 2022 | 
10. Resolved merge error. Branch work2 is now properly updated | 17 Nov 2022 |
11. Can now reference [draft1 of Get_Data activity diagram](https://github.com/JS1936/PubgAPI-Java/blob/work2/src/main/resources/Drafts/Draft%201_%20Activity%20Diagram.pdf) | 17 Nov 2022 |
12. Remove file - APIManager.java. Any retained content placed to API_Request.java | 17 Nov 2022 |
13. Remove file - DoRequest.java (was empty) | 18 Nov 2022 |

### Top 3 Upcoming Updates ###

1. Pinpoint sources of memory leaks and resolve the leaks (caution: frequent repetition of program runs can cause computer to freeze as of Oct 3, 2022) 
2. Transfer PubgAPI-Request-Java repository content into APIManager.java and DoRequest.java. Classes breakdown should reflect current state of all files mentioned.
3. Add instructions for how to make various requests (e.g. for specific player(ers) and/or match(es)). This should include instructions for how to operate the various sample match files, which are currently only accessible locally.

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


