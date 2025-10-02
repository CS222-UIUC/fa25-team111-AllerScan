package com.example.allerscan

import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.collections.MutableList
import java.util.Base64
import org.json.JSONObject

//Given the Barcode of a Product
class BarcodeLookup {
    //fetches ingredients and returns them in a list
    fun lookup(barcode: Int): MutableList<String> {
        println("Barcode: $barcode")
        var ingredients: String = ""

        //Lookup attempt with APIs
        //Open Food Facts
        ingredients = lookupOpenFoodFacts(barcode)
        //Go-UPC
        if (ingredients.isEmpty())
            ingredients = lookupGoUPC(barcode)
        //UPCindex
        if (ingredients.isEmpty())
            ingredients = lookupUPCIndex(barcode);

        //Parsing of all ingredients
        //check if empty
        val ingredientList = mutableListOf<String>()
        return ingredientList;
    }

    fun lookupOpenFoodFacts(barcode: Int): String {
        //https://openfoodfacts.github.io/openfoodfacts-server/api/

        return "";
    }

    fun lookupGoUPC(barcode: Int): String {
        //reference in python: https://go-upc.com/docs/python-barcode-api-lookup
        return "";
    }

    fun lookupUPCIndex(barcode: Int): String {
        //https://go-upc.com/docs/python-barcode-api-lookup
        return "";
    }
}

fun main() {
    //Create Tests For the Barcode


}