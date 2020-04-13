package app.grand.ophthalmicadmin.base;

import android.graphics.drawable.GradientDrawable;

public class CustomDrawable extends GradientDrawable {
    public CustomDrawable(int pStartColor, int pCenterColor, int pEndColor, int pStrokeWidth, int pStrokeColor, float cornerRadius) {
        super(Orientation.BOTTOM_TOP, new int[]{pStartColor, pCenterColor, pEndColor});
        setStroke(pStrokeWidth, pStrokeColor);
        setShape(GradientDrawable.OVAL);
        setCornerRadius(cornerRadius);
    }
}
