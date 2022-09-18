public class Player_V2 {
    private String name;
    private String accountID;

    public Player_V2(String playerName, String id)
    {
        this.name = playerName;
        this.accountID = id;
    }

    public String getPlayerName()
    {
        return this.name;
    }
    public String getPlayerAccountID()
    {
        return this.accountID;
    }

    //name
    //id
    //team?
    //killCounts
    //ranking
    //etc.
    //diff functionalities
}
