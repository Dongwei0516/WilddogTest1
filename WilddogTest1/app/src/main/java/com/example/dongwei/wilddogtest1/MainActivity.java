package com.example.dongwei.wilddogtest1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wilddog.client.ChildEventListener;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.Query;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.Wilddog;
import com.wilddog.client.WilddogError;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Wilddog.setAndroidContext(this);
        Wilddog ref = new Wilddog("https://test-02-dw.wilddogio.com");

        ref.setValue("hello world!!");

        ref.addValueEventListener(new ValueEventListener(){
            public void onDataChange(DataSnapshot snapshot){
                System.out.println(snapshot.getValue());
            }

            public void onCancelled(WilddogError error){
                if(error != null){
                    System.out.println(error.getCode());
                }
            }

        });

        Wilddog usersRef = ref.child("users");
        Map<String,String> mMap = new HashMap<String, String>();
        mMap.put("year","1912");
        mMap.put("name","Sense" );

        Map<String,String> nMap = new HashMap<String, String>();
        nMap.put("year", "1906");
        nMap.put("name", "Copper");

        Map<String,Map<String,String>> users = new HashMap<String, Map<String, String>>();
        users.put("mMap",mMap);
        users.put("nMap",nMap);

//        usersRef.setValue(users);
        usersRef.push().setValue(users);

        Wilddog hopperRef = usersRef.child("hop");
        Map<String ,Object> nickname = new HashMap<String,Object>();
        nickname.put("nickname","AmazingGrace");
        hopperRef.updateChildren(nickname);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getValue());
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(dataSnapshot.getKey(),dataSnapshot.getValue());
                    System.out.println(jsonObject.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(WilddogError wilddogError) {
                if (wilddogError!=null){
                    System.out.println(wilddogError.getCode());
                }
            }
        });

        Wilddog ref2 = ref.child("a").push();
        Wilddog ref3 = ref.child("a/b");
        Wilddog ref4 = ref.child("a").child("b");

        ref2.push().setValue(100);
        ref3.push().setValue(200);
        ref4.push().setValue(300);

        ref2.setPriority(100);
        ref3.setPriority(200);

        ref.child("a/b").removeValue();

        Wilddog dinoRef = new Wilddog("https://dinosaur-facts.wilddogio.com/dinosaurs");
        Query queryRef = dinoRef.orderByValue();
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getKey()+ "---"+ dataSnapshot.getValue());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                System.out.println(dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(WilddogError wilddogError) {

                if (wilddogError != null){
                    System.out.println(wilddogError.getCode());
                }

            }
        });
    }


    public void onChildChanged(DataSnapshot snapshot, String childKey){
        String title = (String)snapshot.child("title").getValue();
        System.out.println("Update post title :" + title);
        try {
            JSONObject json = new JSONObject();
            json.put(snapshot.getKey(),snapshot.getValue());
            System.out.println(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public class DinosaurFacts {

        long height;
        double length;
        long weight;
        public DinosaurFacts() {

        }
        public long getHeight() {
            return height;
        }
        public double getLength() {
            return length;
        }
        public long getWeight() {
            return weight;
        }
    }

}
