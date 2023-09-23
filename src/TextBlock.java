public class TextBlock extends MurphyBlock {

    TextBlock(String name) {
        super(name);
        super.type = 1;
    }

    public void setTextSize(int size){
        super.style += "font-size: " + size + "px;\n";
    }

}
