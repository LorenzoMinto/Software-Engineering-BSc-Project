package it.polimi.se2018.controller;

/**
 * The class {@code BadFormattedPatternFileException} is a form of {@link Exception}
 * that is used to notify that an .xml file representing a pattern is bad formatted
 * and due to this can't be correctly processed.
 *
 * This usually happens in the {@link WindowPatternManager} during the creation
 * of a requested window pattern.
 *
 * This exception should not happen if no custom pattern are added to the directory.
 * In case, check for files that could be became corrupted. If custom files were added,
 * try to remove that files.
 *
 * @author Federico Haag
 */
public class BadFormattedPatternFileException extends RuntimeException {

    /**
     * Basic constructor for exception
     */
    public BadFormattedPatternFileException() {
        super();
    }
}
