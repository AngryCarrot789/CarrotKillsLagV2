package carrot.ckl.command.helpers;

import carrot.ckl.tile.ItemDataPair;

public class ParsedValue<T> {
    private T value;
    private boolean failed;

    public ParsedValue(T value, boolean failed) {
        this.value = value;
        this.failed = failed;
    }

    public T value() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public boolean failed() {
        return this.failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public static ParsedValue<Integer> ParseInteger(String[] args, int index) {
        return ArgumentParser.ParseInteger(args, index);
    }

    public static ParsedValue<Double> ParseDouble(String[] args, int index) {
        return ArgumentParser.ParseDouble(args, index);
    }

    public static ParsedValue<String> ParseString(String[] args, int index) {
        return ArgumentParser.ParseString(args, index);
    }

    public static ParsedValue<Boolean> ParseBoolean(String[] args, int index) {
        return ArgumentParser.ParseBoolean(args, index);
    }

    public static ParsedValue<ItemDataPair> ParseItemData(String[] args, int index) {
        return ArgumentParser.ParseItemData(args, index);
    }
}
