package pl.ssh.java.entity;

import com.microsoft.azure.storage.table.TableServiceEntity;

public class Comment extends TableServiceEntity {
    private String content;
    private String author;

    public String getPartitionKey() {
        return partitionKey;
    }

    public String getRowKey() {
        return rowKey;
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
