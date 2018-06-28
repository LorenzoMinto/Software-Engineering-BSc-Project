package it.polimi.se2018.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reads from file system the configuration file and imports the configs specs
 *
 * @author Federico Haag
 */
public final class ConfigImporter {

    /**
     * HashMap containing the properties read from the config file
     */
    private Properties properties = new Properties();

    /**
     * Config File Name
     */
    private final String configFileName;

    /**
     * Boolean stating if the properties were previously loaded or not (yes=true)
     */
    private boolean parametersLoaded = false;

    /**
     * Default config file path
     */
    private static final String DEFAULT_CONFIG_FILE_PATH = "default";

    /**
     * Path to the folder of config files
     */
    private static final String GENERAL_PATH = "configs/";

    /**
     * Constructor for a ConfigImporter that loads params from the default config file.
     */
    public ConfigImporter(){
        this(DEFAULT_CONFIG_FILE_PATH);
    }

    /**
     * Constructor for a ConfigImporter that loads params from the config file of the given name.
     *
     * @param configFileName the path where to read the config params
     */
    public ConfigImporter(String configFileName) {

        this.configFileName = configFileName;
    }

    /**
     * Loads from file system the properties (from the file specified in {@link ConfigImporter#configFileName}).
     *
     * @throws NoConfigParamFoundException if an IOException throws during the file's opening & reading
     */
    private void loadParameters() throws NoConfigParamFoundException{
        FileFinder fileFinder = new FileFinder();
        parametersLoaded = true;

        try (InputStream input = fileFinder.getResourceAsStream(GENERAL_PATH.concat(configFileName))) {

            this.properties.load(input);
        } catch(IOException e){
            throw new NoConfigParamFoundException();
        }
    }

    /**
     * @return Dictionary of parameters
     * @throws NoConfigParamFoundException if an error occurred loading parameters from file
     */
    public Properties getProperties() throws NoConfigParamFoundException{

        //Loads properties if not already loaded
        if(!parametersLoaded){ loadParameters(); }

        Properties p = new Properties();
        p.putAll(this.properties);
        return p;
    }
}
