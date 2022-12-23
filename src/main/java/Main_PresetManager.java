import java.io.File;
import java.io.FileNotFoundException;

public class Main_PresetManager {

    public static void main(String[] args) throws FileNotFoundException {
            File presetFile = new File("/Users/jenniferstibbins/PubgAPI-Java-December2022/src/main/resources/Preset_Example2");
            if(presetFile.exists())
            {
                System.out.println("preset file exists");
            }
            else
            {
                System.out.println("preset file does NOT yet exist");
            }
            Preset preset = new Preset(presetFile);
    }
}

//Read,