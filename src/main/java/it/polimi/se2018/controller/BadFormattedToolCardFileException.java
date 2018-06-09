package it.polimi.se2018.controller;

/**
 * The class {@code BadFormattedToolCardFileException} is a form of {@link Exception}
 * that is used to notify that an .xml file representing a toolCard is bad formatted
 * and due to this can't be correctly processed.
 *
 * This usually happens in the {@link ToolCardManager} during the creation
 * of a requested ToolCard.
 *
 * This exception should not happen if no custom tool cards are added to the directory.
 * In case, check for files that could be became corrupted. If custom files were added,
 * try to remove that files.
 *
 * @author Federico Haag
 */
public class BadFormattedToolCardFileException extends RuntimeException {

    /**
     * Basic constructor for exception
     */
    public BadFormattedToolCardFileException() {
        super();
    }
}
