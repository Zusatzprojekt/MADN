import java.util.HashMap;
public class Spielfeld {
    // Instanzvariablen - ersetzen Sie das folgende Beispiel mit Ihren Variablen
    public int[] path_red;
    public int[] path_blue;
    public int[] path_yellow;
    public int[] path_green;
    private HashMap<String, int[]> positions;

    /**
     * Konstruktor für Objekte der Klasse Spielfeld
     */
    public Spielfeld() {
        // Instanzvariable initialisieren
        //Bei den letzen vier Zahlen handelt es sich jedes mal um die zielfelder
        path_red = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40,496,497,498,499};
        path_blue = new int[]{11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,196,197,198,199};
        path_yellow = new int[]{21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,296,297,298,299};
        path_green = new int[]{31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,396,397,398,399};
        positions = new HashMap<String, int[]>();
        positions.put("red", new int[]{5, 11});
        positions.put("blue", new int[]{12, 3});
        positions.put("yellow", new int[]{});
        positions.put("green", new int[]{});
    }

    public int[] get_positions(String farbe) {
        return positions.get(farbe);
    }

    public int pruefe_werfen(String farbe,int figur, int augenzahl) {
        int[] positions_farbe = positions.get(farbe);
        String[] farben = {"red", "blue", "yellow", "green"};
        for (String get_farbe : farben) {
            int[] currentPositions = positions.get(get_farbe);
            for (int position : positions_farbe) {
                for (int get_current_position : currentPositions) {
                    if (position + augenzahl == get_current_position) {
                        return get_current_position;
                    }
                }
            }
        }
        return 0;
    }

    private static int findIndex(int[] array, int target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1; // Element not found
    }


    public void werfen(int geworfenen, int augenzahl){
        String current_farbe ="red"; //testen
        String target_farbe = "blue"; //testen
        int position_source = geworfenen-augenzahl;
        int find_index_source = findIndex(get_positions(current_farbe), position_source);
        int find_index_target = findIndex(get_positions(target_farbe), geworfenen);
        int[] positions_source = positions.get(current_farbe);
        int[] positions_target = positions.get(target_farbe);
        positions_source[find_index_source] = geworfenen;


    }
}
