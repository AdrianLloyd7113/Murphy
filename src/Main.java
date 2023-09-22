import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    static String src = ";\n";
    static String html = "";
    static String css = "";

    static String currStructName = "";

    static ArrayList<MurphyStructure> structures = new ArrayList<MurphyStructure>();

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
            src = src.replace("\t", "");
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
        token = token.trim();
        String[] terms = token.split(" ");
        String[] regDiv = token.split(">> ");
        if (terms[0].equals("BEGIN")){
            if (terms[1].equals("Visible") && terms[2].equals("Structure")){
                currStructName = terms[3];
            }
        }else if (terms[0].equals("display")){
            if (terms[1].equals("Text")){

                String toDisplay = regDiv[1];
                String[] words = toDisplay.split(" ");

                for (int i = 0; i < words.length; i++){
                    if (words[i].charAt(0) == '%'){
                        MurphyVariable var = (MurphyVariable) obj.get(objMap.get(words[i].replace("%", "")));
                        toDisplay = toDisplay.replace(words[i], var.getValueAsString());
                    }
                }

                html += "<p>" + toDisplay + "</p>\n";
            }
        }else if (terms[0].equals("create")) {
            if (terms[1].equals("TextBlock")) {
                TextBlock toAdd = new TextBlock(terms[2]);
                String[] blockDiv = token.split(":");
                String[] atts = blockDiv[1].split("//");

                for (int i = 0; i < atts.length; i++) {
                    String attribute = atts[i].trim();
                    System.out.println("attribute");

                    String[] attTokens = attribute.split(" ");
                    System.out.println(attTokens[0]);

                    if (attTokens[0].equals("display")) {
                        if (attTokens[1].equals("Text")) {
                            System.out.println("Found text");
                            String[] attDiv = attribute.split(">> ");
                            toAdd.addText(attDiv[1]);
                        }
                    } else if (attTokens[0].equals("bg-color") && attTokens[1].equals(">>")) {
                        toAdd.giveBG(Integer.parseInt(attTokens[2]), Integer.parseInt(attTokens[3]), Integer.parseInt(attTokens[4]));
                    } else if (attTokens[0].equals("text-size")) {
                        toAdd.setTextSize(Integer.parseInt(attTokens[2]));
                    } else if (attTokens[0].equals("copy-of") && attTokens[1].equals("<<")) {
                        toAdd = (TextBlock) obj.get(objMap.get(attTokens[2]));
                        toAdd.setName(terms[2]);
                    }
                }

                obj.add(toAdd);
                objMap.put(terms[2], obj.size() - 1);
            }
        }else if (terms[0].equals("define")){
            if (terms[1].equals("Integer") && terms[3].equals(">>")){
                MurphyInteger newInt = new MurphyInteger(terms[2], Integer.parseInt(terms[4]));
                obj.add(newInt);
                objMap.put(terms[2], obj.size() - 1);
            }
        }else if (terms[0].equals("END") && terms[1].equals(currStructName)){
            MurphyStructure struct = new MurphyStructure(currStructName, obj, objMap);
            structures.add(struct);

            currStructName = "";
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
