package com.example.ict602_grpproject;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message{
        @SerializedName("Message")
        @Expose
        private String msg;

        public String getMsg(){ return msg; }
}
