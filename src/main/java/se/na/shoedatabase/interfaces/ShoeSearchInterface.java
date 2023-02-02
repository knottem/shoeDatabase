package se.na.shoedatabase.interfaces;

import se.na.shoedatabase.model.shoe.Shoe;

@FunctionalInterface
public interface ShoeSearchInterface {

    boolean search(Shoe s, String answer);

}
