package me.rayzr522.itemframegiver.command;

/**
 * Created by Rayzr522 on 6/10/17.
 */
public class CommandException extends RuntimeException {
    private final String key;

    public CommandException(String key) {
        super();
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
