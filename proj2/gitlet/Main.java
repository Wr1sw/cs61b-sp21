package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                Repository.setupPersistence();
                break;
            case "add":
                new Repository().add(args[1]);
                break;
            case "commit":
                String message = args[1];
                if (message.length() == 0) {
                    System.out.println("Please enter a commit message.");
                    System.exit(0);
                }
                new Repository().commit(message);
                break;
        }
    }
}
