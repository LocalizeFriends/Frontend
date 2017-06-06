package com.example.friendlocation.friendlocation.JavaClasses;

import android.app.Activity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by barte_000 on 06.06.2017.
 */

public class SimpleXmlPull {

    Activity activity;
    GoogleMap mGoogleMap;
    public SimpleXmlPull(Activity activity, GoogleMap googleMap){
        this.activity = activity;
        mGoogleMap = googleMap;
        parseXml();
        visualize(mGoogleMap);
    }

    List<MarkerOptions> markerOptionsList = new ArrayList<>();
    void parseXml() {
        try
        {
            InputStream is = activity.getAssets().open("file.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element = doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("marker");

            for (int i = 0; i < nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;

                    markerOptionsList.add(new MarkerOptions()
                            .title(element2.getAttribute("name"))
                            .position(new LatLng(
                                    Double.parseDouble(element2.getAttribute("lat")),
                                    Double.parseDouble(element2.getAttribute("lng"))
                                    )
                            )
                    ) ;
                }
            }

        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    void visualize(GoogleMap googleMap){
        for (MarkerOptions m: markerOptionsList) {
            m.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            googleMap.addMarker(m);
        }
    }
}

