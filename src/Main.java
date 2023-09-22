import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    static String src = ";\n";
    static String html = "";
    static String css = "";

    static ArrayList<MurphyObject> obj = new ArrayList<MurphyObject>();
    static HashMap<String, Integer> objMap = new HashMap<String, Integer>();

    public static void main(String[] args) {
        readFile();
        parse();
        genObjs();
        compile();
    }


    public static void readFile(){
        try {
            Scanner inFile = new Scanner(System.in);
            File source = new File(inFile.nextLine());
            Scanner scanner = new Scanner(source);

            while (scanner.hasNextLine()) {
                src += scanner.nextLine() + "\n";
            }

            src = src.replace("\n", "");
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void parse(){
        String[] tokens = src.split(";");
        for (int i = 0; i < tokens.length; i++){
            readToken(tokens[i]);
            System.out.println(i);
        }
    }

    private static void readToken(String token) {
        System.out.println(token);
        String[] terms = token.split(" ");
        String[] regDiv = token.split(">> ");
        if (terms[0].equals("display")){
            if (terms[1].equals("Text")){
                html += "<p>" + regDiv[1] + "</p>\n";
            }
        }else if (terms[0].equals("create")){
            if (terms[1].equals("TextBlock")){
                TextBlock toAdd = new TextBlock(terms[2]);
                String[] blockDiv = token.split(":");

                String[] atts = blockDiv[1].split("//");

                for (int i = 0; i < atts.length; i++){
                    System.out.println("attribute");
                    String[] attTokens = atts[i].split(" ");
                    System.out.println(attTokens[0]);
                    if (attTokens[0].equals("display")){
                        if (attTokens[1].equals("Text")){
                            System.out.println("FOund text");
                            String[] attDiv = atts[i].split(">> ");
                            toAdd.addText(attDiv[1]);
                        }
                    } else if (attTokens[0].equals("bgcolor")){
                        toAdd.giveBG(Integer.parseInt(attTokens[1]), Integer.parseInt(attTokens[2]), Integer.parseInt(attTokens[3]));
                    }
                }

                obj.add(toAdd);
                objMap.put(terms[2], obj.size() - 1);
            }
        }
    }

    private static void genObjs() {
        System.out.println("\n\nOBJECTS: \n");
        for (int i = 0; i < obj.size(); i++){
            System.out.println("obj number" + i);
            html += obj.get(i).toHTML() + "\n";
            css += obj.get(i).toCSS() + "\n";
        }
    }

    public static void compile(){

        if (!css.equals("")){
            html += "<link rel=\"stylesheet\" href=\"main.css\">";
        }

        html = html.replace("\n", "");
        html = html.replace("  ", "");

        //HTML Generation

        try {
            File htmlFile = new File("index.html");
            if (htmlFile.createNewFile()) {
                System.out.println("HTML generated: " + htmlFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter htmlWriter = new FileWriter("index.html");
            htmlWriter.write(html);
            htmlWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //CSS Generation

        try {
            File htmlFile = new File("main.css");
            if (htmlFile.createNewFile()) {
                System.out.println("CSS generated: " + htmlFile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        try {
            FileWriter htmlWriter = new FileWriter("main.css");
            htmlWriter.write(css);
            htmlWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
