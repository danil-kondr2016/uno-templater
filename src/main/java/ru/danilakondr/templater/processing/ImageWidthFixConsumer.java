package ru.danilakondr.templater.processing;

import com.sun.star.awt.Size;
import com.sun.star.beans.XPropertySet;
import com.sun.star.uno.UnoRuntime;

import java.util.function.Consumer;

public class ImageWidthFixConsumer implements Consumer<Object> {
    @Override
    public void accept(Object oImage) {
        try {
            XPropertySet xImage = UnoRuntime
                    .queryInterface(XPropertySet.class, oImage);

            Size actualSize = (Size) xImage.getPropertyValue("ActualSize");

            if (actualSize.Width > 16500) {
                long height = 16500L * actualSize.Height / actualSize.Width;

                if (height <= 27700L) {
                    xImage.setPropertyValue("Width", 16500);
                    xImage.setPropertyValue("Height", Long.valueOf(height).intValue());
                } else {
                    long width = 27700L * actualSize.Width / actualSize.Height;

                    xImage.setPropertyValue("Width", Long.valueOf(width).intValue());
                    xImage.setPropertyValue("Height", 27700);
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}