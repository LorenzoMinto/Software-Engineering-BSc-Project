package it.polimi.se2018.networking;

import it.polimi.se2018.utils.Message;

/**
 * Class used to ping server / client when connection network is active
 */
public class Pinging extends Thread{

    /**
     * Sender of the ping message
     */
    private final SenderInterface sender;

    /**
     * Message type to send as ping
     */
    private final Enum ping;

    /**
     * Class constructor
     *
     * @param sender sender of the ping message
     * @param ping message type to send as ping
     */
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
