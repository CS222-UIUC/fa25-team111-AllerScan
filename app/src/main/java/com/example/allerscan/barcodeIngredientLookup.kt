package com.example.allerscan


import okhttp3.OkHttpClient
import okhttp3.Request
import kotlin.collections.MutableList
import java.util.Base64
import org.json.JSONObject
import android.util.Log
import okhttp3.Callback
import okhttp3.Call
import java.io.IOException
import okhttp3.Response

//Given the Barcode of a Product
class BarcodeIngredientLookup {
    //given a barcode, attempts to fetch ingredients and returns ingredients as list of strings
    //combines API call and parse function
    fun lookup(barcode: String): Pair<String, MutableList<String>> {
        Log.d("BarcodeIngredientLookup", "Barcode: $barcode")
        var ingredientsString = ""
        var productName = ""
        //lookup attempt with openFoodFacts
        val call = BarcodeIngredientLookup()
        call.lookupOpenFoodFacts(barcode) { product, ingredients ->
            if (productName.isEmpty()) {
                Log.e("BarcodeIngredientLookup", "Name not found/request failed")
            } else {
                productName = product
            }
            if (ingredients.isEmpty()) {
                Log.e("BarcodeIngredientLookup", "Ingredients not found/request failed")
            } else {
                Log.d("BarcodeIngredientLookup", "Ingredients: $ingredients")
                ingredientsString = ingredients
            }
        }
        val ingredientList = parseIngredients(ingredientsString)
        return Pair(productName, ingredientList);
    }

    //given a string, parses/separates the ingredients
    fun parseIngredients(ingredients: String): MutableList<String> {
        //https://world.openfoodfacts.net/api/v2/product/044000047009.json
        //Parsing of all ingredients
        var ingredientList = mutableListOf<String>()
        if (ingredients.isEmpty()) {
            return ingredientList
        }
        //lowercase all ingredients in case of case sensitive
        val ingredientsString = ingredients.lowercase()
        var word = ""
        for (char in ingredientsString) {
            if (char == ' ' || char == '/') {
                //add word to list
                if (!word.isEmpty()) {
                    //filter not important terms
                    //will make allergen checker faster
                    if (word != "flavor"
                        && word != "artificial"
                        && word != "or"
                        && word != "and"
                        && word != "high"
                        && word != "unbleached"
                        && word != "enriched"
                        && word != "reduced"
                        && word != "processed"
                        && word != "roasted"
                        && word != "fully") {
                        ingredientList.add(word)
                    }
                }
                word = ""
            } else if (char == ',' || char == '(' || char == ')' || char == '{' || char == '}') {
                //skip
                continue
            } else {
                //add the char to the word
                word += char
            }
        }
        //add last word if needed
        if (!word.isEmpty()) {
            ingredientList.add(word)
        }
        Log.d("BarcodeIngredientLookup", "List of Ingredients: "+ingredientList.joinToString("|"))
        return ingredientList
    }


    //attempt to fetch ingredients using OpenFoodFacts API
    fun lookupOpenFoodFacts(barcode: String, callback: (String, String) -> Unit) {
        //https://openfoodfacts.github.io/openfoodfacts-server/api/
        //using a JavaScript example from the Open Food Facts Documentation
        //Some of the code in this function was referenced from an example generated from ChatGPT
        //specifically how to make asynchronous calls and the authorization in Kotlin
        //The code has been edited to work in for the specific function
        //Check Logcat for functionality
        //Must scan US food product
        //Should add a where countries sold check/language check
        val client = OkHttpClient()
        val credentials = "off:off"
        val auth = "Basic " + Base64.getEncoder().encodeToString(credentials.toByteArray())
        val url =
            "https://world.openfoodfacts.net/api/v2/product/$barcode.json"
        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", auth)
            .build()
        //Asynchronous Request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("BarcodeLookup", "Request failed", e)
                callback("", "")
            }
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    try {
                        if (!response.isSuccessful) {
                            Log.e("BarcodeLookup", "Request failed - ${response.code}")
                            callback("", "")
                            return
                        }
                        val body = response.body
                        if (body == null) {
                            Log.e("BarcodeLookup", "Request failed - no response body found")
                            callback("", "")
                            return
                        }
                        val bodyString = body.string()
                        if (bodyString.isEmpty()) {
                            Log.e("BarcodeLookup", "Request failed - response body empty")
                            callback("", "")
                            return
                        }
                        Log.d("BarcodeLookup", "Response: $bodyString")
                        val json = JSONObject(bodyString)
                        if (!json.has("product")) {
                            Log.e("BarcodeLookup", "Request failed - no product found")
                            callback("", "")
                            return
                        }
                        val product = json.getJSONObject("product")
                        val productName = product.optString("product_name_en", "")
                        val ingredients = product.optString("ingredients_text_en", "")
                        Log.d("BarcodeLookup", "Name for barcode $barcode: $productName")
                        Log.d("BarcodeLookup", "Ingredients for barcode $barcode: $ingredients")
                        //calls callback currently in MainActivity
                        callback(productName, ingredients)
                    } catch (e: Exception) {
                        Log.e("BarcodeLookup", "Request failed - exception", e)
                    }
                } //response closed
            }
        })
        //returns ingredients in callback
    }

    //Add additional APIs below
    fun lookupGoUPC(barcode: String): String {
        //reference in python: https://go-upc.com/docs/python-barcode-api-lookup
        //not free API :(
        return ""
    }
    fun lookupUPCIndex(barcode: String): String {
        //https://www.upcindex.com/
        //https://devs.upcitemdb.com/ has free API
        return ""
    }
}


fun main() {
    //Create Tests For the Barcode
    //Use oreo UPC code: 044000047009
    //val lookupBarcode = BarcodeIngredientLookup()
    //val barcode = "044000047009"
    //val ingredients = lookupBarcode.lookupOpenFoodFacts(barcode);
    //expected result
    //"unbleached enriched flour (wheat flour, niacin, reduced iron, thiamine mononitrate {vitamin b1}, riboflavin {vitamin b2}, folic acid), sugar, palm and/or canola oil, cocoa (processed with alkali), high fructose corn syrup, leavening (baking soda and/or calcium phosphate), salt, soy lecithin, chocolate, artificial flavor,"
}
