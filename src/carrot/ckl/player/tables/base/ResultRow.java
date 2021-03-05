package carrot.ckl.player.tables.base;

import carrot.ckl.logs.ChatLogger;
import org.bukkit.ChatColor;

public class ResultRow {
    private String content;

    public ResultRow(String content) {
        setContent(content);
    }

    public ResultRow() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void write(ChatLogger logger, int row) {
        String content = getContent();
        if (content != null) {
            logger.LogAny("" + ChatColor.GREEN + row + " - " + content);
        }
    }

    @Override
    public int hashCode() {
        return this.content.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        ResultRow row = (ResultRow) obj;
        return row.getContent().equals(this.content);
    }
}
