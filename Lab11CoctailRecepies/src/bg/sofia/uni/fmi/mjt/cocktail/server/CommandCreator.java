package bg.sofia.uni.fmi.mjt.cocktail.server;

import java.util.ArrayList;
import java.util.List;

public class CommandCreator {
    private static List<String> getCommandArguments(String input) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();

        for (char c : input.toCharArray()) {
            if (c == '=') {
                tokens.add(sb.toString());
                sb.delete(0, sb.length());
            }

            if (c == ' ') { //when space is not inside quote split
                tokens.add(sb.toString()); //token is ready, lets add it to list
                sb.delete(0, sb.length()); //and reset StringBuilder`s content
            } else if ( c != '=') {
                sb.append(c); //else add character to token
            }
        }
        //lets not forget about last token that doesn't have space after it
        tokens.add(sb.toString());

        return tokens;
    }

    public static Command newCommand(String clientInput) {
        List<String> tokens = CommandCreator.getCommandArguments(clientInput);
        String[] args = tokens.subList(1, tokens.size()).toArray(new String[0]);

        if (tokens.get(0).equals("get") && tokens.get(1).equals("all")) {
            return new Command(tokens.get(0) + " " + tokens.get(1), args);
        } //get all command

        if (tokens.get(0).equals("get") && tokens.get(1).equals("by-name")) {
            return new Command(tokens.get(0) + " " + tokens.get(1),
                    tokens.subList(2, tokens.size()).toArray(new String[0]));
        } //get by-name command

        if (tokens.get(0).equals("get") && tokens.get(1).equals("by-ingredient")) {
            return new Command(tokens.get(0) + " " + tokens.get(1),
                    tokens.subList(2, tokens.size()).toArray(new String[0]));
        } //get by-ingredient command

        return new Command(tokens.get(0), args);
    }
}