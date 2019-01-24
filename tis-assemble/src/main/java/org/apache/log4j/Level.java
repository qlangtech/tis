/* 
 * The MIT License
 *
 * Copyright (c) 2018-2022, qinglangtech Ltd
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.apache.log4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

/*
 * Defines the minimum set of levels recognized by the system, that is
 * <code>OFF</code>, <code>FATAL</code>, <code>ERROR</code>, <code>WARN</code>,
 * <code>INFO</code, <code>DEBUG</code> and <code>ALL</code>.
 * <p>
 * The <code>Level</code> class may be subclassed to define a larger level set.
 *
 * @author 百岁（baisui@qlangtech.com）
 * @date 2019年1月17日
 */
public class Level extends Priority implements Serializable {

    /**
     * TRACE level integer value.
     *
     * @since 1.2.12
     */
    public static final int TRACE_INT = 5000;

    /**
     * The <code>OFF</code> has the highest possible rank and is intended to
     * turn off logging.
     */
    public static final Level OFF = new Level(OFF_INT, "OFF", 0);

    /**
     * The <code>FATAL</code> level designates very severe error events that
     * will presumably lead the application to abort.
     */
    public static final Level FATAL = new Level(FATAL_INT, "FATAL", 0);

    /**
     * The <code>ERROR</code> level designates error events that might still
     * allow the application to continue running.
     */
    public static final Level ERROR = new Level(ERROR_INT, "ERROR", 3);

    /**
     * The <code>WARN</code> level designates potentially harmful situations.
     */
    public static final Level WARN = new Level(WARN_INT, "WARN", 4);

    /**
     * The <code>INFO</code> level designates informational messages that
     * highlight the progress of the application at coarse-grained level.
     */
    public static final Level INFO = new Level(INFO_INT, "INFO", 6);

    /**
     * The <code>DEBUG</code> Level designates fine-grained informational events
     * that are most useful to debug an application.
     */
    public static final Level DEBUG = new Level(DEBUG_INT, "DEBUG", 7);

    /**
     * The <code>TRACE</code> Level designates finer-grained informational
     * events than the <code>DEBUG</code level.
     *
     * @since 1.2.12
     */
    public static final Level TRACE = new Level(TRACE_INT, "TRACE", 7);

    /**
     * The <code>ALL</code> has the lowest possible rank and is intended to turn
     * on all logging.
     */
    public static final Level ALL = new Level(ALL_INT, "ALL", 7);

    /**
     * Serialization version id.
     */
    static final long serialVersionUID = 3491141966387921974L;

    /**
     * Instantiate a Level object.
     */
    protected Level(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    /**
     * Convert the string passed as argument to a level. If the conversion
     * fails, then this method returns {@link #DEBUG}.
     */
    public static Level toLevel(String sArg) {
        return (Level) toLevel(sArg, Level.DEBUG);
    }

    /**
     * Convert an integer passed as argument to a level. If the conversion
     * fails, then this method returns {@link #DEBUG}.
     */
    public static Level toLevel(int val) {
        return (Level) toLevel(val, Level.DEBUG);
    }

    /**
     * Convert an integer passed as argument to a level. If the conversion
     * fails, then this method returns the specified default.
     */
    public static Level toLevel(int val, Level defaultLevel) {
        switch(val) {
            case ALL_INT:
                return ALL;
            case DEBUG_INT:
                return Level.DEBUG;
            case INFO_INT:
                return Level.INFO;
            case WARN_INT:
                return Level.WARN;
            case ERROR_INT:
                return Level.ERROR;
            case FATAL_INT:
                return Level.FATAL;
            case OFF_INT:
                return OFF;
            case TRACE_INT:
                return Level.TRACE;
            default:
                return defaultLevel;
        }
    }

    /**
     * Convert the string passed as argument to a level. If the conversion
     * fails, then this method returns the value of <code>defaultLevel</code>.
     */
    public static Level toLevel(String sArg, Level defaultLevel) {
        if (sArg == null)
            return defaultLevel;
        String s = sArg.toUpperCase();
        if (s.equals("ALL"))
            return Level.ALL;
        if (s.equals("DEBUG"))
            return Level.DEBUG;
        if (s.equals("INFO"))
            return Level.INFO;
        if (s.equals("WARN"))
            return Level.WARN;
        if (s.equals("ERROR"))
            return Level.ERROR;
        if (s.equals("FATAL"))
            return Level.FATAL;
        if (s.equals("OFF"))
            return Level.OFF;
        if (s.equals("TRACE"))
            return Level.TRACE;
        // 
        if (s.equals("\u0130NFO"))
            return Level.INFO;
        return defaultLevel;
    }

    /**
     * Custom deserialization of Level.
     *
     * @param s
     *            serialization stream.
     * @throws IOException
     *             if IO exception.
     * @throws ClassNotFoundException
     *             if class not found.
     */
    private void readObject(final ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        level = s.readInt();
        syslogEquivalent = s.readInt();
        levelStr = s.readUTF();
        if (levelStr == null) {
            levelStr = "";
        }
    }

    /**
     * Serialize level.
     *
     * @param s
     *            serialization stream.
     * @throws IOException
     *             if exception during serialization.
     */
    private void writeObject(final ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        s.writeInt(level);
        s.writeInt(syslogEquivalent);
        s.writeUTF(levelStr);
    }

    /**
     * Resolved deserialized level to one of the stock instances. May be
     * overriden in classes derived from Level.
     *
     * @return resolved object.
     * @throws ObjectStreamException
     *             if exception during resolution.
     */
    private Object readResolve() throws ObjectStreamException {
        // 
        if (getClass() == Level.class) {
            return toLevel(level);
        }
        // 
        return this;
    }
}
