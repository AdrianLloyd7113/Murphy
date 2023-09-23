public class ImageBlock extends MurphyBlock{

    ImageBlock(String name) {
        super(name);
        super.type = 2;
    }

    void giveSrc(String in){
        super.imgSrc = in;
    }

}
