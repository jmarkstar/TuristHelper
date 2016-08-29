package com.jmarkstar.turisthelper.helpers;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import  static com.jmarkstar.turisthelper.utils.Constant.*;

/**
 * Helpers class to consume the services.
 * Created by jmarkstar on 27/08/2016.
 */
public final class HttpClientHelper {

    public enum Method {
        GET, POST, PUT, DELETE
    }

    private HttpURLConnection urlConnection;
    private String responseBody;

    public HttpClientHelper(HttpURLConnection urlConnection) throws IOException {
        this.urlConnection = urlConnection;
    }

    public String getURL(){
        return urlConnection.getURL().toString();
    }

    /**
     * Connects.
     */
    public void connectToServer() throws IOException {
        //connect() method is invoked by conn.getInputStream();
        //so that sometimes we dont need to use it.
        urlConnection.connect();
    }

    /**
     * return the response code of the request.
     */
    public int getResponseCode() throws IOException {
        return urlConnection.getResponseCode();
    }

    /**
     * returns the response.
     */
    public String getResponse() throws IOException {
        if( responseBody == null){
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            responseBody = result.toString();
        }
        return responseBody;
    }

    public static class Builder {

        private StringBuilder mUrl;
        private Method mMethod;
        private boolean wereAddQueryParams = false;
        private Map<String, Object> requestBody;
        private String jsonBody;
        private int timeOut = 10000;

        public Builder(String urlBase) throws Exception {
            if( urlBase.endsWith(SLASH) ){
                mUrl = new StringBuilder(urlBase);
            }else{
                throw new Exception("the url base need to finish with '/'");
            }
        }

        /** HTTP Request Methods.
         * */
        public Builder method(Method method){
            this.mMethod = method;
            return this;
        }

        /** name of the Resource REST.
         * */
        public Builder resource(String resource){
            this.mUrl.append(resource);
            return this;
        }

        /** Adds a query param.
         * */
        public Builder query(String param, String value){
            addInitQuery();
            String query = param+EQUALS+value;
            this.mUrl.append(query);
            this.wereAddQueryParams = true;
            return this;
        }

        /** Adds a query param's List.
         * */
        public Builder queryMap(Map<String, String> options){
            this.addInitQuery();
            /*for (Map.Entry<String, String> entry : options.entrySet()) {
                String query = entry.getKey()+EQUALS+entry.getValue();
                this.mUrl.append(query);
            }*/

            Iterator entries = options.entrySet().iterator();
            boolean hasNext = entries.hasNext();
            while (hasNext) {
                Map.Entry entry = (Map.Entry) entries.next();
                String query = entry.getKey()+EQUALS+entry.getValue();
                this.mUrl.append(query);
                hasNext = entries.hasNext();
                if(hasNext){
                    this.mUrl.append(AND);
                }
            }
            this.wereAddQueryParams = true;
            return this;
        }

        /** Adds a param to the request body.
         * */
        public Builder requestProperty(String param, Object value){
            if(this.requestBody == null){
                this.requestBody = new HashMap<>();
            }
            this.requestBody.put(param, value);
            return this;
        }

        /** Adds a param list to the request body.
         * */
        public Builder requestPropertiesMap(Map<String, Object> requestBody){
            if(this.requestBody == null){
                this.requestBody = new HashMap<>();
            }
            this.requestBody.putAll(requestBody);
            return this;
        }

        /** Adds a json to the request body.
         * */
        public Builder requestPropertiesJson(String jsonBody){
            this.jsonBody = jsonBody;
            return this;
        }

        /** Adds a timeout
         * by default is 10000
         * */
        public Builder timeOut(int timeOut){
            this.timeOut = timeOut;
            return this;
        }

        /** creates the {@link HttpClientHelper} object.
         * */
        public HttpClientHelper create() throws IOException{

            URL url = new URL(mUrl.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(timeOut);
            urlConnection.setConnectTimeout(timeOut);
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setRequestMethod(mMethod.name());
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");

            if (requestBody != null) {
                for (Map.Entry<String, Object> entry : requestBody.entrySet()) {
                    urlConnection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                }
            }

            if(jsonBody != null){
                urlConnection.setFixedLengthStreamingMode(jsonBody.getBytes().length);
                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(jsonBody.getBytes());
                out.flush();
                out.close();
            }
            return  new HttpClientHelper(urlConnection);
        }

        /** Adds a '?' or '&' to a Url.
         * */
        private void addInitQuery(){
            if(wereAddQueryParams){
                mUrl.append(AND);
            }else{
                mUrl.append(QUESTION_MARK);
            }
        }
    }
}

