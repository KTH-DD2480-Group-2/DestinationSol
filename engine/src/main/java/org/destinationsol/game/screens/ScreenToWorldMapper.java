package org.destinationsol.game.screens;
import com.badlogic.gdx.math.Vector2;

public class ScreenToWorldMapper {
    private ScreenToWorldMapper() {
    }

    private static final int PIXEL_TO_WORLD_UNIT_RATIO = 5;

    /**
     * Maps a click position on the screen to the world 
     * @param screenDimensions The screen dimensions of the game window
     * @param clickPosition The click position on the screen. Is a vector2 with values between 0 and 1
     * @param camPos The camera position in the world
     * @param camAngle The camera angle in the world
     * @return
     */
    public static Vector2 screenClickPositionToWorldPosition(Vector2 screenDimensions, Vector2 clickPosition, Vector2 camPos, float zoom) {
        float ratio = getScreenRatio(screenDimensions);
        clickPosition = matchClickPositionWithCameraZoom(clickPosition, zoom);
        return getCameraPositionOffsetToWorld(camPos, clickPosition, ratio, zoom);
    }

    /**
     * Getting the world position from a cam pos and a cam offset
     * 
     * @param camPos    The camera position in the world
     * @param camOffset The camera offset in the world
     * @param ratio     The screen ratio
     * @param zoom      The zoom of the world
     * @return The camera offset position in the world
     */
    private static Vector2 getCameraPositionOffsetToWorld(Vector2 camPos, Vector2 camOffset, float ratio, float zoom) {
        Vector2 finalPosition = new Vector2(camPos);
        finalPosition.add(camOffset);
        finalPosition.x -= (ratio * zoom) / (2.f / PIXEL_TO_WORLD_UNIT_RATIO);
        finalPosition.y -= (zoom) / (2.f / PIXEL_TO_WORLD_UNIT_RATIO);
        return finalPosition;
    }

    /**
     * Matches the click position with the world zoom
     * 
     * @param clickPosition The click position
     * @param zoom          The zoom level of the world
     */
    private static Vector2 matchClickPositionWithCameraZoom(Vector2 clickPosition, float zoom) {
        Vector2 clickPositionCopy = clickPosition.cpy();
        clickPositionCopy.scl(PIXEL_TO_WORLD_UNIT_RATIO);
        clickPositionCopy.scl(zoom);
        return clickPositionCopy;
    }

    /**
     * Get the screen ratio, (width / height)
     * 
     * @param screenDimensions The screen dimensions in any unit
     * @return The ratio between the width and height
     */
    private static float getScreenRatio(Vector2 screenDimensions) {
        float screenWidth = screenDimensions.x;
        float screenHeight = screenDimensions.y;
        return screenWidth / screenHeight;
    }
}
