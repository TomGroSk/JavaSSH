package pl.ssh.java.repository;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.table.CloudTable;
import com.microsoft.azure.storage.table.CloudTableClient;
import com.microsoft.azure.storage.table.TableOperation;
import com.microsoft.azure.storage.table.TableQuery;
import org.springframework.stereotype.Repository;
import pl.ssh.java.entity.Comment;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public class CommentsRepository {
    public CloudTable cloudTable = null;

    public CommentsRepository() {
        CloudStorageAccount storageAccount = null;
        try {
            storageAccount = CloudStorageAccount.parse("");
        } catch (URISyntaxException | InvalidKeyException e) {
            e.printStackTrace();
        }
        CloudTableClient tableClient = storageAccount.createCloudTableClient();
        try {
            cloudTable = tableClient.getTableReference("comments");
        } catch (URISyntaxException | StorageException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Comment> getCommentsByParentId(UUID parent) {
        String filter = TableQuery.generateFilterCondition(
                "PartitionKey",
                TableQuery.QueryComparisons.EQUAL,
                parent.toString()
        );

        TableQuery<Comment> partitionQuery = TableQuery.from(Comment.class).where(filter);

        var comments = cloudTable.execute(partitionQuery);

        return StreamSupport
                .stream(comments.spliterator(), false).collect(Collectors.toCollection(ArrayList::new));
    }

    public Comment getComment(UUID parentId, UUID commentId) {
        TableOperation retrieve = TableOperation.retrieve(parentId.toString(), commentId.toString(), Comment.class);
        try {
            return cloudTable.execute(retrieve).getResultAsType();
        } catch (StorageException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Comment comment) {
        TableOperation replace = TableOperation.replace(comment);
        try {
            cloudTable.execute(replace);
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    public void create(Comment comment) {
        TableOperation insert = TableOperation.insert(comment);
        try {
            cloudTable.execute(insert);
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }

    public void delete(Comment comment) {
        TableOperation delete = TableOperation.delete(comment);
        try {
            cloudTable.execute(delete);
        } catch (StorageException e) {
            e.printStackTrace();
        }
    }
}
