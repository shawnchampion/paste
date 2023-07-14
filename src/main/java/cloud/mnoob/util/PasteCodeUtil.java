package cloud.mnoob.util;

import cloud.mnoob.config.PasteConfig;

import java.util.Random;

public class PasteCodeUtil {
    public static int getRandomCode() {
        int origin = (int) Math.pow(10, PasteConfig.getInstance().getCodeLength() - 1);
        return new Random().nextInt(origin, origin * 10);
    }
}
