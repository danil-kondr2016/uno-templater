/*
 * Copyright (c) 2024 Danila A. Kondratenko
 *
 * This file is a part of UNO Templater.
 *
 * UNO Templater is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * UNO Templater is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with UNO Templater.  If not, see <https://www.gnu.org/licenses/>.
 */

package ru.danilakondr.templater.macros;

import com.sun.star.beans.PropertyValue;
import com.sun.star.document.XDocumentInsertable;
import com.sun.star.text.XTextCursor;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.uno.UnoRuntime;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.regex.Pattern;

/**
 * Обрабатывает макросы вида %INCLUDE(...)%. Вставляет содержимое заданных
 * файлов на нужные места. Обработка %INCLUDE(...)% может происходить несколько
 * раз.
 *
 * @author Данила А. Кондратенко
 * @since 0.3.0, 0.1.5
 */
public class DocumentIncludeSubstitutor implements MacroSubstitutor.Substitutor {
    private static final Pattern macroPattern = Pattern.compile("%INCLUDE\\((.*)\\)%");
    @Override
    public void substitute(XTextDocument xDoc, XTextRange xRange) {
        String include = macroPattern.matcher(xRange.getString()).replaceAll("$1");

        File f = new File(include).getAbsoluteFile();
        if (!f.exists()) {
            throw new RuntimeException(new FileNotFoundException(f.getAbsolutePath()));
        }

        String url = f.toURI().toString();
        try {
            XTextCursor xCursor = xRange.getText().createTextCursorByRange(xRange);
            xCursor.gotoRange(xRange, true);

            XDocumentInsertable xInsertable = UnoRuntime.queryInterface(XDocumentInsertable.class, xCursor);
            xInsertable.insertDocumentFromURL(url, new PropertyValue[0]);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean test(XTextRange xRange) {
        return macroPattern.matcher(xRange.getString()).matches();
    }
}
