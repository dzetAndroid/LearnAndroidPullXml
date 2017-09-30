package com.example.d_zet.learnandroidpullxml;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = getApplicationContext().getAssets().open("temp.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);

            parseXML(parser);

        } catch (XmlPullParserException e) {

            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        ArrayList<Product> products = null;
        int eventType = parser.getEventType();
        Product currentProduct = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;
            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    products = new ArrayList<>();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if ("product".equalsIgnoreCase(name)){
                        currentProduct = new Product();
                    } else if (currentProduct != null){
                        if ("productname".equalsIgnoreCase(name)){
                            currentProduct.name = parser.nextText();
                        } else if ("productcolor".equalsIgnoreCase(name)){
                            currentProduct.color = parser.nextText();
                        } else if ("productquantity".equalsIgnoreCase(name)){
                            currentProduct.quantity= parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("product") && currentProduct != null){
                        if (products != null) {
                            products.add(currentProduct);
                        }
                    }
            }
            eventType = parser.next();
        }

        printProducts(products);
    }

    private void printProducts(ArrayList<Product> products)
    {
        String content = "";
        Iterator<Product> it = products.iterator();
        while(it.hasNext())
        {
            Product currProduct  = it.next();
            content = content + "Product :" +  currProduct.name + "\t";
            content = content + "Quantity :" +  currProduct.quantity + "\t";
            content = content + "Color :" +  currProduct.color + "\t\n\n";

        }

        TextView display = (TextView)findViewById(R.id.info);
        display.setText(content);
    }

}
