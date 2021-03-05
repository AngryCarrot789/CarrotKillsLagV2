package carrot.ckl.player.tables;

import carrot.ckl.helpers.MathsHelper;
import carrot.ckl.logs.ChatLogger;
import carrot.ckl.player.tables.base.ResultRow;
import carrot.ckl.player.tables.base.ResultsSection;
import org.bukkit.ChatColor;

import java.util.ArrayList;

public class ResultsCollection {
    private String header;
    private final ArrayList<ResultRow> resultRows;

    public ResultsCollection() {
        this.resultRows = new ArrayList<ResultRow>();
    }

    public void updateHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void addRow(ResultRow resultRow) {
        resultRows.add(resultRow);
    }

    public void displaySavedAndWrite(ChatLogger logger) {
        if (resultsCount() > 0) {
            logger.LogInfo("Results saved, " +
                           "Use " + ChatColor.GREEN + " /ckl results show <page> " +
                           ChatColor.GOLD + " to view them");
            getPage(0).writeNumbered(logger);
        }
    }

    /**
     * Creates a section of rows based on the given page index. The section contains references to the original rows, so they can be edited
     * @param pageIndex The zero-based page number
     * @return A section of rows based on the page number
     */
    public ResultsSection getPage(int pageIndex) {
        ResultsSection section = new ResultsSection(getHeader());
        int startIndex = pageIndex * PlayerResultsTable.ShowResultsAmount;
        for (int i = startIndex; i < Math.min(startIndex + PlayerResultsTable.ShowResultsAmount, resultRows.size()); i++) {
            section.addRow(resultRows.get(i));
        }
        section.setRowOffset(startIndex);
        return section;
    }


    /**
     * Creates a list of all "sections" (or pages). These sections have references to the original rows
     * @return A list of sections
     */
    public ArrayList<ResultsSection> getSectionCollection() {
        ArrayList<ResultsSection> sections = new ArrayList<ResultsSection>();
        for (int i = 0; i < resultRows.size(); i += PlayerResultsTable.ShowResultsAmount) {
            ResultsSection section = new ResultsSection(header);
            section.addRows(resultRows, i);
            sections.add(section);
        }
        return sections;
    }

    public void removeResultsRange(int startIndex, int endIndex) {
        resultRows.subList(startIndex, Math.min(endIndex, resultsCount() - 1)).clear();
        //int beforePage = (startIndex == 0) ? 0 : (startIndex - 1);
        //int end = Math.min(endIndex, resultsCount() - 1);
        //if (end < 1 || end <= startIndex)
        //    return;
        //ArrayList<ResultRow> remaining = new ArrayList<ResultRow>(resultRows.size() - (end - startIndex));
        //for(int i = 0; i < beforePage; i++) {
        //    remaining.add(resultRows.get(i));
        //}
        //for (int i = end + 1; i < resultRows.size(); i++) {
        //    remaining.add(resultRows.get(i));
        //}
        //resultRows.clear();
        //resultRows.addAll(remaining);
    }

    public void removePage(int pageIndex) {
        int startIndex = pageIndex * PlayerResultsTable.ShowResultsAmount;
        removeResultsRange(startIndex, startIndex + PlayerResultsTable.ShowResultsAmount);
    }

    public ArrayList<ResultRow> getRows() {
        return resultRows;
    }

    public int resultsCount() {
        return resultRows.size();
    }

    public void clearResults() {
        this.resultRows.clear();
    }
}
