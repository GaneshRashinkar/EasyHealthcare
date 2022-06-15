package com.ganeshrashinkar.easyhealthcare

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ganeshrashinkar.easyhealthcare.adapters.ChatAdapter
import com.ganeshrashinkar.easyhealthcare.databinding.ActivityChatBinding
import com.ganeshrashinkar.easyhealthcare.dataclass.Message
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {
    private  var sessionsClient: SessionsClient?=null
    private var sessionName:SessionName?=null
    lateinit var mBinding : ActivityChatBinding
    lateinit var chatAdapter: ChatAdapter
    var messageList=ArrayList<Message>()
    var counter=0
    private val uuid=UUID.randomUUID().toString()
    val msgList= mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding= ActivityChatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)


            msgList.add("Hi Ganesh, how can I help you")
            msgList.add("Can you please share me the details about your problem")
            msgList.add("Please describe more symptums")
           // msgList.add("Can you take photo to analyze in more breaf")
            msgList.add("It seems you are having fungal infection. You have to visit a skin care center. Please choose Skin care center." +
                    "\n\nEnter 1 for Skin Care N Care, near santosh hall, Sinhagad Rd, Pune" +
                    "\n\nEnter 2 for Divine Skin Care Clinic, Sawali apartment, Sargam Society Rd, Pune")

            msgList.add("Do you want to book appointment?")
            msgList.add("Please enter date and time for appointment")
            msgList.add("Request have been sent to doctor. You will get confirmation of appointment. You can also call on 9900990055 to contact with Skin Care N Care" +
                    "" +
                    "\n\nTill the appointment take care of following things" +
                    "\n1. Bath twice\n2. use detol or neem in bathing water" +
                    "\n3. keep infected area dry\n4. use clean cloths")

        mBinding.rvMessages.layoutManager=LinearLayoutManager(this)
        chatAdapter=ChatAdapter(this,messageList)
        mBinding.rvMessages.adapter=chatAdapter
        mBinding.btnSend.setOnClickListener {
            var message=mBinding.etMessage.text.toString()
            if(message.isNotEmpty())
            {
                addMessageToList(message,false)
                Thread{
                    runOnUiThread {
                        if(counter<msgList.size){
                            Thread.sleep(1000)
                            addMessageToList(msgList[counter],true)
                        }
                        counter++
                    }
                }.start()
                //sendMessageToBot(message)
            }else Toast.makeText(this, "Please write something", Toast.LENGTH_SHORT).show()
        }
        setUpBot()
    }

    private fun addMessageToList(message:String,isReceived:Boolean){
        if(message.equals("Do you want to book appointment?")){
            chatAdapter.showYesNoButtons(true)
        }
        else{
            chatAdapter.showYesNoButtons(false)
        }
        messageList.add(Message(message,isReceived))
        mBinding.etMessage.setText("")
        chatAdapter.notifyDataSetChanged()
        mBinding.rvMessages.layoutManager?.scrollToPosition(messageList.size-1)
    }
    private fun setUpBot(){
        try{
            val stream=this.resources.openRawResource(R.raw.credentials)
            val credentials:GoogleCredentials= GoogleCredentials.fromStream(stream)
                .createScoped("https://www.googleapis.com/auth/cloud-platform")
            val projectId:String=(credentials as ServiceAccountCredentials).projectId
            val settingsBuilder:SessionsSettings.Builder=SessionsSettings.newBuilder()
            val sessionSettings:SessionsSettings=settingsBuilder.setCredentialsProvider(
                FixedCredentialsProvider.create(credentials)
            ).build()
            sessionsClient=SessionsClient.create(sessionSettings)
            sessionName=SessionName.of(projectId,uuid)
            Log.d("projectId",projectId)
        }catch (e:Exception){
            Log.e("setUpBot",e.toString())
        }
    }
    private fun sendMessageToBot(message: String) {
        val input=QueryInput.newBuilder()
            .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build()
        GlobalScope.launch {
            setMessageInBg(input)
        }
    }
    private suspend fun setMessageInBg(input: QueryInput) {
        withContext(Default){
            try{
                val detectIntentRequest=DetectIntentRequest.newBuilder()
                    .setSession(sessionName.toString())
                    .setQueryInput(input)
                    .build()
                val result=sessionsClient?.detectIntent(detectIntentRequest)
                if(result!=null){
                    runOnUiThread {
                        updateUI(result)
                    }
                }
                else{

                }
            }catch (e:Exception){
                Log.e("setMessageInBg",e.toString())
            }
        }
    }

    private fun updateUI(response: DetectIntentResponse) {
        val botReply:String=response.queryResult.fulfillmentText
        if(botReply.isNotEmpty()){
            addMessageToList(botReply,true)
        }
        else Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
    }
}