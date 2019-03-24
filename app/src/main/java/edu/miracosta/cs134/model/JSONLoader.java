package edu.miracosta.cs134.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Class loads CS134Superheroes data from a formatted JSON (JavaScript Object Notation) file.
 * Populates data model (SuperHero) with data.
 *
 * @author Dennis La
 * @version 1.0
 */
public class JSONLoader {

    /**
     * Loads JSON data from a file in the assets directory.
     *
     * @param context The activity from which the data is loaded.
     * @throws IOException If there is an error reading from the JSON file.
     */
    public static List<SuperHero> loadJSONFromAsset(Context context) throws IOException {
        List<SuperHero> allSuperHeroList = new ArrayList<>();
        String json = null;
        InputStream is = context.getAssets().open("cs134superheroes.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allSuperHeroesJSON = jsonRootObject.getJSONArray("CS134Superheroes");

            // DONE: Loop through all the super heroes in the JSON data, create a hero
            int length = allSuperHeroesJSON.length();

            JSONObject superHeroJSON;
            String name, superPower, oneThing, imageName;
            SuperHero superHero;

            for(int i = 0; i < length; i++)
            {
                superHeroJSON = allSuperHeroesJSON.getJSONObject(i);

                //extract the data from the JSON file
                name = superHeroJSON.getString("Name");
                superPower = superHeroJSON.getString("Superpower");
                oneThing = superHeroJSON.getString("OneThing");
                imageName = superHeroJSON.getString("FileName");

                // DONE: object for each and add the object to the allSuperHeroList
                superHero = new SuperHero(name, superPower, oneThing, imageName);
                allSuperHeroList.add(superHero);

            }


        } catch (JSONException e) {
            Log.e("CS134 Super Heroes", e.getMessage());
        }

        return allSuperHeroList;
    }
}
