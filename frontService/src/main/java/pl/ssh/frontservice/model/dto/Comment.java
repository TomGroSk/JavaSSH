package pl.ssh.frontservice.model.dto;

import java.util.Date;
import java.util.UUID;

public class Comment {
    public UUID partitionKey;
    public UUID rowKey;
    public Date timestamp;
    public String content;
    public String author;

    public UUID getPartitionKey() {
        return partitionKey;
    }

    public void setPartitionKey(UUID partitionKey) {
        this.partitionKey = partitionKey;
    }

    public UUID getRowKey() {
        return rowKey;
    }

    public void setRowKey(UUID rowKey) {
        this.rowKey = rowKey;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
