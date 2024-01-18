//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.util.ArrayList;

public class Utils {
    public Utils() {
    }

    public static boolean ALContains(ArrayList<Location> visited, Location l) {
        for(int i = 0; i < visited.size(); ++i) {
            if (((Location)visited.get(i)).equals(l)) {
                return true;
            }
        }

        return false;
    }

    public static ArrayList<Location> ALCopy(ArrayList<Location> one) {
        ArrayList<Location> two = new ArrayList();

        for(int i = 0; i < one.size(); ++i) {
            two.add(new Location(((Location)one.get(i)).x, ((Location)one.get(i)).y));
        }

        return two;
    }
}
