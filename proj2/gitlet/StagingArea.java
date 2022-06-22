package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static gitlet.Utils.*;
/**
 * @author wr1sw
 * @version 1.0.0
 * @description
 */
public class StagingArea implements Serializable {
    private final Map<String, String> added = new HashMap<>();

    private final Set<String> removed = new HashSet<>();

    // transient key work can need not serialization
    private transient Map<String, String> tracked;


    // Get a StagingArea instance from the file INDEX
    public static StagingArea getInstance() {
        return readObject(Repository.INDEX, StagingArea.class);
    }

    // Save the instance to the file INDEX
    public void save() {
        writeObject(Repository.INDEX, this);
    }


    public Map<String, String> getAdded() {
        return added;
    }

    public Set<String> getRemoved() {
        return removed;
    }

    public Map<String, String> getTracked() {
        return tracked;
    }

    public void setTracked(Map<String, String> tracked) {
        this.tracked = tracked;
    }

    // clear the stagingArea
    public void clear() {
        added.clear();
        removed.clear();
    }

    public boolean add(File file) {
        String filePath = file.getPath();

        Blob blob = new Blob(file);
        String blobId = blob.getId();

        String trackedBlobId = tracked.get(filePath);
        if (trackedBlobId != null) {
            if (trackedBlobId.equals(blobId)) {
                if (added.remove(filePath) != null) {
                    return true;
                }
                return removed.remove(filePath);
            }
        }

        String prevBlobId = added.put(filePath, blobId);

        if (prevBlobId != null && prevBlobId.equals(blobId)) {
            return false;
        }

        if (!blob.getFile().exists()) {
            blob.save();
        }
        return true;
    }

    public static StagingArea fromFile() {
        return readObject(Repository.INDEX, StagingArea.class);
    }

    public Map<String, String> commit() {
        tracked.putAll(added);
        for (String path : removed) {
            tracked.remove(path);
        }
        clear();
        return tracked;
    }
    public boolean isClean() {
        return added.isEmpty() && removed.isEmpty();
    }

}
