/**
 * Copyright 2013-2014
 * Ademar Alves de Oliveira <ademar111190@gmail.com /> Simbio.se
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package simbio.se.sau.json;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import simbio.se.sau.API;
import simbio.se.sau.log.SimbiLog;

/**
 * This class provide with a static way methods to convert {@link Object} to Json {@link String} and
 * vice versa. This class use {@link Gson} to do it, so for more details see {@link Gson}
 *
 * @author Ademar Alves de Oliveira ademar111190@gmail.com
 * @date 2013-aŭg-15 00:44:14
 * @since {@link API#Version_1_0_0}
 */
public class JsonUtils {

    /**
     * The static instance of {@link Gson}
     */
    protected static Gson gson = new Gson();

    /**
     * This method convert a {@link Object} to a Json {@link String}, for more details see
     * {@link Gson#toJson(Object)}
     *
     * @param object to be converted to Json {@link String}
     * @return a {@link String} with Json format
     * @since {@link API#Version_1_0_0}
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * This method convert a Json {@link String} to a {@link Object} and if get some error return
     * the default object param, for more details see {@link Gson#fromJson(String, Class)}
     *
     * @param json     a Json {@link String} mapping the object
     * @param theClass The {@link Class} of object mapped
     * @param def      default object if it get some error
     * @return a {@link Object} represented from json parameter and with class struct from theClass.
     * @since {@link API#Version_1_0_0}
     */
    public static Object fromJson(String json, Class<?> theClass, Object def) {
        Object object = def;
        try {
            object = gson.fromJson(json, theClass);
        } catch (Exception e) {
            SimbiLog.printException(e);
        }
        return object;
    }

    /**
     * This method convert a Json {@link String} to a {@link Object} and if get some error return
     * <code>null</code>, for more details see {@link Gson#fromJson(String, Class)}
     *
     * @param json     a Json {@link String} mapping the object
     * @param theClass The {@link Class} of object mapped
     * @return a {@link Object} represented from json parameter and with class struct from theClass
     * or <code>null</code> if get some error.
     * @since {@link API#Version_1_0_0}
     */
    public static Object fromJsonOrNull(String json, Class<?> theClass) {
        return fromJson(json, theClass, null);
    }

    /**
     * This method convert a Json {@link String} to a {@link Object} and if get some error return
     * the default object param, for more details see {@link Gson#fromJson(String, Class)}
     *
     * @param json a Json {@link String} mapping the object
     * @param type The {@link Type} of object mapped
     * @param def  default object if it get some error
     * @return a {@link Object} represented from json parameter and with class struct from theClass.
     * @since {@link API#Version_2_0_0}
     */
    public static Object fromJsonWithType(String json, Type type, Object def) {
        Object object;
        try {
            object = gson.fromJson(json, type);
        } catch (Exception e) {
            return def;
        }
        if (object == null)
            return def;
        return object;
    }

    /**
     * This method convert a Json {@link String} to a {@link Object} and if get some error return
     * <code>null</code>, for more details see {@link Gson#fromJson(String, Class)}
     *
     * @param json a Json {@link String} mapping the object
     * @param type The {@link Type} of object mapped
     * @return a {@link Object} represented from json parameter and with class struct from theClass
     * or null if get some error.
     * @since {@link API#Version_2_0_0}
     */
    public static Object fromJsonWithTypeOrNull(String json, Type type) {
        return fromJsonWithType(json, type, null);
    }

}
