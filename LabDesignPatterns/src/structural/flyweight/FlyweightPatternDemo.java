package structural.flyweight;

public class FlyweightPatternDemo {

    public static void main(String[] args) {

        PotionFactory factory = new PotionFactory();

        Potion potion1 = factory.createPotion(PotionType.INVISIBILITY);
        potion1.drink();
        factory.createPotion(PotionType.INVISIBILITY).drink();
        factory.createPotion(PotionType.HEALING).drink();
        factory.createPotion(PotionType.INVISIBILITY).drink();
        factory.createPotion(PotionType.HOLY_WATER).drink();
        factory.createPotion(PotionType.HOLY_WATER).drink();
        factory.createPotion(PotionType.HEALING).drink();

    }

}