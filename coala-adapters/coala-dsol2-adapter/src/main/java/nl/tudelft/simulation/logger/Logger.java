/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/nl/tudelft/simulation/logger/Logger.java $
 * 
 * @(#) Logger.java Aug 27, 2003 Copyright (c) 2002-2005 Delft University of
 * Technology Jaffalaan 5, 2628 BX Delft, the Netherlands. All rights reserved.
 * This software is proprietary information of Delft University of Technology
 * 
 * Part of the EU project INERTIA, see http://www.inertia-project.eu/
 * 
 * @license
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright © 2010-2013 Almende B.V. 
 * 
 */
package nl.tudelft.simulation.logger;

/**
 * Logger
 *
 *
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.slf4j.bridge.SLF4JBridgeHandler;

import nl.tudelft.simulation.event.util.EventProducingMap;
import nl.tudelft.simulation.language.reflection.ClassUtil;

/**
 * Provides a static class to Sun's logging framework.
 * <p>
 * (c) copyright 2002-2005 <a href="http://www.simulation.tudelft.nl">Delft
 * University of Technology </a>, the Netherlands. <br>
 * See for project information <a href="http://www.simulation.tudelft.nl">
 * www.simulation.tudelft.nl </a> <br>
 * License of use: <a href="http://www.gnu.org/copyleft/lesser.html">Lesser
 * General Public License (LGPL) </a>, no warranty.
 * 
 * @author <a href="http://www.peter-jacobs.com">Peter Jacobs </a>
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 * @date $Date: 2014-04-30 15:15:27 +0200 (Wed, 30 Apr 2014) $
 * @version $Revision: 247 $
 */
public class Logger
{
	static {
		// divert java.util.logging.Logger LogRecords to SLF4J
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();
	}

    /** loggers are the currently available loggers */
    public static final EventProducingMap<String, java.util.logging.Logger> LOGGERS = new EventProducingMap<String, java.util.logging.Logger>(
            Collections
                    .synchronizedMap(new HashMap<String, java.util.logging.Logger>()));

    /** the defaultHandler to use */
    private static Class<?> defaultHandler = ConsoleHandler.class;

    /** the logLevel threshold */
    private static Level logLevel = Level.WARNING;

    /**
     * constructs a new Logger
     */
    protected Logger()
    {
        // Unreachable code
    }

    /**
     * resolves the caller
     * 
     * @param caller the caller
     * @return the class of the caller. caller if caller instanceof Caller
     */
    private static String resolveCaller(final Object caller)
    {
        if (caller instanceof Class)
        {
            return ((Class<?>) caller).getName();
        }
        return caller.getClass().getName();
    }

    /**
     * resolves the logger for a caller
     * 
     * @param caller the object invoking the loggers
     * @return Logger the logger
     */
    public static synchronized java.util.logging.Logger resolveLogger(
            final Object caller)
    {
        if (caller == null)
        {
            return null;
        }
        Class<?> callerClass = caller.getClass();
        if (caller instanceof Class)
        {
            callerClass = (Class<?>) caller;
        }
        String loggerName = callerClass.getPackage().getName();
        java.util.logging.Logger logger = LOGGERS.get(loggerName);
        if (logger != null)
        {
            return logger;
        }
        return Logger.createLogger(loggerName);
    }

    /**
     * creates a new Logger
     * 
     * @param loggerName the name of the logger
     * @return Logger the name
     */
    private static synchronized java.util.logging.Logger createLogger(
            final String loggerName)
    {
        java.util.logging.Logger logger = java.util.logging.Logger
                .getLogger(loggerName);

        Handler[] handlers = logger.getHandlers();
        for (int i = 0; i < handlers.length; i++)
        {
            logger.removeHandler(handlers[i]);
        }
        Handler handler = null;
        try
        {
            handler = (Handler) ClassUtil.resolveConstructor(defaultHandler,
                    null).newInstance();
        } catch (Exception exception)
        {
            handler = new ConsoleHandler();
        }
        handler.setLevel(Logger.logLevel);
        logger.addHandler(handler);
        logger.setLevel(Logger.logLevel);
        logger.setUseParentHandlers(false);
        LOGGERS.put(loggerName, logger);
        return logger;
    }

    /**
     * configs a message
     * 
     * @param caller the caller
     * @param methodName the name of the method
     * @param msg the message
     */
    public static void config(final Object caller, final String methodName,
            final String msg)
    {
        Logger.resolveLogger(caller).config(methodName + ": " + msg);
    }

    /**
     * Logs a method entry.
     * 
     * @param caller the object calling the logger
     * @param arg0 the name of the class calling the method
     * @param arg1 the name of the method entering
     * @param arg2 parameter to the method being entered
     */
    public static void entering(final Object caller, final String arg0,
            final String arg1, final Object arg2)
    {
        Logger.resolveLogger(caller).entering(arg0, arg1, arg2);
    }

    /**
     * Logs a method entry.
     * 
     * @param caller the object calling the logger
     * @param arg0 the name of the class calling the method
     * @param arg1 the name of the method entering
     * @param arg2 parameters to the method being entered
     */
    public static void entering(final Object caller, final String arg0,
            final String arg1, final Object[] arg2)
    {
        Logger.resolveLogger(caller).entering(arg0, arg1, arg2);
    }

    /**
     * Logs a method entry.
     * 
     * @param caller the object calling the logger
     * @param arg0 the name of the class calling the method
     * @param arg1 the name of the method entering
     */
    public static void entering(final Object caller, final String arg0,
            final String arg1)
    {
        Logger.resolveLogger(caller).entering(arg0, arg1);
    }

    /**
     * Logs a method exit.
     * 
     * @param caller the object calling the logger
     * @param arg0 the name of the class calling the method
     * @param arg1 the name of the method entering
     * @param arg2 result of the method
     */
    public static void exiting(final Object caller, final String arg0,
            final String arg1, final Object arg2)
    {
        Logger.resolveLogger(caller).exiting(arg0, arg1, arg2);
    }

    /**
     * Logs a method exit.
     * 
     * @param caller the object calling the logger
     * @param arg0 the name of the class calling the method
     * @param arg1 the name of the method entering
     */
    public static void exiting(final Object caller, final String arg0,
            final String arg1)
    {
        Logger.resolveLogger(caller).exiting(arg0, arg1);
    }

    /**
     * Logs a fine message
     * 
     * @param caller the object calling the logger
     * @param methodName the name of the method
     * @param msg the message
     */
    public static void fine(final Object caller, final String methodName,
            final String msg)
    {
        Logger.resolveLogger(caller).fine(methodName + ": " +  msg);
    }

    /**
     * Logs a finer message
     * 
     * @param caller the object calling the logger
     * @param methodName the name of the method
     * @param msg the message
     */
    public static void finer(final Object caller, final String methodName,
            final String msg)
    {
        Logger.resolveLogger(caller).finer(methodName + ": " +  msg);
    }

    /**
     * Logs a finest message
     * 
     * @param caller the object calling the logger
     * @param methodName the name of the method
     * @param msg the message
     */
    public static void finest(final Object caller, final String methodName,
            final String msg)
    {
        Logger.resolveLogger(caller).finest(methodName + ": " +  msg);
    }

    /**
     * returns the names of the currently used loggers
     * 
     * @return String[] the names
     */
    public static synchronized String[] getLoggerNames()
    {
        return LOGGERS.keySet().toArray(new String[LOGGERS.keySet().size()]);
    }

    /**
     * Logs a info message
     * 
     * @param caller the object calling the logger
     * @param methodName the name of the method
     * @param msg the message
     */
    public static void info(final Object caller, final String methodName,
            final String msg)
    {
        Logger.resolveLogger(caller).info(methodName + ": " +  msg);
    }

    /**
     * Logs a message, with associated parameter
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the message
     * @param arg2 the parameter
     */
    public static void log(final Object caller, final Level arg0,
            final String arg1, final Object arg2)
    {
        Logger.resolveLogger(caller).log(arg0, arg1, arg2);
    }

    /**
     * Logs a message, with associated parameters
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the message
     * @param arg2 the parameters
     */
    public static void log(final Object caller, final Level arg0,
            final String arg1, final Object[] arg2)
    {
        Logger.resolveLogger(caller).log(arg0, arg1, arg2);
    }

    /**
     * Logs a message, with associated throwable
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the message
     * @param arg2 the throwable
     */
    public static void log(final Object caller, final Level arg0,
            final String arg1, final Throwable arg2)
    {
        Logger.resolveLogger(caller).log(arg0, arg1, arg2);
    }

    /**
     * Logs a message
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the message
     */
    public static void log(final Object caller, final Level arg0,
            final String arg1)
    {
        Logger.resolveLogger(caller).log(arg0, arg1);
    }

    /**
     * Logs a log record
     * 
     * @param caller the object calling the logger
     * @param arg0 the log record
     */
    public static void log(final Object caller, final LogRecord arg0)
    {
        Logger.resolveLogger(caller).log(arg0);
    }

    /**
     * Logs a message, with associated parameter and class information
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the source class
     * @param arg2 the method information
     * @param arg3 the message
     * @param arg4 the parameter
     */
    public static void logp(final Object caller, final Level arg0,
            final String arg1, final String arg2, final String arg3,
            final Object arg4)
    {
        Logger.resolveLogger(caller).logp(arg0, arg1, arg2, arg3, arg4);
    }

    /**
     * Logs a message, with associated parameter and class information
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the source class
     * @param arg2 the method information
     * @param arg3 the message
     * @param arg4 the parameters
     */
    public static void logp(final Object caller, final Level arg0,
            final String arg1, final String arg2, final String arg3,
            final Object[] arg4)
    {
        Logger.resolveLogger(caller).logp(arg0, arg1, arg2, arg3, arg4);
    }

    /**
     * Logs a message, with associated throwable and class information
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the source class
     * @param arg2 the method information
     * @param arg3 the message
     * @param arg4 the parameters
     */
    public static void logp(final Object caller, final Level arg0,
            final String arg1, final String arg2, final String arg3,
            final Throwable arg4)
    {
        Logger.resolveLogger(caller).logp(arg0, arg1, arg2, arg3, arg4);
    }

    /**
     * Logs a message, with associated class information
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the source class
     * @param arg2 the method information
     * @param arg3 the message
     */
    public static void logp(final Object caller, final Level arg0,
            final String arg1, final String arg2, final String arg3)
    {
        Logger.resolveLogger(caller).logp(arg0, arg1, arg2, arg3);
    }

    /**
     * Logs a message, with associated parameter and class information and
     * resource bundle
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the source class
     * @param arg2 the method information
     * @param arg3 the resource bundle
     * @param arg4 the message
     * @param arg5 the parameter
     */
    //@SuppressWarnings("deprecation")
	public static void logrb(final Object caller, final Level arg0,
            final String arg1, final String arg2, final String arg3,
            final String arg4, final Object arg5)
    {
        Logger.resolveLogger(caller).logrb(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Logs a message, with associated parameters and class information and
     * resource bundle
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the source class
     * @param arg2 the method information
     * @param arg3 the resource bundle
     * @param arg4 the message
     * @param arg5 the parameters
     */
    //@SuppressWarnings("deprecation")
	public static void logrb(final Object caller, final Level arg0,
            final String arg1, final String arg2, final String arg3,
            final String arg4, final Object[] arg5)
    {
        Logger.resolveLogger(caller).logrb(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Logs a message, with associated throwable and class information and
     * resource bundle
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the source class
     * @param arg2 the method information
     * @param arg3 the resource bundle
     * @param arg4 the message
     * @param arg5 the parameters
     */
    //@SuppressWarnings("deprecation")
	public static void logrb(final Object caller, final Level arg0,
            final String arg1, final String arg2, final String arg3,
            final String arg4, final Throwable arg5)
    {
        Logger.resolveLogger(caller).logrb(arg0, arg1, arg2, arg3, arg4, arg5);
    }

    /**
     * Logs a message, with associated class information and resource bundle
     * 
     * @param caller the object calling the logger
     * @param arg0 the log level
     * @param arg1 the source class
     * @param arg2 the method information
     * @param arg3 the resource bundle
     * @param arg4 the message
     */
    //@SuppressWarnings("deprecation")
	public static void logrb(final Object caller, final Level arg0,
            final String arg1, final String arg2, final String arg3,
            final String arg4)
    {
        Logger.resolveLogger(caller).logrb(arg0, arg1, arg2, arg3, arg4);
    }

    /**
     * Logs a severe message
     * 
     * @param caller the object calling the logger
     * @param methodName the name of the method
     * @param msg the message
     */
    public static void severe(final Object caller, final String methodName,
            final String msg)
    {
        Logger.resolveLogger(caller).logp(Level.SEVERE, resolveCaller(caller),
                methodName, msg);
    }

    /**
     * Logs a severe message
     * 
     * @param caller the object calling the logger
     * @param methodName the name of the method
     * @param throwable the throwable
     */
    public static void severe(final Object caller, final String methodName,
            final Throwable throwable)
    {
        Logger.resolveLogger(caller).logp(Level.SEVERE, resolveCaller(caller),
                methodName, throwable.getLocalizedMessage(), throwable);
    }

    /**
     * logs a throwable message
     * 
     * @param caller the object invoking the logger
     * @param arg0 the class
     * @param arg1 the method
     * @param arg2 the throwable
     */
    public static void throwing(final Object caller, final String arg0,
            final String arg1, final Throwable arg2)
    {
        Logger.resolveLogger(caller).throwing(arg0, arg1, arg2);
    }

    /**
     * Logs a warning message
     * 
     * @param caller the object calling the logger
     * @param methodName the name of the method
     * @param msg the message
     */
    public static void warning(final Object caller, final String methodName,
            final String msg)
    {
        Logger.resolveLogger(caller).logp(Level.WARNING, resolveCaller(caller),
                methodName, msg);
    }

    /**
     * Logs a warning message
     * 
     * @param caller the object calling the logger
     * @param methodName the name of the method
     * @param thrown the thrown
     */
    public static void warning(final Object caller, final String methodName,
            final Throwable thrown)
    {
        Logger.resolveLogger(caller).logp(Level.WARNING, resolveCaller(caller),
                methodName, thrown.getLocalizedMessage(), thrown);
    }

    /**
     * @return Returns the defaultHandler.
     */
    public static Class<?> getDefaultHandler()
    {
        return defaultHandler;
    }

    /**
     * @param defaultHandler The defaultHandler to set.
     */
    public static void setDefaultHandler(final Class<?> defaultHandler)
    {
        if (Handler.class.isAssignableFrom(defaultHandler))
        {
            Logger.defaultHandler = defaultHandler;
        } else
        {
            throw new IllegalArgumentException(
                    "defaultHandler should extend Handler");
        }
    }

    /**
     * @return Returns the logLevel.
     */
    public static Level getLogLevel()
    {
        return Logger.logLevel;
    }

    /**
     * @param logLevel The logLevel to set.
     */
    public static void setLogLevel(final Level logLevel)
    {
        Logger.logLevel = logLevel;
    }

}