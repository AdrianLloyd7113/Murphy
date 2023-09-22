import java.util.ArrayList;
import java.util.HashMap;

public class MurphyStructure {

    private static ArrayList<MurphyObject> obj = new ArrayList<MurphyObject>();
    private static HashMap<String, Integer> objMap = new HashMap<String, Integer>();
    private String name;

    MurphyStructure(String name, ArrayList<MurphyObject> obj, HashMap<String, Integer> objMap){
        this.obj = obj;
        this.objMap = objMap;
    }


}
