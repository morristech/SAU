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
package simbio.se.sau.iterable;

import java.util.Iterator;

import simbio.se.sau.API;

/**
 * This class provide a {@link Iterable} range of {@link Integer}
 *
 * @author Ademar Alves de Oliveira <ademar111190@gmail.com>
 * @date 2013-sep-29 20:55:35
 * @see http://stackoverflow.com/questions/5858966/why-java-numbers-arent-iterable
 * @since {@link API#Version_2_0_0}
 */
public class Range implements Iterable<Integer> {

    //----------------------------------------------------------------------------------------------
    // Static Access
    //----------------------------------------------------------------------------------------------

    /**
     * A static way to get a {@link Range} instance, is a good idea use it with a static import like
     * "import static simbio.se.sau.iterable.Range.range;"
     *
     * @param start    the {@link Integer} to start the range "inclusive"
     * @param end      the {@link Integer} to finish the range "exclusive"
     * @param step     the {@link Integer} to be incremented on next {@link Integer}
     * @param iterator the {@link Iterator} of {@link Integer} to be used, you don't need pass it.
     * @return an {@link Range} instance formatted
     * @since {@link API#Version_2_0_0}
     */
    public static Range range(int start, int end, int step, Iterator<Integer> iterator) {
        return new Range(start, end, step, iterator);
    }

    /**
     * A static way to get a {@link Range} instance, is a good idea use it with a static import like
     * "import static simbio.se.sau.iterable.Range.range;". This use the default {@link Iterable} of
     * {@link Integer}
     *
     * @param start the {@link Integer} to start the range "inclusive"
     * @param end   the {@link Integer} to finish the range "exclusive"
     * @param step  the {@link Integer} to be incremented on next {@link Integer}
     * @return an {@link Range} instance formatted
     * @since {@link API#Version_2_0_0}
     */
    public static Range range(int start, int end, int step) {
        return new Range(start, end, step);
    }

    /**
     * A static way to get a {@link Range} instance, is a good idea use it with a static import like
     * "import static simbio.se.sau.iterable.Range.range;". This use the default {@link Iterable} of
     * {@link Integer} and use 1 like step
     *
     * @param start the {@link Integer} to start the range "inclusive"
     * @param end   the {@link Integer} to finish the range "exclusive"
     * @return an {@link Range} instance formatted
     * @since {@link API#Version_2_0_0}
     */
    public static Range range(int start, int end) {
        return new Range(start, end);
    }

    /**
     * A static way to get a {@link Range} instance, is a good idea use it with a static import like
     * "import static simbio.se.sau.iterable.Range.range;". This use the default {@link Iterable} of
     * {@link Integer}, uses 1 like step and 0 to start
     *
     * @param end the {@link Integer} to end the range "inclusive"
     * @return an {@link Range} instance formatted
     * @since {@link API#Version_2_0_0}
     */
    public static Range range(int end) {
        return new Range(end);
    }

    //----------------------------------------------------------------------------------------------
    // Class Fields
    //----------------------------------------------------------------------------------------------

    private int start;
    private int end;
    private int step;
    private int current;
    private Iterator<Integer> iterator;
    private boolean isDecreaseCount;

    /**
     * @param start    the {@link Integer} to start the range "inclusive"
     * @param end      the {@link Integer} to finish the range "exclusive"
     * @param step     the {@link Integer} to be incremented on next {@link Integer}
     * @param iterator the {@link Iterator} of {@link Integer} to be used, you don't need pass it.
     * @since {@link API#Version_2_0_0}
     */
    public Range(int start, int end, int step, Iterator<Integer> iterator) {
        init(start, end, step, iterator);
    }

    /**
     * This use the default {@link Iterable} of {@link Integer}
     *
     * @param start the {@link Integer} to start the range "inclusive"
     * @param end   the {@link Integer} to finish the range "exclusive"
     * @param step  the {@link Integer} to be incremented on next {@link Integer}
     * @since {@link API#Version_2_0_0}
     */
    public Range(int start, int end, int step) {
        init(start, end, step, makeAnNewIterator());
    }

    /**
     * This use the default {@link Iterable} of {@link Integer} and use 1 like step
     *
     * @param start the {@link Integer} to start the range "inclusive"
     * @param end   the {@link Integer} to finish the range "exclusive"
     * @since {@link API#Version_2_0_0}
     */
    public Range(int start, int end) {
        init(start, end, 1, makeAnNewIterator());
    }

    /**
     * This use the default {@link Iterable} of {@link Integer}, uses 1 like step and 0 to start
     *
     * @param end the {@link Integer} to end the range "inclusive"
     * @since {@link API#Version_2_0_0}
     */
    public Range(int end) {
        init(0, end, 1, makeAnNewIterator());
    }

    /**
     * a grouped setters to be started on constructor methods
     *
     * @param start    the {@link Integer} to start the range "inclusive"
     * @param end      the {@link Integer} to finish the range "exclusive"
     * @param step     the {@link Integer} to be incremented on next {@link Integer}
     * @param iterator the {@link Iterator} of {@link Integer} to be used.
     * @since {@link API#Version_2_0_0}
     */
    private void init(int start, int end, int step, Iterator<Integer> iterator) {
        this.start = start;
        this.end = end;
        this.step = Math.abs(step);
        this.iterator = iterator;
        restart();
    }

    /**
     * @return the default {@link Iterable} of {@link Integer}
     * @since {@link API#Version_2_0_0}
     */
    private Iterator<Integer> makeAnNewIterator() {
        return new Iterator<Integer>() {

            /*
             * (non-Javadoc)
             * 
             * @see java.util.Iterator#hasNext()
             */
            @Override
            public boolean hasNext() {
                if (isDecreaseCount)
                    return current > end;
                else
                    return current < end;
            }

            /*
             * (non-Javadoc)
             * 
             * @see java.util.Iterator#next()
             */
            @Override
            public Integer next() {
                int next = current;
                if (current < end)
                    current += step;
                else if (current > end)
                    current -= step;
                return next;
            }

            /*
             * (non-Javadoc)
             * 
             * @see java.util.Iterator#remove()
             */
            @Override
            public void remove() {
            }
        };
    }

    /**
     * Reset {@link Iterable},
     *
     * @since {@link API#Version_2_0_0}
     */
    public void restart() {
        current = start;
        isDecreaseCount = start > end;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<Integer> iterator() {
        return iterator;
    }

}
