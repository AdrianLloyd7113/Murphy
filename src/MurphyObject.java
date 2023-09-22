public class MurphyObject {

    private String name;

    MurphyObject(String name){
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
