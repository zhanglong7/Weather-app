package com.example.weather

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URI
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import android.provider.ContactsContract.CommonDataKinds.Website.URL as URL1

class MainActivity : AppCompatActivity() {

    val CITY: String = "khulna,bd"
    val API: String= "bb9f38871e6eaeb785f75bc764dca4f8"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        WeatherTask().execute()

    }

    inner class WeatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */
            // onPreExecuteis an AsyncTask mehod
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContiner).visibility = View.GONE
            findViewById<TextView>(R.id.errortext).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{

                //   we create a http request to the API url.

                // if we want to get current city through lat and lang we need to get let and lang and use in api url
                // response = URL("https://api.openweathermap.org/data/2.5/weather?lat=$LAT&lon=$LON&units=metric&appid=$API").readText(
                //                    Charsets.UTF_8
                //                )

                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                     .readText( Charsets.UTF_8)
            }catch (e: Exception){
                response = null
            }
            return response
        }


        //Then the response from the url will be passed to the onPostExecute(Result).

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                /* Extracting JSON returns from the API */


                //api passes weather information in JSON format, we’ll extract the data so that we can
                // later set the extracted data for our views

                //Inside this main object say, "weather" is an array cause its elements are
                // inside a square bracket. So to get its data we can use val weather = jsonObj.getJSONArray("weather")
                // look this: http://surl.li/dsxak

                //extracted our necessary weather information
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt:Long = jsonObj.getLong("dt")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")

                val updatedAtText = "Updated at: "+ SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt*1000))
                val address = jsonObj.getString("name")+", "+sys.getString("country")

                val temp = main.getString("temp")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val tempMin = "Min " + main.getString("temp_min")+"°C"
                val tempMax = "Max " + main.getString("temp_max")+"°C"

                val windSpeed = wind.getString("speed")

                val weatherDescription = weather.getString("description")



                /* Populating extracted data into our views */
                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.upated_at).text =  updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax

                //why we multiply 1000 ?

                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset*1000))

                findViewById<TextView>(R.id.wind).text = windSpeed
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity

                /* Views populated, Hiding the loader, Showing the main design */
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContiner).visibility = View.VISIBLE

            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errortext).visibility = View.VISIBLE
            }

        }
    }
}

