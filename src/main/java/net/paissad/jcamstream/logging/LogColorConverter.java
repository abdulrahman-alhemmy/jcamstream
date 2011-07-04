/*
 * JCamStream, simple Java application for video surveillance from webcams.
 * Copyright (C) 2011 Papa Issa DIAKHATE (paissad).
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.paissad.jcamstream.logging;

import static net.paissad.jcamstream.AnsiTermColors.BOLD;
import static net.paissad.jcamstream.AnsiTermColors.RED_COLOR;
import static net.paissad.jcamstream.AnsiTermColors.RESET_COLOR;
import static net.paissad.jcamstream.AnsiTermColors.YELLOW_COLOR;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * A customised ClassicConverter that colors the level of the logging event,
 * according to ANSI terminal conventions.
 * <p>
 * The example is taken from this <a
 * href="http://logback.qos.ch/manual/layouts.html#customConversionSpecifier"
 * >link</a>.
 * <p>
 * 
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class LogColorConverter extends ClassicConverter {

    private static final String END_COLOR   = RESET_COLOR;
    private static final String ERROR_COLOR = RED_COLOR;
    private static final String WARN_COLOR  = YELLOW_COLOR;

    /*
     * (non-Javadoc)
     * 
     * @see ch.qos.logback.core.pattern.Converter#convert(java.lang.Object)
     */
    @Override
    public String convert(ILoggingEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getColor(event.getLevel()));
        sb.append(event.getLevel());
        sb.append(END_COLOR);
        return sb.toString();
    }

    /**
     * Returns the appropriate characters to change the color for the specified
     * logging level.
     */
    private String getColor(Level level) {
        switch (level.toInt()) {

            case Level.ERROR_INT:
                return ERROR_COLOR;

            case Level.WARN_INT:
                return WARN_COLOR;

            case Level.TRACE_INT:
                return BOLD;

            default:
                return "";
        }
    }
}
