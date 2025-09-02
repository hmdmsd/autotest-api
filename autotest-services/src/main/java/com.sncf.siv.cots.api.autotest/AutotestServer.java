package com.sncf.siv.cots.api.autotest;

import com.sncf.siv.cots.api.autotest.config.MainConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import sncf.mobilite.vpr.socle.commun.springboot.RegisteredServerComponent;

/**
 * Auto Test Application Configuration Bean.
 *
 * Simplified version for testing other COTS services.
 */
@Import({MainConfiguration.class})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class AutotestServer extends RegisteredServerComponent {

    /**
     * Méthode principale pour le lancement de SpringBoot
     *
     * @param args arguments de la ligne de commande
     */
    public static void main(String[] args) {
        run("autotestApi", "autotestApi", AutotestServer.class, args);
    }
}