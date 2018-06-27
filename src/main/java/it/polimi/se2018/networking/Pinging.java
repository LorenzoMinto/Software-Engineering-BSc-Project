package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

public class Pinging extends Thread{

    private final SenderInterface sender;

    private final Enum ping;

    Pinging(SenderInterface sender, Enum ping) {
        this.sender = sender;
        this.ping = ping;
    }

    @Override
    public void run() {
        super.run();

        //noinspection InfiniteLoopStatement
        while(true){

            try {
                sender.sendMessage(new Message(this.ping));
            } catch (NetworkingException e) {
                //Gateway may manage this
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
