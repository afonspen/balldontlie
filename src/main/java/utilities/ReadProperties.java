package utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadProperties {


    private static final Logger log = LoggerFactory.getLogger(ReadProperties.class.getName());

    /**
     * Carga el fichero de propiedades.
     * @return Propiedades cargadas del fichero test.properties
     */
    public Properties loadProperties() {

        Properties config = new Properties();
        InputStream input = null;

        try {
             input = getClass().getResourceAsStream("/test.properties");

            config.load(input);
            setEnviromentProperties(config);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage(), e);
        }

        return config;
    }

    /**
     * Setea las propiedades del fichero como propiedades del sistema.
     * @param config Propiedades cargadas del fichero test.properties
     */
    private void setEnviromentProperties(Properties config) {

        String propiedad;

        @SuppressWarnings("unchecked")
        Enumeration<String> enumeration = (Enumeration<String>) config.propertyNames();
        while (enumeration.hasMoreElements()) {

            propiedad = enumeration.nextElement();

            if (propiedad != null) {
                if (System.getProperty(propiedad) != null) {
                    log.info("sistema......{}: {}", propiedad, System.getProperty(propiedad));

                    config.setProperty(propiedad, System.getProperty(propiedad));

                } else {
                    System.setProperty(propiedad, config.getProperty(propiedad));
                }
            }

        }

    }

}
