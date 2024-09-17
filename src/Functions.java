import java.io.*;
import java.util.ArrayList;

public class Functions {

    private static ArrayList<String> highscoreArray = new ArrayList<String>();
    private static String playerName;
    private static int score;

    public static void setStats(String name, int points){
        playerName = name;
        score = points;
    }


    public static void main(String[] args) throws IOException {
        fileRead();
        highScoreComparison();
        updateHighScoreFile();
    }

    public static void fileRead() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("src/highScores.txt"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i < 10; i++){
            try {
                highscoreArray.add(reader.readLine());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void highScoreComparison(){
        for(int i = 0; i < highscoreArray.size(); i++){
            String highscore = highscoreArray.get(i);
            if (highscore != null && score >= Integer.parseInt(highscore.split(",")[1].trim())){
                String newScoreEntry = playerName + "," + score;
                if (highscoreArray.size() == 10) {
                    highscoreArray.remove(highscoreArray.size() - 1);
                }
                highscoreArray.add(i, newScoreEntry);
                break;
            }
        }
    }
    public static void updateHighScoreFile(){
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("src/highScores.txt");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (String highscore : highscoreArray) {
            writer.println(highscore);
        }
        writer.close();
    }

    public static String returnArrayList(){
        String returnString = "";
        String appendString = "";
        for(String line : highscoreArray){
            if(line == null){
                appendString = "---";
            } else appendString = line;
            returnString = returnString + appendString + "\n";
        }

        //String listString = highscoreArray.stream().map(Object::toString)
                //.collect(Collectors.joining(", "));

        return returnString;
    }
}
