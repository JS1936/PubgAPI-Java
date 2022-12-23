import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

public class Preset {
    private char preset_id;
    private String player_name;
    private Vector<File> files = new Vector<>();
    private Vector<Integer> actions = new Vector<>();
    private File directoryFolder; //could add the files to here, so then main can use it...
    //what if you could make a "summary_matchList.json" from it?

    //char VS string vs file vs int


    private boolean initializePreset(File presetFile) throws FileNotFoundException {

        //Check if presetFile is a file
        if (!presetFile.isFile()) {
            System.out.println("Error: given presetFile is not a file");
            System.exit(0);
        }

        //If presetFile is a file, read through the file
        Scanner input = new Scanner(presetFile);
        //int lineNumber = 0;
        //int type = 0; // 0 means preset, 1 means player_name, 2 means active_file, 3 means action)(?)
        while(input.hasNextLine())
        {
            String text = input.nextLine();
            System.out.println("Current text: " + text);
            int indexOfSeparator = text.indexOf(":");
            String desiredText = text.substring(0, indexOfSeparator);


            //desiredText.
            System.out.println("Desired text: " + desiredText);
            if(text.contains("PRESET #"))
            {
                if(desiredText.length() != 1)
                {
                    System.out.println("Error: preset_ID should be one character");
                }
                else
                {
                    setPreset_Id(desiredText.charAt(0));
                }
            }
            else if(text.contains("PLAYER_NAME")) {
                setPlayer_Name(desiredText);
            }
            else if(text.contains("ACTIVE_FILE"))
            {
                File file = new File(desiredText);
                if(file.exists())
                {
                    System.out.println("File exists");
                    addToChosenFiles(file);
                }
                else
                {
                    System.out.println("Error: file: " + file.toString() + " does not yet exist");
                }

            }
            else if(text.contains("ACTION"))
            {
                int desiredNum = Integer.valueOf(desiredText);
                addToChosenActions(desiredNum);
            } else
            {
                System.out.println("Error: " + text + " does not follow format for preset intake");
            }
        }
        return true; //TODO: add input validation + error message if input is invalid
    }
    public Preset(File presetFile) throws FileNotFoundException {

        initializePreset(presetFile);
        printPresetSummary();
        //doPreset();


    }

    //how does the thing need to receive it?
    //Note: convert vector to directory?
    public void doPreset()
    {
        //
    }

    private void setPreset_Id(char ch)
    {
        System.out.println("Attempting to set preset_id...");
        this.preset_id = ch;
        System.out.println("preset_id = " + preset_id);
    }
    private void setPlayer_Name(String player_name)
    {
        this.player_name = player_name;
    }
    private void addToChosenFiles(File file)
    {
        if(!files.contains(file))
        {
            files.add(file); //not okay to have duplicate files
        }
    }
    private void addToChosenActions(int action)
    {
        actions.add(action); //okay to have duplicate actions
    }
    //Assuming: 1-6 are the only valid int options right now (for ACTION). Length = 1
    //Assuming: preset # has a letter in it.
    //Assuming: file name has "telemetry" in it.
    //Assuming: player name has ...?
    public void getInputType()
    {

    }

    public int get_preset_id() {
        return this.preset_id;
    }

    public String getPlayerName()
    {
        return this.player_name;
    }
    public Vector<File> files()
    {
        return this.files;
    }
    public Vector<Integer> getActions()
    {
        return this.actions;
    }

    public void printPresetSummary()
    {
        System.out.println("PRESET SUMMARY ---");
        System.out.println("---preset_id       = " + this.preset_id);
        System.out.println("---player_name     = " + this.player_name);
        for(File f : this.files)
        {
            System.out.println("---ACTIVE FILE  : " + f.toString());
        }
        for(int action : this.actions)
        {
            System.out.println("---ACTION      : " + action);
        }
    }
    //add getter for getCurrentAction?
}
