package com.example.allerscan

import android.util.Log

//Checks if allergen is within ingredients
//Should be updated when user adds allergy in profile
//Must be tied to frontend/UI
class AllergenChecker {
    //user enters in their allergens to list/map
    val allergenMap = mutableMapOf(
        "peanut" to false,
        "almond" to false,
        "walnut" to false,
        "pecan" to false,
        "nut" to false,
        "milk" to false,
        "egg" to false,
        "soy" to false,
        "fish" to false,
        "shellfish" to false,
        "sesame" to false,
        "wheat" to false,
    )
    //or use a list
    //val allergenList = mutableListOf<String>()
    fun addAllergen(allergen : String) {
        //allergenList.add(allergen);
        allergenMap[allergen] = true
    }
    //should return 0 = false/does not contain, 1 = unknown, 2 = true/contains
    fun foodSafe(ingredients: MutableList<String>): Int {
        //function check ingredient list according to allergenMap
        if (ingredients.isEmpty()) {
            Log.e("Allergens", "Unknown/Proceed with Caution")
            return 1;
        }
        for (ingredient in ingredients) {
            if (allergenMap[ingredient] == true) {
                Log.d("Allergens", "Allergen is Found")
                return 2;
            }
        }
        Log.d("Allergens", "No Allergens Found")
        return 0;
    }
}
