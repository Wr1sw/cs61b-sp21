package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /** the created date **/
    private Date date;

    /** the father commit's SHA1 **/
    private List<String> fathers;

    /** key:file path    value:SHA1 **/
//    private Map<String, String> tracked;
    private TreeMap<String, String> tracked;

    /** SHA1 id **/
    private final String id;

    private final File file;

    public Commit(String message, TreeMap<String, String> trackedFilesMap, List<String> parents) {
        date = new Date();
        this.message = message;
        this.tracked = trackedFilesMap;
        this.fathers = parents;
        id = generateID();
        file = getFileByID(id);
    }

    public Commit() {
        date = new Date(0); // 0 -> 00:00:00 UTC, Thursday, 1 January 1970
        message = "initial commit";
        fathers = new ArrayList<>();
        tracked = new TreeMap<>();
        id = generateID();
        file = getFileByID(id);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getFathers() {
        return fathers;
    }

    public void setFathers(List<String> fathers) {
        this.fathers = fathers;
    }

    public Map<String, String> getTracked() {
        return tracked;
    }

    public void setTracked(TreeMap<String, String> tracked) {
        this.tracked = tracked;
    }

    public String getId() {
        return id;
    }

    public File getFile() {
        return file;
    }

    private File getFileByID(String id) {
        // take the first two char as the Dirname
        String dirName = getDirName(id);
        // The rest as the file name
        String fileName = getFileName(id);
        // generate a new directory under the OBJECTS_DIR, the directory name is dirName, Generate a new file called fileName
        File theFile = Utils.join(Repository.OBJECTS_DIR, dirName, fileName);
        return theFile;
    }

    private String getFileName(String id) {
        return id.substring(2);
    }

    private String getDirName(String id) {
        return id.substring(0, 2);
    }

    private String generateID() {
        return Utils.sha1(getTimestamp());
    }

    private String getTimestamp() {
        // 00:00:00 UTC, Thursday, 1 January 1970
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.CHINA);
        return dateFormat.format(date);
    }

    public void save() {
        File parentFile = file.getParentFile();
        if(!parentFile.exists()) {
            parentFile.mkdir();
        }
        // Save a commit for future use.
        Utils.writeObject(file, this);
    }
    public static void main(String[] args) {
        Commit commit = new Commit();
        System.out.println("commit.id = " + commit.file);
    }
}
