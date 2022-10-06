import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    ArrayList<String> list = new ArrayList<>();
    String listItems;

    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            listItems = "[";

            if (!list.isEmpty())
                for (int i = 0; i < list.size(); i++) {
                    listItems += list.get(i) + ", ";
                }
            listItems += "]";

            return String.format(listItems
                    + "\nTry adding by doing \"/add?s=<something>\" or searching by doing /search?s=<something>");
        } else if (url.getPath().equals("/add")) {
            if (url.getQuery().isEmpty()) {
                return String.format("Input a valid query!");
            }

            String[] parameters = url.getQuery().split("=");
            list.add(parameters[1]);

            listItems = "[";
            for (int i = 0; i < list.size(); i++) {
                listItems += list.get(i) + ", ";
            }
            listItems += "]";

            return String.format(listItems + "\nAdded \"%s\" to the list", parameters[1]);
        } else if (url.getPath().equals("/search")) {
            if (url.getQuery().isEmpty()) {
                return String.format("Input a valid query!");
            }

            boolean found = false;
            String[] parameters = url.getQuery().split("=");
            String foundItems = "";

            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).contains(parameters[1])) {
                    foundItems += "\"" + list.get(i) + "\" contains \"" + parameters[1] + "\"\n";
                    found = true;
                }
            }
            if (!found) {
                return String.format("\"%s\" not found D:", parameters[1]);
            }

            return String.format(foundItems);
        } else {
            System.out.println("Path: " + url.getPath());
            String.format("\"" + url.getPath() + "\" Invalid");

            return "404 Not Found!";
        }
    }
}

class SearchEngine {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}