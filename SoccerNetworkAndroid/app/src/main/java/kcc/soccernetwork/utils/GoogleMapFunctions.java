package kcc.soccernetwork.utils;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.ArrayList;

        import javax.xml.parsers.DocumentBuilder;
        import javax.xml.parsers.DocumentBuilderFactory;

        import org.apache.http.HttpResponse;
        import org.apache.http.client.ClientProtocolException;
        import org.apache.http.client.HttpClient;
        import org.apache.http.client.methods.HttpGet;
        import org.apache.http.client.methods.HttpPost;
        import org.apache.http.impl.client.DefaultHttpClient;
        import org.apache.http.protocol.BasicHttpContext;
        import org.apache.http.protocol.HttpContext;
        import org.w3c.dom.Document;
        import org.w3c.dom.Node;
        import org.w3c.dom.NodeList;
        import android.app.Activity;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;

        import kcc.soccernetwork.R;

public class GoogleMapFunctions {
    GoogleMap googleMap;
    Context context;
    public GoogleMapFunctions(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
    }

    public void moveCameraToLatLng(double latitude, double longitude, String title, String snippet) {

        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOps = new MarkerOptions();
        markerOps.position(latLng);
        markerOps.title(title).snippet(snippet);
        markerOps.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_field));
        Marker markerRes = googleMap.addMarker(markerOps);
        markerRes.showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    public static MarkerOptions createMarkerOptions(double latitude, double longitude, String title, String snippet) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOps = new MarkerOptions();
        markerOps.position(latLng);

        markerOps.title(title).snippet(snippet);
        markerOps.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_field));
        return markerOps;
    }

    public void addMarker(double latitude, double longitude, String title, String snippet) {
        LatLng latLng = new LatLng(latitude, longitude);
        MarkerOptions markerOps = new MarkerOptions();
        markerOps.position(latLng);

        markerOps.title(title).snippet(snippet);
        markerOps.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_field));
        Marker markerRes = googleMap.addMarker(markerOps);
    }

    public void moveCameraToCurrentPosition(Activity activity) {
        GPSTracker gpsTracker = new GPSTracker(activity);
        double latitude = gpsTracker.getLatitude();
        double longitude = gpsTracker.getLongitude();

        LatLng currentLatLng = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));


    }

    public static Bitmap getGoogleMapThumbnail(double lati, double longi) {
        String URL = "http://maps.google.com/maps/api/staticmap?center="
                + lati
                + ","
                + longi
                + "&zoom=17&size=400x200&sensor=false&markers=size:mid%7Ccolor:0x0000ff%7Clabel:R%7C"
                + lati + "," + longi + "&key=AIzaSyBPYhKjbTSEcXkZKaXS1vSmWE8OarMyd1s";
        Bitmap bmp = null;
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);

        InputStream in = null;
        try {
            in = httpclient.execute(request).getEntity().getContent();
            bmp = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return bmp;
    }

    public static String getStaticMapURL(String lati, String longi, String apiKey) {
        String URL = "http://maps.google.com/maps/api/staticmap?center="
                + lati
                + ","
                + longi
                + "&zoom=17&size=400x200&sensor=false&markers=size:mid%7Ccolor:0x0000ff%7Clabel:R%7C"
                + lati + "," + longi + "&key=" + apiKey;
        return URL;
    }

    public static String getDistanceOnRoad(double latitude, double longitude,
                                           double prelatitute, double prelongitude) {
        String result_in_kms = "";
        String url = "http://maps.google.com/maps/api/directions/xml?origin="
                + latitude + "," + longitude + "&destination=" + prelatitute
                + "," + prelongitude + "&sensor=false&units=metric";
        String tag[] = { "text" };
        HttpResponse response = null;
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            response = httpClient.execute(httpPost, localContext);
            InputStream is = response.getEntity().getContent();
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(is);
            if (doc != null) {
                NodeList nl;
                ArrayList args = new ArrayList();
                for (String s : tag) {
                    nl = doc.getElementsByTagName(s);
                    if (nl.getLength() > 0) {
                        Node node = nl.item(nl.getLength() - 1);
                        args.add((node).getTextContent());
                    } else {
                        args.add("-");
                    }
                }
                result_in_kms = String.format("%s", args.get(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result_in_kms;
    }
}
