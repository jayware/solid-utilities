/**
 * Solid Utilities -- A collection of utility classes.
 *
 * Copyright (C) 2016 Markus Neubauer <markus.neubauer@jayware.org>,
 *                    Alexander Haumann <alexander.haumann@jayware.org>,
 *                    Manuel Hinke <manuel.hinke@jayware.org>,
 *                    Marina Schilling <marina.schilling@jayware.org>,
 *                    Elmar Schug <elmar.schug@jayware.org>,
 *
 *     This file is part of Solid Utilities.
 *
 *     Solid Utilities is free software; you can redistribute it and/or
 *     modify it under the terms of the GNU Lesser General Public License
 *     as published by the Free Software Foundation, either version 3 of
 *     the License, or any later version.
 *
 *     Solid Utilities is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *     Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jayware.solid.utilities;


import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jayware.solid.utilities.GlobUtil.toRegex;


public class GlobUtilTest
{
    @Test
    public void starBecomesDotStar() throws Exception {
        assertThat(toRegex("gl*b")).isEqualTo("gl.*b");
    }

    @Test
    public void escapedStarIsUnchanged() throws Exception {
        assertThat(toRegex("gl\\*b")).isEqualTo("gl\\*b");
    }

    @Test
    public void questionMarkBecomesDot() throws Exception {
        assertThat(toRegex("gl?b")).isEqualTo("gl.b");
    }

    @Test
    public void escapedQuestionMarkIsUnchanged() throws Exception {
        assertThat(toRegex("gl\\?b")).isEqualTo("gl\\?b");
    }

    @Test
    public void characterClassesDontNeedConversion() throws Exception {
        assertThat(toRegex("gl[-o]b")).isEqualTo("gl[-o]b");
    }

    @Test
    public void escapedClassesAreUnchanged() throws Exception {
        assertThat(toRegex("gl\\[-o\\]b")).isEqualTo("gl\\[-o\\]b");
    }

    @Test
    public void negationInCharacterClasses() throws Exception {
        assertThat(toRegex("gl[!a-n!p-z]b")).isEqualTo("gl[^a-n!p-z]b");
    }

    @Test
    public void nestedNegationInCharacterClasses() throws Exception {
        assertThat(toRegex("gl[[!a-n]!p-z]b")).isEqualTo("gl[[^a-n]!p-z]b");
    }

    @Test
    public void escapeCaratIfItIsTheFirstCharInACharacterClass() throws Exception {
        assertThat(toRegex("gl[^o]b")).isEqualTo("gl[\\^o]b");
    }

    @Test
    public void metacharsAreEscaped() throws Exception {
        assertThat(toRegex("gl?*.()+|^$@%b")).isEqualTo("gl..*\\.\\(\\)\\+\\|\\^\\$\\@\\%b");
    }

    @Test
    public void metacharsInCharacterClassesDontNeedEscaping() throws Exception {
        assertThat(toRegex("gl[?*.()+|^$@%]b")).isEqualTo("gl[?*.()+|^$@%]b");
    }

    @Test
    public void escapedBackslashIsUnchanged() throws Exception {
        assertThat(toRegex("gl\\\\b")).isEqualTo("gl\\\\b");
    }

    @Test
    public void slashQAndSlashEAreEscaped() throws Exception {
        assertThat(toRegex("\\Qglob\\E")).isEqualTo("\\\\Qglob\\\\E");
    }

    @Test
    public void bracesAreTurnedIntoGroups() throws Exception {
        assertThat(toRegex("{glob,regex}")).isEqualTo("(glob|regex)");
    }

    @Test
    public void escapedBracesAreUnchanged() throws Exception {
        assertThat(toRegex("\\{glob\\}")).isEqualTo("\\{glob\\}");
    }

    @Test
    public void commasDontNeedEscaping() throws Exception {
        assertThat(toRegex("{glob\\,regex},")).isEqualTo("(glob,regex),");
    }
}
