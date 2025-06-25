import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

/*
 * The BotCounts class counts the number of non-player-characters in a match ("bots")
 * and the number of player characters ("players").
 */
public class BotCounts {

    /*
     * Reads from a file to determine how many "people" in a pubg match were actually bots.
     * The file includes many, many details about the match.
     * IS A MANUAL VERSION: Does not use JSONObjects. Instead, scans line by line.
     * NOTE: does not count guards as bots.
     */
    public static void countBotsAndPeople(File prettyFile) {
        try {
            Scanner scan = new Scanner(prettyFile);

            Vector<String> playerNames = new Vector<String>(); //account.
            Vector<String> botNames = new Vector<String>(); //ai.

            boolean gameHasStarted = false;

            int countLogPlayerKillV2 = 0; //part of checker
            int countKillCounts = 0; //part of checker
            //int countRevivals = 0; // part of checker
            while (scan.hasNextLine()) {
                String data = scan.nextLine();

                //Remove 06/21/25:
                /*
                if(data.contains("arcade"))
                {
                    System.out.println("ARCADE: Zero bots");
                }
                */
                if(data.contains("LogMatchEnd"))
                {
                    System.out.println("----END OF MATCH=====\n");
                    //break;
                    gameHasStarted = false; //imperfect
                }
                //Only start counting bots and people IF the game has started (people can enter and leave beforehand)
                if (data.contains("LogMatchStart")) { //changecFrom numStartPlayer to LogMatchStart
                    gameHasStarted = true; //could change this to game in progress (then make it false once logmatch end it found...)
                }
                //Checker... (temporary)

                //if(data.contains("numAlivePlayers") && gameHasStarted)
                //{
                //    System.out.println(data);
                //}
                //if(data.contains("LogPlayerKillV2") && gameHasStarted)
                //{
                //    countLogPlayerKillV2++;
                //    System.out.println(data);
                //    System.out.println("countLogPlayerKillV2 = " + countLogPlayerKillV2);
                //}
                if(data.contains("killCount") && gameHasStarted)
                {
                  //System.out.println("killCount data = " + data); //Temporarily remove 06/21/25
                    countKillCounts++;
                 //   System.out.println("countKillCounts =      " + countKillCounts);
                }
                //Temporary:
                //if(data.contains("numParticipatedPlayers"))
                //{
                //    System.out.println(data);
                //}

                //A "revival" is when a player resurrects a downed / down-but-not-out (DBNO) teammate.
                //Comment out LogPlayerRevive count 06/22/25.
                /*
                if(data.contains("LogPlayerRevive") && gameHasStarted)
                {
                    countRevivals++;
                    //System.out.println(data + " (revival #" + countRevivals); //Temporarily remove 06/21/25
                }
                */
                //System.out.println("countKillCounts =      " + countKillCounts);
                //System.out.println("countLogPlayerKillV2 = " + countLogPlayerKillV2);
                //End of temporary checker

                //Account found, game has started
                if (data.contains("accountId") && gameHasStarted) {
                    if (data.contains("account.")) //real person
                    {
                        //Store the account_id
                        int accountStart = data.indexOf("account.");
                        String account_id = data.substring(accountStart);

                        //Store it "pretty"/nicely
                        if (account_id.contains(",")) {
                            account_id = account_id.substring(0, account_id.length() - 1);
                        }

                        //If new player, add to list of players (playerNames)
                        if (!playerNames.contains(account_id)) //new player
                        {
                            playerNames.add(account_id);
                        }
                    } else //bot (or guard, potentially)
                    {
                        if (data.contains("ai.3")) //bot //<-- change to ai.3 to identify user_ai, not guards
                        {
                            //Store account_id
                            int accountStart = data.indexOf("ai.3");
                            String account_id = data.substring(accountStart, accountStart + 6);

                            //If new bot, add to list of bots (botNames).
                            if (!botNames.contains(account_id)) //new bot
                            {
                                botNames.add(account_id);
                            }
                        }
                    }
                }

            }
            String text = "#bots:       " + botNames.size() + " / " + (playerNames.size() + botNames.size());
            //System.out.println("#revivals = " + countRevivals); //temp
            System.out.println("#killCounts = " + countKillCounts); //temp
            FileManager.writeToFileAndConsole(text);
            FileManager.writeToFileAndConsole("#players:        " + playerNames.size());


            //Temporary:
            //System.out.println("Players:" + playerNames);
            //System.out.println("Bots   :" + botNames);
            

            


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred: unable to write string to file");
            e.printStackTrace();
        }
        //System.exit(0); //temporary. REMOVE LATER
    }
   
}

//Potential future updates:
//0: Adjust depending on request scope (individual, team, match) -> minor changes, if any
//1: Include match_id when printing #bots
//2: Read from requestHistory file -> calculate ___ based off of what was just stored in there
//      (like total percentage of bots across a group of games)
