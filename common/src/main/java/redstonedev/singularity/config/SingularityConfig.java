package redstonedev.singularity.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import redstonedev.singularity.Singularity;

@Config(name = Singularity.MOD_ID)
public class SingularityConfig implements ConfigData {
    @ConfigEntry.Gui.CollapsibleObject
    public GeneralOptions generalOptions = new GeneralOptions();

    @ConfigEntry.Gui.CollapsibleObject
    public ClearOptions clearOptions = new ClearOptions();

    public static class GeneralOptions {
        // Radius for a singularity, in blocks
        public double singularityRadius = 3.0;

        // Minimum number of items for a singularity
        public int minimumSize = 2000;

        // Display the detection message for everyone
        public boolean displayPotentialsPublicly = false;
    }

    public static class ClearOptions {
        // Enable automatic deletion of singularities
        public boolean enableClearing = true;

        // Enable count display
        public boolean showCleared = true;

        // The interval in which singularities should be cleared, in seconds
        public int clearInterval = 600;

        // The minimum number of items to clear.
        public int minimumItems = 1000;
    }
}
