package carrot.ckl.command.helpers;

import carrot.ckl.tile.ItemDataPair;

import java.util.ArrayList;
import java.util.Arrays;

// a null-safe class used for parsing commands and stuff.
// never directly returns null, and never throws exceptions
public final class ArgumentParser {
    /** Returns true if the index is too big, meaning the array is too small. True == Out of range
     * @param array the args array
     * @param smallestIndexRequired The length - 1, aka the max length required to index without worry... sort of
     * @return If index is out of range
     */
    public static boolean ArgsTooSmall(Object[] array, int smallestIndexRequired){
        if (array == null){
            return false;
        }
        return array.length <= smallestIndexRequired;
    }

    public static ParsedValue<ItemDataPair> ParseItemData(String[] args, int index) {
        ParsedValue<String> itemData = ParseString(args, index);
        if (itemData.failed())
            return new ParsedValue<ItemDataPair>(null, true);
        String[] data = new String[2];
        if (itemData.value().contains(":")) {
            data = itemData.value().split(":");
        }
        else {
            data[0] = itemData.value();
            data[1] = "-1";
        }
        try {
            return new ParsedValue<ItemDataPair>(new ItemDataPair(Integer.parseInt(data[0]), Byte.parseByte(data[1])), false);
        }
        catch (Exception e) {
            return new ParsedValue<ItemDataPair>(null, true);
        }
    }

    public static ParsedValue<String> ParseString(String[] args, int index) {
        if (ArgsTooSmall(args, index)) {
            return new ParsedValue<String>("", true);
        }
        return new ParsedValue<String>(args[index], false);
    }

    public static ParsedValue<Boolean> ParseBoolean(String[] args, int index) {
        if (ArgsTooSmall(args, index)) {
            return new ParsedValue<Boolean>(false, true);
        }
        String content = args[index];
        if (content.equalsIgnoreCase("true") || content.equalsIgnoreCase("t")) {
            return new ParsedValue<Boolean>(true, false);
        }
        else {
            return new ParsedValue<Boolean>(false, false);
        }
    }

    public static ParsedValue<Integer> ParseInteger(String[] args, int index){
        try{
            if (ArgsTooSmall(args, index)) {
                return new ParsedValue<Integer>(0, true);
            }
            return new ParsedValue<Integer>(Integer.parseInt(args[index]), false);
        }
        catch (Exception e){
            return new ParsedValue<Integer>(0, true);
        }
    }

    public static ParsedValue<Integer> ParseIntegerFast(String[] args, int index) {
        try {
            return new ParsedValue<Integer>(Integer.parseInt(args[index]), false);
        }
        catch (Exception e) {
            return new ParsedValue<Integer>(0, true);
        }
    }
    public static ParsedValue<Double> ParseDouble(String[] args, int index){
        try{
            if (ArgsTooSmall(args, index)) {
                return new ParsedValue<Double>(0.0d, true);
            }
            return new ParsedValue<Double>(Double.parseDouble(args[index]), false);
        }
        catch (Exception e){
            return new ParsedValue<Double>(0.0d, true);
        }
    }

    public static String GetCommand(String[] fullArgs) {
        if (ArgsTooSmall(fullArgs, 0)){
            return "command_missing";
        }
        return fullArgs[0];
    }

    public static ArrayList<String> GetCommandArgs(String[] fullArgs){
        if (fullArgs == null || fullArgs.length == 0){
            return new ArrayList<String>();
        }
        ArrayList<String> args = new ArrayList<String>(Arrays.asList(fullArgs));
        args.remove(0);
        return args;
    }

    public static ArrayList<String> GetSubCommandArgs(String[] commandArgs) {
        if (commandArgs == null || commandArgs.length == 1) {
            return new ArrayList<String>();
        }
        ArrayList<String> args = new ArrayList<String>(Arrays.asList(commandArgs));
        args.remove(0);
        return args;
    }

    public static String[] ToArray(ArrayList<String> arr){
        return arr.toArray(new String[0]);
    }
}
