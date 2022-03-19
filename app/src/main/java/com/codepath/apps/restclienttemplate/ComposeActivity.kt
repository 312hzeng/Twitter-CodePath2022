package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {


    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweet)
        btnTweet = findViewById(R.id.btnTweet)

        client = TwitterApplication.getRestClient(this)

        btnTweet.setOnClickListener{
            //get content of edittext
            val tweetContent = etCompose.text.toString()
                
            //make sure tweet isn't empty
            if(tweetContent.isEmpty()){
                Toast.makeText(this, "Empty tweets", Toast.LENGTH_SHORT ).show()
            }
            //make sure content isnt too long
            else if(tweetContent.length > 148){
                Toast.makeText(this, "tweets is too long", Toast.LENGTH_SHORT ).show()

            }else{
                //make api call to twitter to publish
                client.publishTweet(tweetContent, object:JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e("ComposeActivity", "Fail to publish", throwable)
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        //send tweet back to TimelineActivity
                        Log.i("ComposeActivity", "Success to publish")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                        
                    }

                })
            }

        }

    }
}