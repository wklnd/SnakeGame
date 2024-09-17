package testing;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class testing {
    private static final int score = 1;
    private static String playerName = "Berit";
    private static ArrayList<String> highscoreArray = new ArrayList<String>();


    public static void main(String[] args) throws IOException {
        fileRead();
        highScoreComparison();
        updateHighScoreFile();
    }

    private static void fileRead() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/highScores.txt"));
        for(int i = 0; i < 10; i++){
            highscoreArray.add(reader.readLine());
        }
    }

    private static void highScoreComparison(){
        for(int i = 0; i < highscoreArray.size(); i++){
            if(score >= Integer.parseInt(highscoreArray.get(i).split(",")[1].trim())){
                String newScoreEntry = playerName + "," + score;
                if (highscoreArray.size() == 10) {
                    highscoreArray.remove(highscoreArray.size() - 1);
                }
                highscoreArray.add(i, newScoreEntry);
                break;
            }
        }
    }

    private static void updateHighScoreFile() throws IOException {
        PrintWriter writer = new PrintWriter("src/highScores.txt");
        for (String highscore : highscoreArray) {
            writer.println(highscore);
        }
        writer.close();
    }
}

