package it.polimi.se2018.controller;

/**
 * The class {@code NoToolCardsFoundInFileSystemException} is a form of {@link Exception}
 * that is used to notify the application that no toolCards files were found in the
 * file system inspection. Usually happens in {@link ToolCardManager} during the
 * creation of a new {@link it.polimi.se2018.model.ToolCard}.
 *
 * @author Federico Haag
 */
public class NoToolCardsFoundInFileSystemException extends RuntimeException {

    /**
     * Basic constructor for exception
     */
    public NoToolCardsFoundInFileSystemException() {
        super();
    }
}
