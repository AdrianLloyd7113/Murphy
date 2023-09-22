public class TercetObject {

    private String name;

    TercetObject(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toHTML(){
        return "<p>" + name + "</p>";
    }
    public String toCSS(){
        return "";
    }
}
