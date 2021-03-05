package carrot.ckl.player.tables.base;

import carrot.ckl.logs.ChatFormatting;
import carrot.ckl.logs.ChatLogger;
import carrot.ckl.player.tables.PlayerResultsTable;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class ResultsSection {
    private int rowOffset;
    private String header;
    private final ArrayList<ResultRow> resultRows;

    public ResultsSection(String header) {
        setHeader(header);
        resultRows = new ArrayList<ResultRow>();
    }

    public void writeNumbered(ChatLogger logger) {
        if (resultsCount() < 1) {
            logger.LogInfo("There are no results in this section!");
            return;
        }

        logger.LogAny(ChatColor.GREEN + ChatFormatting.TitliseContent(ChatFormatting.Spacify(getHeader()), 56));
        for (int i = 0; i < resultRows.size(); i++) {
            ResultRow row = resultRows.get(i);
            row.write(logger, rowOffset + i + 1);
        }
        logger.LogAny(ChatColor.GREEN + ChatFormatting.Repeat('-', 50));
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void addRow(ResultRow resultRow) {
        resultRows.add(resultRow);
    }

    public void addRows(ArrayList<ResultRow> resultRows, int rowOffset) {
        for (int i = rowOffset; i < Math.min(rowOffset + PlayerResultsTable.ShowResultsAmount, resultRows.size()); i++) {
            addRow(resultRows.get(i));
        }
    }

    public int resultsCount() {
        return resultRows.size();
    }

    public void setRowOffset(int rowOffset) {
        this.rowOffset = rowOffset;
    }
}
