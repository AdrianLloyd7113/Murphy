import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    static String htmlPath = "index.html";
    static String src = ";\n";
    static String html = "";
    static String css = "";

    static String bodyStyle = "";

    static int objIndex = 0;
    static String currStructName = "";

    static ArrayList<MurphyStructure> structures = new ArrayList<MurphyStructure>();

    static ArrayList<MurphyObject> obj = new ArrayList<MurphyObject>();
    static HashMap<String, Integer> objMap = new HashMap<String, Integer>();

    public static void main(String[] args) {
        System.out.println("Welcome to Murphy Compiler. Enter the number of source files, then provide source paths...");

        acceptFiles();

        parse();
        compile();

        System.out.println("\n\nMurphy is a good boy.\nWeb files successfully compiled.");
    }

    public static void acceptFiles(){
        Scanner scanner = new Scanner(System.in);
        int numberToAccept = Integer.parseInt(scanner.nextLine());
        for (int i = 0; i < numberToAccept; i++){
            readFile(scanner.nextLine());
        }
    }

    public static void readFile(String filePath){
        try {
            Scanner inFile = new Scanner(System.in);
            File source = new File(filePath);
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
        }
    }

    private static void readToken(String token) {
        token = token.trim();
        String[] terms = token.split(" ");
        String[] regDiv = token.split(">> ");
        if (terms[0].equals("BEGIN")){
            if (terms[1].equals("Visible") && terms[2].equals("Structure")){
                currStructName = terms[3];
            }
        }
        else if (terms[0].equals("display")){
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
            else if (terms[1].equals("Image")){
                html += "<img src='" + regDiv[1] + "' alt=" + regDiv[2] + ">";
            }
        }else if (terms[0].equals("create")) {
            if (terms[1].equals("TextBlock")) {
                TextBlock toAdd = new TextBlock(terms[2].replace(":", ""));
                String[] blockDiv = token.split(":");
                String[] atts = blockDiv[1].split("//");

                for (int i = 0; i < atts.length; i++) {
                    String attribute = atts[i].trim();

                    String[] attTokens = attribute.split(" ");

                    if (attTokens[0].equals("content")) {
                        if (attTokens[1].equals("Text")) {
                            String[] attDiv = attribute.split(">> ");
                            toAdd.addText(attDiv[1]);
                        }
                    } else if (attTokens[0].equals("bg-color") && attTokens[1].equals(">>")) {
                        toAdd.giveBG(Integer.parseInt(attTokens[2]), Integer.parseInt(attTokens[3]), Integer.parseInt(attTokens[4]));
                    } else if (attTokens[0].equals("text-size")) {
                        toAdd.setTextSize(Integer.parseInt(attTokens[2]));
                    } else if (attTokens[0].equals("new") && attTokens[1].equals("<<")) {
                        toAdd = (TextBlock) obj.get(objMap.get(attTokens[2]));
                        toAdd.setName(terms[2]);
                    } else if (attTokens[0].equals("shape")){
                        toAdd.giveShape(Integer.parseInt(attTokens[1]), Integer.parseInt(attTokens[2]));
                    } else if (attTokens[0].equals("font")){
                        toAdd.giveFont("" + attTokens[1]);
                    } else if (attTokens[0].equals("align-content")){
                        toAdd.giveTextAlign("" + attTokens[1]);
                    } else if (attTokens[0].equals("align")){
                        toAdd.giveBlockAlign(attTokens[1]);
                    }
                }

                obj.add(toAdd);
                objMap.put(terms[2], obj.size() - 1);
            }
            else if (terms[1].equals("ImageBlock")){
                ImageBlock toAdd = new ImageBlock(terms[2]);
                String[] blockDiv = token.split(":");
                String[] atts = blockDiv[1].split("//");

                for (int i = 0; i < atts.length; i++) {
                    String attribute = atts[i].trim();

                    String[] attTokens = attribute.split(" ");

                    if (attTokens[0].equals("source")){
                        toAdd.giveSrc(attTokens[2]);
                    } else if (attTokens[0].equals("shape")){
                        toAdd.giveShape(Integer.parseInt(attTokens[1]), Integer.parseInt(attTokens[2]));
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
        }else if (terms[0].equals("##")){

        }
        else if (terms[0].equals("modify")){
            MurphyBlock toMod = new MurphyBlock("");
            String[] blockDiv = token.split(":");
            String[] atts = blockDiv[1].split("//");

            int type = ((MurphyBlock) obj.get(objMap.get(terms[1]))).getType();

            if (type == 1){
                TextBlock textMod = (TextBlock) obj.get(objMap.get(terms[1]));

                for (int i = 0; i < atts.length; i++) {
                    String attribute = atts[i].trim();

                    String[] attTokens = attribute.split(" ");

                    if (attTokens[0].equals("content")) {

                        if (attTokens[1].equals("Text")) {
                            System.out.println("Got here");
                            String[] attDiv = attribute.split(">> ");
                            System.out.println(attDiv[1]);
                            textMod.addText(attDiv[1]);
                        }
                    } else if (attTokens[0].equals("bg-color") && attTokens[1].equals(">>")) {
                        textMod.giveBG(Integer.parseInt(attTokens[2]), Integer.parseInt(attTokens[3]), Integer.parseInt(attTokens[4]));
                    } else if (attTokens[0].equals("new") && attTokens[1].equals("<<")) {
                        textMod = (TextBlock) obj.get(objMap.get(attTokens[2]));
                        textMod.setName(terms[2]);
                    } else if (attTokens[0].equals("shape")){
                        textMod.giveShape(Integer.parseInt(attTokens[1]), Integer.parseInt(attTokens[2]));
                    }
                }
                toMod = textMod;
            }else if (type == 2){
                ImageBlock imgMod = (ImageBlock) obj.get(objMap.get(terms[1]));

                for (int i = 0; i < atts.length; i++) {
                    String attribute = atts[i].trim();

                    String[] attTokens = attribute.split(" ");

                    if (attTokens[0].equals("source")){
                        imgMod.giveSrc(attTokens[2]);
                    } else if (attTokens[0].equals("shape")){
                        imgMod.giveShape(Integer.parseInt(attTokens[1]), Integer.parseInt(attTokens[2]));
                    }
                }

                toMod = imgMod;
            }

            obj.set(objMap.get(terms[1]), toMod);
            System.out.println(toMod.getHtml());

            //TODO: Figure out why text isn't modifying

            String oldHTML, oldCSS;
            oldHTML = toMod.getHtml();
            oldCSS = toMod.getCSS();

            html = html.replace(oldHTML, toMod.getHtml());
            css = css.replace(oldCSS, toMod.toCSS());

        }
        else if (terms[0].equals("PAGE-STYLE")){
            bodyStyle = "";
            String[] blockDiv = token.split(":");
            String[] atts = blockDiv[1].split("//");

            for (int i = 0; i < atts.length; i++) {
                String attribute = atts[i].trim();
                String[] attTokens = attribute.split(" ");

                System.out.println("token " + attTokens.length);
                System.out.println(attTokens[0]);
                if (attTokens[0].equals("bg-color")){
                    bodyStyle += "background-color:rgb(" + attTokens[2] + ", " +  attTokens[3] + ", " + attTokens[4] + ");";
                }
            }
        }
        else if (terms[0].equals("END") && terms[1].equals(currStructName)){
            MurphyStructure struct = new MurphyStructure(currStructName, obj, objMap);
            structures.add(struct);

            currStructName = "";
        }else{
            System.out.println("Murphy encountered a syntax error when compiling.\nUNKNOWN COMMAND: " + terms[0]);
        }
        genObjs();
    }

    private static void genObjs() {
        for (int i = objIndex; i < obj.size(); i++){
            html += obj.get(i).toHTML() + "\n";
            css += obj.get(i).toCSS() + "\n";
        }
        objIndex = obj.size();
    }

    public static void compile(){

        if (!css.equals("")){
            html += "<link rel=\"stylesheet\" href=\"main.css\">";
        }

        html = html.replace("\n", "");
        html = html.replace("  ", "");
        html = "<body style='" + bodyStyle + "'>" + html + "</body>";

        //HTML Generation

        try {
            File htmlFile = new File("examples/index.html");
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
            FileWriter htmlWriter = new FileWriter("examples/index.html");
            htmlWriter.write(html);
            htmlWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        //CSS Generation

        try {
            File htmlFile = new File("examples/main.css");
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
            FileWriter htmlWriter = new FileWriter("examples/main.css");
            htmlWriter.write(css);
            htmlWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

}
