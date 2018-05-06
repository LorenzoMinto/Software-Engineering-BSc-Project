package it.polimi.se2018.model;

public class Player {

    private int favorTokens;

    private String nickname;

    private WindowPattern windowPattern;

    private User user;

    private PrivateObjectiveCard privateObjectiveCard;

    public Player(User user, String nickname, PrivateObjectiveCard card) {

        //Checks for bad params
        if(user==null){ throw new IllegalArgumentException("Asked to create a player giving null user"); }
        if(windowPattern==null){ throw new IllegalArgumentException("Asked to create a player giving null windowPattern"); }
        if(card==null){ throw new IllegalArgumentException("Asked to create a player giving null card"); }

        this.user = user;

        this.nickname = nickname;

        this.windowPattern = null;
        this.favorTokens = 0;

        this.privateObjectiveCard = card;
    }



    //Getters

    public int getFavorTokens() {
        return favorTokens;
    }

    public String getNickname() {
        return nickname;
    }



    //Setters

    //Can be assigned only one time at all
    public void setWindowPattern(WindowPattern windowPattern) {
        if(windowPattern==null) throw new IllegalArgumentException();

        if(this.windowPattern==null){
            this.windowPattern = windowPattern;
            this.favorTokens = windowPattern.getDifficulty();
        }
    }

    //Can be assigned only once
    public void setPrivateObjectiveCard(PrivateObjectiveCard card) {
        if(card==null) throw new IllegalArgumentException();

        if(this.privateObjectiveCard==null){
            this.privateObjectiveCard = card;
        }
    }

    public PrivateObjectiveCard getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }


    //Decrease favorTokens of quantity. Return false if not enough tokens left.
    public boolean decreaseTokens(int quantity) {
        if(favorTokens<quantity) return false;
        favorTokens -= quantity;
        return true;
    }

    public WindowPattern getWindowPattern() {
        return windowPattern;
    }

    //Utils

    //Compares favorTokens with toolCard.getNeededTokens()
    public boolean canUseToolCard(ToolCard toolCard) {
        return toolCard.getNeededTokens() <= favorTokens;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }

        Player p = (Player) o;

        return this.nickname == p.getNickname();
    }
}
