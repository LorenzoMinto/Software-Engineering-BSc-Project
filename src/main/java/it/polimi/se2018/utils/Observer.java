package it.polimi.se2018.utils;

/**
 * A class can implement the {@link Observer} interface when it
 * wants to be informed of changes in observable objects.
 *
 * @author Federico Haag
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public interface Observer {

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an {@link Observable} object's
     * {@link Observable#notify(Message)} method to have all the object's
     * observers notified of the change.
     *
     * @param message an argument passed to the {@link Observable#notify(Message)} method
     * @return true if message is successfully sent
     */
    boolean update(Message message);
}