package PRISMS.AP_CS.Final_Project.src;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamEvaluator {

    private static class Champion {
        String name;
        String lane;
        String range;
        String damageType;
        String role;
        int picks;
        int bans;
        int presence;
        int wins;
        int losses;


        public Champion(String name, String lane, String range, String damageType, String role,
                        int picks, int bans, int presence, int wins, int losses) {
            this.name = name;
            this.lane = lane;
            this.range = range;
            this.damageType = damageType;
            this.role = role;
            this.picks = picks;
            this.bans = bans;
            this.presence = presence;
            this.wins = wins;
            this.losses = losses;
        }
    }

    private static List<Champion> readCSV(String filePath) {
        List<Champion> champions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] record = line.split(",");
                champions.add(new Champion(
                        record[0],
                        record[1],
                        record[2],
                        record[3],
                        record[4],
                        Integer.parseInt(record[5]),
                        Integer.parseInt(record[6]),
                        Integer.parseInt(record[7]),
                        Integer.parseInt(record[8]),
                        Integer.parseInt(record[9])
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return champions;
    }

    static int evaluateTeam(String[] team) {
        Map<String, Champion> championMap = new HashMap<>();
        List<Champion> champions = readCSV("PRISMS/AP_CS/Final_Project/src/Data.csv");
        for (Champion champ : champions) {
            championMap.put(champ.name, champ);
        }

        int score = 0;
        int totalPicks = 0;
        int totalBans = 0;
        int totalPresence = 0;
        int totalWins = 0;
        int totalLosses = 0;

        Map<String, Integer> lanes = new HashMap<>();
        Map<String, Integer> ranges = new HashMap<>();
        Map<String, Integer> damageTypes = new HashMap<>();
        Map<String, Integer> roles = new HashMap<>();

        for (String champName : team) {
            Champion champ = championMap.get(champName);
            if (champ == null) {
                continue;
            }

            totalPicks += champ.picks;
            totalBans += champ.bans;
            totalPresence += champ.presence;
            totalWins += champ.wins;
            totalLosses += champ.losses;

            lanes.put(champ.lane, lanes.getOrDefault(champ.lane, 0) + 1);
            ranges.put(champ.range, ranges.getOrDefault(champ.range, 0) + 1);
            damageTypes.put(champ.damageType, damageTypes.getOrDefault(champ.damageType, 0) + 1);
            roles.put(champ.role, roles.getOrDefault(champ.role, 0) + 1);
        }

        int maxPicks = 150;
        int maxBans = 100;
        int maxPresence = 200;
        int maxWins = 100;
        int maxLosses = 100;

        score += (totalPicks * 100) / maxPicks;
        score += (totalBans * 100) / maxBans;
        score += (totalPresence * 100) / maxPresence;
        score += (totalWins * 100) / maxWins;
        score -= (totalLosses * 100) / maxLosses;

        score *= 0.4;

        score += lanes.size() * 4;
        score += ranges.size() * 10;
        score += damageTypes.size() * 10;
        score += roles.size() * 4;

        score *= 0.6;

        score = Math.min(Math.max(score, 0), 100);

        return score;
    }
}
