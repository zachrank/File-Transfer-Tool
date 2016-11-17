import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by cthill on 10/31/16.
 */
public class Transfer {
    public static void main(String args[]) {
        List<String> argslist = Arrays.asList(args);

        if (argslist.size() < 1) {
            System.out.println("Usage: ...");
            System.exit(1);
        }

        boolean serverMode = argslist.contains("-s");
        if (serverMode) {
            try {
                int port = 9999;

                if (argslist.indexOf("-p") != -1) {
                    port = Integer.parseInt(argslist.get(argslist.indexOf("-p") + 1));
                }

                TransferServer ts = new TransferServer(port);
                System.out.println("Listening on port " + port);
                ts.serve();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String sourceFilename = "";
            String destFilename = "";
            String serverAddress = "";
            int port = 9999;

            boolean firstFile = false;

            // parse arguments
            for (int i = 0; i < argslist.size(); i++) {
                String arg = argslist.get(i);

                if (arg.startsWith("-")) {
                    //port flag
                    if (arg.equals("-p")) {
                        port = Integer.parseInt(argslist.get(++i));
                    }

                    //todo: implement flags for base64, encryption, etc
                } else {
                    //not flag
                    if (firstFile) {
                        sourceFilename = arg;
                    } else {
                        String[] split = arg.split(":");
                        serverAddress = split[0];
                        if (split.length > 1) {
                            destFilename = split[1];
                        } else {
                            destFilename = sourceFilename;
                        }
                    }
                }
            }

            try {
                // connect to server
                TransferClient tc = new TransferClient(serverAddress, port);
                // transfer file
                tc.transfer(sourceFilename, destFilename);
                // close connection
                tc.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}