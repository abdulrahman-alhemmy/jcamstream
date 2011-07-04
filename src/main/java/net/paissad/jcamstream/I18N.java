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
package net.paissad.jcamstream;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * This class provides a mechanism to localise the text messages. It is
 * based on {@link ResourceBundle}.
 * 
 * @author Papa Issa DIAKHATE (paissad)
 * 
 */
public class I18N {

    private static final String         BUNDLE_NAME     = "net.paissad.jcamstream.i18n.lang";
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private I18N() {
    }

    /**
     * Returns the locale-specific string associated with the key.
     * 
     * @param key
     *            - The key for which we want the associated value.
     * @return Descriptive String if key is found, or a copy of the key if the
     *         the demanded value is not found
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException mre) {
            return "!" + key + "!";
        }
    }

    /**
     * Gets the current ResourceBundle used.
     * 
     * @return The current ResourceBundle which is used.
     */
    public static ResourceBundle getResourceBundle() {
        return RESOURCE_BUNDLE;
    }

}
