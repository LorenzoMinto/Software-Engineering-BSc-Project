package it.polimi.se2018.controller;

/**
 * The class {@code NoToolCardsFoundInFileSystemException} is a form of {@link Exception}
 * that is used to notify the application that no toolcards files were found in the
 * file system inspection. Usually happens in {@link ToolCardsManager} during the
 * creation of a new {@link it.polimi.se2018.model.ToolCard}.
 *
 * @author Federico Haag
 */
public class NoToolCardsFoundInFileSystemException extends Exception {
    public NoToolCardsFoundInFileSystemException() {
        super();
    }
}
