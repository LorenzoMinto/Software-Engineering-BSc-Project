package it.polimi.se2018.controller;

/**
 * The class {@code NoPatternsFoundInFileSystemException} is a form of {@link Exception}
 * that is used to notify the application that no patterns files were found in the
 * file system inspection. Usually happens in {@link WindowPatternManager} during the
 * creation of a new {@link it.polimi.se2018.model.WindowPattern}.
 *
 * @author Federico Haag
 */
public class NoPatternsFoundInFileSystemException extends RuntimeException {

    /**
     * Basic constructor for exception
     */
    public NoPatternsFoundInFileSystemException() {
        super();
    }
}
