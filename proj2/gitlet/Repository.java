package gitlet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static gitlet.Utils.*;
import static java.lang.System.exit;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author wr1sw
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The object directory */
    public static final File OBJECTS_DIR = join(GITLET_DIR, "objects");
    /** the refs directory */
    private static final File REFS_DIR = join(GITLET_DIR, "refs");
    /** The heads directory */
    public static final File HEADS_DIR = join(REFS_DIR, "heads");
    /** The HEAD file */
    public static final File HEAD = join(GITLET_DIR, "HEAD");
    /** The HEAD branch prefix */
    private static final String HEAD_BRANCH_REF_PREFIX = "ref: refs/heads/";
    /** The default branch name */
    public static final String DEFAULT_BRANCH = "master";

    public static final File INDEX = join(GITLET_DIR, "index");


    private final StagingArea stagingArea;


    public Repository() {
        if (INDEX.exists()) {
            System.out.println("in...");
            stagingArea = StagingArea.getInstance();
        } else {
            stagingArea = new StagingArea();
        }
    }


    public static void setupPersistence() {
        if (GITLET_DIR.exists()) {
            Utils.message("A Gitlet version-control system already exists in the current directory.");
            exit(0);
        } else {
            GITLET_DIR.mkdir();
            REFS_DIR.mkdir();
            HEADS_DIR.mkdir();
            OBJECTS_DIR.mkdir();

            // Initialize.
            setCurrentBranch(DEFAULT_BRANCH);//set HEAD point to the branch (default master)
            FirstCommit();// first commit
        }

    }

    private static void setCurrentBranch(String default_branch) {
        Utils.writeObject(HEAD,HEAD_BRANCH_REF_PREFIX+default_branch);
    }

    private static void FirstCommit() {
        Commit initCommit = new Commit();
        initCommit.save();
        setBranch(DEFAULT_BRANCH, initCommit.getId());// master:id
    }

    private static void setBranch(String defaultBranch, String id) {
        File file = getBranchHeadFile(defaultBranch);
        setBranchCommit(file, id);
    }

    private static void setBranchCommit(File file, String id) {
        writeContents(file, id); // master file : id
    }

    private static File getBranchHeadFile(String defaultBranch) {
        return join(HEADS_DIR, defaultBranch); // return the file of master
    }

    public void add(String fileName) {
        File in = join(CWD, fileName);
        // File does not exist
        if (!in.exists()) {
            System.out.println("File does not exist");
            System.exit(0);
        }
        if (stagingArea.add(in)) {
            stagingArea.save();
        }
    }


//    public static void commit(String message) {
//        stageForAddition = readObject(STAGE_FOR_ADD, TreeMap.class);
//        stageForRemoval = readObject(STAGE_FOR_REMOVE, TreeMap.class);
//
//        String currentBranchName = plainFilenamesIn(HEADS_DIR).get(0);
//
//        if (stageForAddition.isEmpty() && stageForRemoval.isEmpty()) {
//            System.out.println("No changes added to the commit.");
//            System.exit(0);
//        }
//
//        if (message.isBlank()) {
//            System.out.println("Please enter a commit message.");
//            System.exit(0);
//        }
//
//        Commit commit = new Commit(message, stageForAddition, null);
//        Commit head = readObject(join(HEADS_DIR, currentBranchName), Commit.class);
//
//        for (Map.Entry<String, String> entry : head.getTracked().entrySet()) {
//            if (!commit.getTracked().containsValue(entry.getValue())) {
//                commit.getTracked().put(entry.getKey(), entry.getValue());
//            }
//        }
//        // stageForRemove is not empty
//        if (!stageForRemoval.isEmpty()) {
//            for (Map.Entry<String, String> entry : stageForRemoval.entrySet()) {
//                commit.getTracked().remove(entry.getKey());
//            }
//        }
//        // set fathers
//        List<String> father = new ArrayList<>();
//        father.add(head.getId());
//        commit.setFathers(father);
//
//        head = commit;
//
//        writeObject(join(HEADS_DIR, currentBranchName), head);
//
//        // update branch
////        writeObject(join(BRAN));
//
//        String shaId = commit.getId();
//        // take the first two char as the Dirname
//        String dirName = shaId.substring(0, 2);
//        // The rest as the file name
//        String fileName = shaId.substring(2);
//        // generate a new directory under the OBJECTS_DIR, the directory name is dirName, Generate a new file called fileName
//        File theFile = Utils.join(Repository.OBJECTS_DIR, dirName, fileName);
//        writeObject(theFile, commit);
//
//        // clear stage area
//        stageForAddition.clear();
//        writeObject(STAGE_FOR_ADD, stageForAddition);
//        stageForRemoval.clear();
//        writeObject(STAGE_FOR_REMOVE, stageForRemoval);
//    }
}
