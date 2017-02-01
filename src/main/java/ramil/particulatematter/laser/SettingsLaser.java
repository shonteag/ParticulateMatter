package ramil.particulatematter.laser;


public class SettingsLaser {

    public static float[] COLOR_RED = new float[]{1, 0, 0};
    public static float[] COLOR_GREEN = new float[]{0, 1, 0};
    public static float[] COLOR_BLUE = new float[]{0, 0, 1};
    public static float[] COLOR_WHITE = new float[]{1, 1, 1};

    // needs to be implemented
    public static float[] get_color_from_power(int power, int maxPower) {
        return new float[]{};
    }

    /*
    Base energy cost to fire the laser.
    Adjust this to make laser firing more or less expensive per fire.
     */
    public static int FIRE_COST = 100;

}
