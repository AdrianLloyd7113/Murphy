public class MurphyInteger extends MurphyVariable{

    private int val;

    MurphyInteger(String name, int val) {
        super(name);
        this.val = val;
        this.value = "" + val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
        this.value = "" + val;
    }
}
