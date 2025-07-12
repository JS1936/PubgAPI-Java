import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
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
            Vector<String> botNames = new Vector<String>(); //ai.3 (not guards)
            boolean gameHasStarted = false;

            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                
                if(data.contains("LogMatchEnd"))
                {
                    gameHasStarted = false;
                }
                //Only start counting bots and people IF the game has started (people can enter and leave beforehand)
                if (data.contains("LogMatchStart")) {
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
                        if (account_id.contains(","))
                        {
                            account_id = account_id.substring(0, account_id.length() - 1);
                        }

                        //If new player, add to list of players (playerNames)
                        if (!playerNames.contains(account_id)) //new player
                        {
                            playerNames.add(account_id);
                        }
                    } else //bot
                    {
                        if (data.contains("ai.3"))
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
            String text = "#bots:       " + botNames.size() + " / " + (playerNames.size() + botNames.size())
                + " [" + prettyFile.getAbsolutePath() + "]";
            FileManager.writeToFileAndConsole(text);
            scan.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred: file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("An error occurred: unable to write string to file");
            e.printStackTrace();
        }
    }
}