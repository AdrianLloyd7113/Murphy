public class MurphyBlock extends MurphyObject {

    private double w;
    private double h;
    private String data = "";
    private String style = "";

    MurphyBlock(String name) {
        super(name);
    }

    void addText(String toAdd){
        data = toAdd;
    }

    void giveBG(int r, int g, int b){
        style += "background-color: rgb(" + r + ", " +  g + ", " + b + ")\n";
    }

    void giveDimensions(int w, int h){

    }

    public String toHTML(){
        String html = "<div id='" + super.getName() + "'>";

        System.out.println("GENNED HTML");

        if (data != ""){
            html += "<p>" + data + "</p>";
        }

        html += "</div>";

        return html;
    }

    public String toCSS(){
        String css = "#" + getName() + " {\n";

        if (style != null){
            css += style;
        }

        css += "}";
        return css;
    }

}
