package it.polimi.se2018.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an observable object.
 *
 * It can be subclassed to represent an object that the application wants to have observed.
 *
 * An observable object can have one or more observers. An observer
 * may be any object that implements interface {@link Observer}. After an
 * observable instance changes, an application calling the
 * {@link Observable#notify(Message)} method
 * causes all of its observers to be notified of the change by a call
 * to their {@link Observer#update(Message)} method.
 *
 * The order in which notifications will be delivered is unspecified.
 * The default implementation provided in the Observable class will
 * notify Observers in the order in which they registered interest.
 *
 * When an observable object is newly created, its set of observers is
 * empty.
 *
 * @author Federico Haag
 * @author Lorenzo Minto
 * @author Jacopo Pio Gargano
 */
public class Observable {

    private final List<Observer> observers = new ArrayList<>();

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param   observer   an observer to be added
     */
    public void register(Observer observer){
        synchronized (observers) {
            observers.add(observer);
        }
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing null to this method will have no effect.
     *
     * @param   observer   the observer to be deleted
     */
    public void deregister(Observer observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }


    /**
     * Calls all observers method update sending the received message
     *
     * @param message message received from the calling application
     */ 
    public void notify(Message message){
        synchronized (observers) {
            for(Observer observer : observers){
                observer.update(message);
            }
        }
    }

}

