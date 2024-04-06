package ru.danilakondr.templater.processing;

import com.sun.star.beans.XPropertySet;
import com.sun.star.text.TextContentAnchorType;
import com.sun.star.text.XTextDocument;
import com.sun.star.uno.UnoRuntime;

import java.util.function.Consumer;

public class MathFormulaFixProcessor implements TextDocument.ObjectProcessor<Object> {
    @Override
    public void process(Object object, XTextDocument xDoc) {
        try {
            XPropertySet xFormulaObject = UnoRuntime
                    .queryInterface(XPropertySet.class, object);
            Object oFormula = xFormulaObject.getPropertyValue("Model");

            XPropertySet xPropertySet = UnoRuntime
                    .queryInterface(XPropertySet.class, oFormula);
            String sFormula = (String)xPropertySet.getPropertyValue("Formula");

            xPropertySet.setPropertyValue("Formula", StarMathFixer.fixFormula(sFormula));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
