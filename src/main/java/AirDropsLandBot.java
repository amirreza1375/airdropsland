import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.ws.rs.GET;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AirDropsLandBot extends TelegramLongPollingBot {

//    private String[] airDropsArr = {"MUFC"};
    private static HttpURLConnection connection;

    @Override
    public String getBotUsername() {
        return "airdropsland_bot";
    }

    @Override
    public String getBotToken() {
        return "2105085653:AAFNPi8IaryLyM0blhi2jJImbRpZ1frQlgc";
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {



        String command = update.getMessage().getText();

        SendMessage sendMessage = new SendMessage();
        SendPhoto sendPhoto = new SendPhoto();


        String userName = update.getMessage().getFrom().getFirstName();

        if (command.equals("/airdrops")){

            BufferedReader reader;
            String line;
            StringBuffer responseContent = new StringBuffer();
            try {
                URL url = new URL("http://me-pet.ir/airdrops_land/api/airdrop/read.php");
                connection = (HttpURLConnection) url.openConnection();

                //Request setup
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int status = connection.getResponseCode();

                if (status > 299){

                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                    while((line = reader.readLine()) != null){
                        responseContent.append(line);
                    }
                    reader.close();

                }else{
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    while((line = reader.readLine()) != null){
                        responseContent.append(line);
                    }
                    reader.close();
                }

                JSONArray airDrops = new JSONArray(responseContent.toString());

                String fullMessage = "";

                for (int i = 0 ; i < airDrops.length() ; i++){
                    JSONObject airDrop = airDrops.getJSONObject(i);
                    String name = airDrop.getString("name").replace(" ","_");
                    String desc = airDrop.getString("desc");
                    String link = airDrop.getString("link");
                    String c_date = airDrop.getString("c_date");
                    String isExpired = airDrop.getString("isExpired");
                    String expireDate = airDrop.getString("expireDate");
                    String usedCount = airDrop.getString("usedCount");
                    String priority = airDrop.getString("priority");

                    String messageStr = "/"+name;

                    fullMessage = fullMessage + messageStr +"\n ----------------------- \n";


                }
                setCompanyInfo(fullMessage);
                sendMessage.setText(fullMessage);



            } catch (MalformedURLException e) {
                sendMessage.setText(e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                sendMessage.setText(e.getMessage());
                e.printStackTrace();
            }finally {
                connection.disconnect();
            }


        }else if(command.equals("/old")){
            sendMessage.setText("Dear "+userName +"\n\nThis part is in progress \n\nThanks for your patient :)");
        }else if (command.equals("/start")){
            sendMessage.setText("Let's find aboud about airdrops and earn money \n\nAdd command to get airdrops list /airdrops");
        }
        else{
                BufferedReader reader;
                String line;
                StringBuffer responseContent = new StringBuffer();
                try {
                    URL url = new URL("http://me-pet.ir/airdrops_land/api/airdrop/read.php");
                    connection = (HttpURLConnection) url.openConnection();

                    //Request setup
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);

                    int status = connection.getResponseCode();

                    if (status > 299){

                        reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));

                        while((line = reader.readLine()) != null){
                            responseContent.append(line);
                        }
                        reader.close();

                    }else{
                        reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                        while((line = reader.readLine()) != null){
                            responseContent.append(line);
                        }
                        reader.close();
                    }

                    JSONArray airDrops = new JSONArray(responseContent.toString());

                    String fullMessage = "";

                    for (int i = 0 ; i < airDrops.length() ; i++){
                        JSONObject airDrop = airDrops.getJSONObject(i);
                        String name = airDrop.getString("name").replace(" ","_");
                        String desc = airDrop.getString("desc");
                        String link = airDrop.getString("link");
                        String c_date = airDrop.getString("c_date");
                        String isExpired = airDrop.getString("isExpired");
                        String expireDate = airDrop.getString("expireDate");
                        String usedCount = airDrop.getString("usedCount");
                        String priority = airDrop.getString("priority");

                        if (command.replace("/"," ").trim().equals(name.trim())) {

                            String messageStr = "Airdrop name : " + name+"\n\n"
                                    +desc+"\n\n"
                                    +link;

                            fullMessage = fullMessage + messageStr + "\n ----------------------- \n";
                        }


                    }
                    if (fullMessage.equals("")){
                        fullMessage = "No airdop found";
                    }
                    sendMessage.setText(fullMessage);
                    sendMessage.setReplyToMessageId(update.getMessage().getMessageId());



                } catch (MalformedURLException e) {
                    sendMessage.setText(e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    sendMessage.setText(e.getMessage());
                    e.printStackTrace();
                }finally {
                    connection.disconnect();
                }


        }

        sendMessage.setChatId(update.getMessage().getChatId()+"");

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void setCompanyInfo(String fullMessage) {

        String companyInfo = "\n ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ \nAirDrops - Land \n https://me-pet.ir";
        fullMessage = fullMessage + companyInfo;

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }
}
