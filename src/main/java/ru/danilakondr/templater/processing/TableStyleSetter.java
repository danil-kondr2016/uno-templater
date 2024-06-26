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

package ru.danilakondr.templater.processing;

import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNamed;
import com.sun.star.table.BorderLine;
import com.sun.star.table.TableBorder;
import com.sun.star.text.XTextDocument;
import com.sun.star.text.XTextRange;
import com.sun.star.text.XTextSection;
import com.sun.star.text.XTextTable;
import com.sun.star.uno.UnoRuntime;

/**
 * Установщик стилей таблиц.
 *
 * @author Данила А. Кондратенко
 * @since 0.3.0, 0.2.8
 */
public class TableStyleSetter implements TextDocument.ObjectProcessor<XTextTable> {
    public static final int TABLE_LINE_WIDTH = 17; // around 1/2 * (25.4/72) mm
    @Override
    public void process(XTextTable xTable, XTextDocument xDoc) {
        if (isNotToBeProcessed(xTable))
            return;

        XPropertySet xTableProp = UnoRuntime
                .queryInterface(XPropertySet.class, xTable);
        TableBorder tableBorder = new TableBorder();

        tableBorder.HorizontalLine = new BorderLine();
        tableBorder.HorizontalLine.OuterLineWidth = TABLE_LINE_WIDTH;
        tableBorder.IsHorizontalLineValid = true;

        tableBorder.VerticalLine = new BorderLine();
        tableBorder.VerticalLine.OuterLineWidth = TABLE_LINE_WIDTH;
        tableBorder.IsVerticalLineValid = true;

        tableBorder.LeftLine = new BorderLine();
        tableBorder.LeftLine.OuterLineWidth = TABLE_LINE_WIDTH;
        tableBorder.IsLeftLineValid = true;

        tableBorder.RightLine = new BorderLine();
        tableBorder.RightLine.OuterLineWidth = TABLE_LINE_WIDTH;
        tableBorder.IsRightLineValid = true;

        tableBorder.TopLine = new BorderLine();
        tableBorder.TopLine.OuterLineWidth = TABLE_LINE_WIDTH;
        tableBorder.IsTopLineValid = true;

        tableBorder.BottomLine = new BorderLine();
        tableBorder.BottomLine.OuterLineWidth = TABLE_LINE_WIDTH;
        tableBorder.IsBottomLineValid = true;

        try {
            xTableProp.setPropertyValue("TableBorder", tableBorder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isNotToBeProcessed(XTextTable xTable) {
        XTextRange xTableRange = xTable.getAnchor();
        XPropertySet xTableRangeProp = UnoRuntime
                .queryInterface(XPropertySet.class, xTableRange);
        try {
            XTextSection xSection = UnoRuntime
                    .queryInterface(XTextSection.class,
                            xTableRangeProp.getPropertyValue("TextSection"));
            if (xSection == null)
                return false;

            XNamed xSectionName = UnoRuntime
                    .queryInterface(XNamed.class, xSection);
            return xSectionName.getName().startsWith("unproc-tbl:");
        }
        catch (Exception e) {
            return false;
        }
    }
}
