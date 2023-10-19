public class MurphyBlock extends MurphyObject {

    private String html;
    private String css;
    private double w;
    private double h;
    private String data = "";
    String imgSrc = "";
    String style = "";

    int type = 0;

    MurphyBlock(String name) {
        super(name);
    }

    void addText(String toAdd){
        data = toAdd;
    }

    void giveBG(int r, int g, int b){
        style += "background-color: rgb(" + r + ", " +  g + ", " + b + ");\n";
    }

    void giveShape(int w, int h){

        style += "width: " + w + ";\n" + "height: " + h + ";\n" + "max-width: " + w + ";\n" + "max-height: " + h + ";\n";

    }

    void giveBlockAlign(String align){
        if (align.equals("center")){
            style += "margin: auto;\n";
        }
    }

    public int getType() {
        return type;
    }

    public String toHTML(){
        String html = "<div id='" + super.getName() + "'>";

        if (data != ""){
            html += "<p>" + data + "</p>";
        }

        if (imgSrc != ""){
            html += "<img src='" + imgSrc + "' alt=''>";
        }

        html += "</div>";

        this.html = html;
        return html;
    }

    public String getHtml() {
        return html;
    }

    public String toCSS(){
        String css = "#" + getName() + " {\n";
        if (style != null){
            css += style;
        }
        css += "}";

        this.css = css;
        return css;
    }

    public String getCSS() {
        return css;
    }
}
