package SpaceportSystem.UI;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;

/**
 * SpaceTheme - Futuristic space design system
 * Contains all colors, styles, and design constants
 */
public class SpaceTheme {
    
    // === COLOR PALETTE ===
    
    // Background colors - Deep space
    public static final String BG_DARK = "#0a0e27";
    public static final String BG_DARKER = "#05070f";
    public static final String BG_CARD = "#141b3d";
    public static final String BG_CARD_HOVER = "#1a2350";
    
    // Accent colors - Neon cyan/blue
    public static final String ACCENT_PRIMARY = "#00d9ff";
    public static final String ACCENT_SECONDARY = "#7b2ff7";
    public static final String ACCENT_SUCCESS = "#00ff88";
    public static final String ACCENT_WARNING = "#ffaa00";
    public static final String ACCENT_DANGER = "#ff3366";
    
    // Text colors
    public static final String TEXT_PRIMARY = "#ffffff";
    public static final String TEXT_SECONDARY = "#a0aec0";
    public static final String TEXT_MUTED = "#718096";
    
    // Border colors
    public static final String BORDER_SUBTLE = "#2d3748";
    public static final String BORDER_ACCENT = "#00d9ff";
    
    // === GRADIENTS ===
    
    public static final String GRADIENT_PRIMARY = 
        "linear-gradient(135deg, #667eea 0%, #764ba2 100%)";
    
    public static final String GRADIENT_CARD = 
        "linear-gradient(135deg, rgba(20, 27, 61, 0.9) 0%, rgba(10, 14, 39, 0.9) 100%)";
    
    public static final String GRADIENT_BUTTON = 
        "linear-gradient(135deg, #00d9ff 0%, #7b2ff7 100%)";
    
    public static final String GRADIENT_SUCCESS = 
        "linear-gradient(135deg, #00ff88 0%, #00d9ff 100%)";
    
    // === SHADOWS ===
    
    public static final String SHADOW_CARD = 
        "-fx-effect: dropshadow(gaussian, rgba(0, 217, 255, 0.3), 20, 0, 0, 0);";
    
    public static final String SHADOW_BUTTON = 
        "-fx-effect: dropshadow(gaussian, rgba(0, 217, 255, 0.5), 15, 0, 0, 0);";
    
    public static final String GLOW_EFFECT = 
        "-fx-effect: dropshadow(gaussian, rgba(0, 217, 255, 0.8), 25, 0, 0, 0);";
    
    // === SPACING ===
    
    public static final int SPACING_XS = 5;
    public static final int SPACING_SM = 10;
    public static final int SPACING_MD = 15;
    public static final int SPACING_LG = 20;
    public static final int SPACING_XL = 30;
    
    // === BORDER RADIUS ===
    
    public static final int RADIUS_SM = 8;
    public static final int RADIUS_MD = 12;
    public static final int RADIUS_LG = 16;
    public static final int RADIUS_XL = 24;
    
    // === COMMON STYLES ===
    
    public static String getCardStyle() {
        return "-fx-background-color: " + BG_CARD + ";" +
               "-fx-background-radius: " + RADIUS_LG + ";" +
               "-fx-border-color: " + BORDER_SUBTLE + ";" +
               "-fx-border-width: 1;" +
               "-fx-border-radius: " + RADIUS_LG + ";" +
               SHADOW_CARD;
    }
    
    public static String getButtonPrimaryStyle() {
        return "-fx-background-color: " + ACCENT_PRIMARY + ";" +
               "-fx-text-fill: " + BG_DARK + ";" +
               "-fx-font-weight: bold;" +
               "-fx-font-size: 14px;" +
               "-fx-background-radius: " + RADIUS_MD + ";" +
               "-fx-padding: 12 30 12 30;" +
               "-fx-cursor: hand;";
    }
    
    public static String getButtonSecondaryStyle() {
        return "-fx-background-color: transparent;" +
               "-fx-text-fill: " + ACCENT_PRIMARY + ";" +
               "-fx-font-weight: bold;" +
               "-fx-font-size: 14px;" +
               "-fx-border-color: " + ACCENT_PRIMARY + ";" +
               "-fx-border-width: 2;" +
               "-fx-border-radius: " + RADIUS_MD + ";" +
               "-fx-background-radius: " + RADIUS_MD + ";" +
               "-fx-padding: 12 30 12 30;" +
               "-fx-cursor: hand;";
    }
    
    public static String getButtonDangerStyle() {
        return "-fx-background-color: " + ACCENT_DANGER + ";" +
               "-fx-text-fill: white;" +
               "-fx-font-weight: bold;" +
               "-fx-font-size: 14px;" +
               "-fx-background-radius: " + RADIUS_MD + ";" +
               "-fx-padding: 12 30 12 30;" +
               "-fx-cursor: hand;";
    }
    
    public static String getTextFieldStyle() {
        return "-fx-background-color: " + BG_DARKER + ";" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";" +
               "-fx-prompt-text-fill: " + TEXT_MUTED + ";" +
               "-fx-border-color: " + BORDER_SUBTLE + ";" +
               "-fx-border-width: 1;" +
               "-fx-border-radius: " + RADIUS_SM + ";" +
               "-fx-background-radius: " + RADIUS_SM + ";" +
               "-fx-padding: 10;" +
               "-fx-font-size: 14px;";
    }
    
    public static String getTitleStyle(int size) {
        return "-fx-font-size: " + size + "px;" +
               "-fx-font-weight: bold;" +
               "-fx-text-fill: " + TEXT_PRIMARY + ";";
    }
    
    public static String getSubtitleStyle() {
        return "-fx-font-size: 16px;" +
               "-fx-text-fill: " + TEXT_SECONDARY + ";";
    }
    
    public static String getLabelStyle() {
        return "-fx-font-size: 14px;" +
               "-fx-text-fill: " + TEXT_SECONDARY + ";";
    }
    
    // Background style for main containers
    public static String getMainBackgroundStyle() {
        return "-fx-background-color: " + BG_DARK + ";";
    }
}
