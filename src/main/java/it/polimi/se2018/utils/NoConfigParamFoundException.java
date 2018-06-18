package it.polimi.se2018.utils;

/**
 * The class {@code NoConfigParamFoundException} is a form of {@link Exception}
 * that is thrown if ConfigImporter receives the request for a property that can't
 * find in the setted config file.
 *
 * @author Federico Haag
 */
public class NoConfigParamFoundException extends Exception {

    /**
     * Basic constructor. Do nothing else than calling super()
     */
    public NoConfigParamFoundException() {
        super();
        //do nothing else
    }
}
