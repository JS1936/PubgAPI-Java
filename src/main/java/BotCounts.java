import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Scanner;
import java.util.Vector;

public class BotCounts {

    /*
     * Reads from a file to determine how many "people" in a pubg match were actually bots.
     * The file includes many, many details about the match.
     * IS A MANUAL VERSION: Does not use JSONObjects. Instead, scans line by line.
     * NOTE: does not count guards as bots.
     */
    //Individual, team, and match scope --> results all look the same for countBotsAndPeople. (Right?) -> At least, for now.
    public static void countBotsAndPeople(File prettyFile) { //make private?
        try {
            Scanner scan = new Scanner(prettyFile);
            Vector<String> playerNames = new Vector<String>(); //account.
            Vector<String> botNames = new Vector<String>(); //ai.

            boolean gameHasStarted = false;

            while (scan.hasNextLine()) {
                String data = scan.nextLine();

                //Only start counting bots and people IF the game has started (people can enter and leave beforehand)
                if (data.contains("numStartPlayers")) {
                    gameHasStarted = true;
                }

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
                        if (data.contains("ai.")) //bot
                        {
                            //Store account_id
                            int accountStart = data.indexOf("ai.");
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
            String text = "#bots:       " + botNames.size() + " / " + playerNames.size();
            //added 9/17 //To-do: add match_id to this?
            FileManager.writeToFileAndConsole(text);
            //Note: If you then read from the requestHistory file, would you be able to calculate things based off of what was just now stored in there?


        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred: unable to write string to file");
            e.printStackTrace();
        }
    }

}
