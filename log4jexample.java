package com.mycompany.log4jwork;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.io.IoBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.*;

public class log4jexample{

    public static void main(String[] args) throws IOException,SQLException{
        // Config search functionality
        ConfigurationFactory factory = XmlConfigurationFactory.getInstance();
        ConfigurationSource configurationSource = new ConfigurationSource(new FileInputStream(new File("/home/user/myproject/src/main/resources/configuration.xml")));
        Configuration configuration = factory.getConfiguration((LoggerContext) LogManager.getContext(),configurationSource);
        ConsoleAppender appender = ConsoleAppender.createDefaultAppenderForLayout(PatternLayout.createDefaultLayout());
        configuration.addAppender(appender);
        LoggerConfig loggerConfig = new LoggerConfig("MyLoggerConfig",Level.DEBUG,false);
        loggerConfig.addAppender(appender,null,null);
        configuration.addLogger("com", loggerConfig);
        LoggerContext context = new LoggerContext("MyLoggerContext");
        context.start(configuration);
        Logger logger = context.getLogger("MyLoggerConfig");
        logger.error("Error Message Logged !!!", new NullPointerException("Null Value Error"));
        logger.log(Level.FATAL, "Fatal Level Message Logged");
        logger.log(Level.ERROR, "Error Level Message Logged");

        ProcessBuilder processBuilder = new ProcessBuilder("/usr/bin/python3",
                "/home/user/foobar/infinite_print.py").redirectErrorStream(true).inheritIO();
        Process process = processBuilder.start();
        while (process.isAlive()) {
            // System.out.println(new String(IOUtils.toByteArray(process.getOutputStream().toString())));
            System.out.println(new String(IOUtils.toByteArray(process.getInputStream())));
            logger.info(new String(IOUtils.toByteArray(process.getInputStream())));
        }
    }

    public static Process logToFile()
        throws IOException
    {
        final ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(
                "/usr/bin/python3",
                "/home/user/foobar/infinite_print.py")
                .redirectErrorStream(true) // redirect error stream to output stream
                .redirectOutput(new File("/home/user/foobar/python_out.log"));

        return processBuilder.start();
    }

    public static Process logToStdout()
            throws IOException
    {
        final ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(
                "/usr/bin/python3",
                "/home/user/foobar/infinite_print.py")
                .redirectErrorStream(true) // redirect error stream to output stream
                .inheritIO();

        return processBuilder.start();
    }

}
