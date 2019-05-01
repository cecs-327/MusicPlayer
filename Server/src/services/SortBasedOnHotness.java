package services;
import java.util.Comparator;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class SortBasedOnHotness implements Comparator<JsonObject> {

        @Override
        public int compare(JsonObject lhs, JsonObject rhs) {

        	float leftObject;
        	float rightObject;

        	try
    		{
        		leftObject = lhs.getAsJsonObject("song").get("hotttnesss").getAsFloat();
            	rightObject = rhs.getAsJsonObject("song").get("hotttnesss").getAsFloat();


    		}catch(Exception e)
    		{

    			try
        		{

    				leftObject = lhs.getAsJsonObject("artist").get("hotttnesss").getAsFloat();
                	rightObject = rhs.getAsJsonObject("artist").get("hotttnesss").getAsFloat();

        		}catch(Exception c)
        		{
        			System.out.println("Enter second time");
        			System.out.println("null as value");
        			leftObject = 0;
        			rightObject = 0;
        		}
    		}


            try {
                if(leftObject < rightObject)
                {

                	return 	1;

                }
                else if(leftObject > rightObject)
                {

                	return -1;
                }
                else
                {

                	return 0;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
           return 0;

        }
    }