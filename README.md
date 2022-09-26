## Pubg-API-Java ##

## Purpose ## 
This project started because I wanted to know how many bots were in the pubg games I played. Upon seeing what the telemetry data looked like, I realized there were a lot of other statistics I could learn about, too. 

## Functionalities ##
Currently, the Pubg-API-Java project has the following primary functionalities:

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
* APIManager.java  //empty
* BotCounts.java //countBotsAndPeople(File prettyFile) --> end
* DoRequest.java //empty
* FileManager.java //makePretty, storeFileAsString, getFile, writeToFileAndConsole
* JSONManager.java //getJSONObject, returnObject, returnMultipleObjects
* KillCounts.java //printKillCountsToHistoryAndConsole, printKillCounts, calculateKillCounts
* KillCountsJSON.java //printKillCountsJSON, calculateKillCountsJSON
* Main.java //main, pseudoMain, getInfo, printOptionsToChooseFrom, initiateFunctionalities, getInput, getRequestType
* MapManager.java //printMapNames (and getMapName, dashed out because present in MatchManager.java)
* MatchManager.java //printMatchInfo, getMatchID, getPlayerPerspective, getMatchType, getTeamSizeForOfficialMatch, weaponFrequencies, winnerWeapons, printPlayersByTeam, getMapName
* Ranking.java //ranking
* Request.java //getRequest, getTypes, getScopes, getRequest_type, getRequest_scope

## Updates ##

1. Can now store history of searches

## Areas for Improvement ##
* Efficient memory usage/storage
* Consistent organization of classes
* Readability / simplicity
* Clarity with naming
* Removal of unneeded code and/or comments
* Ease of API request --> project request transference
