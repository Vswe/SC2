package vswe.stevescarts.old.Fancy;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


@SideOnly(Side.CLIENT)
public class FancyPancyLoader implements Runnable {


    private Map<String, FancyPancyHandler> fancyTypes;

    public FancyPancyLoader() {
        fancyTypes = new HashMap<String, FancyPancyHandler>();
        add(new CapeHandler());
        add(new SkinHandler());
        add(new OverheadHandler());

        new Thread(this).start();
    }

    private void add(FancyPancyHandler fancyPancyHandler) {
        fancyTypes.put(fancyPancyHandler.getCode(), fancyPancyHandler);
    }

    @Override
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection)new URL("https://dl.dropbox.com/u/46486053/RemoteInfo.txt").openConnection();
            HttpURLConnection.setFollowRedirects(true);
            connection.setConnectTimeout(2147483647);
            connection.setDoInput(true);
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;


            while ((line = reader.readLine()) != null) {

                int commentStart = line.indexOf("#");
                if (commentStart != -1) {
                    line = line.substring(0, commentStart);
                }

                line = line.trim();

                String[] split = line.split(":");

                if (split.length == 2) {
                    String command = split[0];
                    String content = split[1];

                    HashMap<String, String[]> entries = new HashMap<String, String[]>();
                    int start = 0;
                    while (true) {


                        int startIndex = content.indexOf("[", start);
                        if (startIndex == -1) {
                            break;
                        }
                        String entry = content.substring(start, startIndex).trim();
                        int endIndex = content.indexOf("]", startIndex);
                        if (endIndex == -1) {
                            break;
                        }
                        start = endIndex + 1;
                        String values = content.substring(startIndex + 1, endIndex).trim();
                        String[] valueSplit = values.split(",");
                        for (int i = 0; i < valueSplit.length; i++) {
                            valueSplit[i] = valueSplit[i].trim();
                        }
                        entries.put(entry, valueSplit);
                    }

                    FancyPancyHandler fancyPancyHandler = fancyTypes.get(command);
                    if (fancyPancyHandler != null) {

                        String[] users = entries.get("User");
                        if (users != null) {

                            for (String user : users) {
                                UserFancy userFancy = fancyPancyHandler.getFancies().get(user);
                                if (userFancy == null) {
                                    userFancy = new UserFancy(fancyPancyHandler);
                                    fancyPancyHandler.getFancies().put(user, userFancy);
                                }

                                userFancy.add(new FancyPancy(fancyPancyHandler, entries));
                            }
                        }else{
                            String[] servers = entries.get("Server");
                            if (servers != null) {
                                for (String server : servers) {
                                    ServerFancy serverFancy = fancyPancyHandler.getServerFancies().get(server);
                                    if (serverFancy == null) {
                                        serverFancy = new ServerFancy();
                                        fancyPancyHandler.getServerFancies().put(server, serverFancy);
                                    }

                                    serverFancy.add(new FancyPancy(fancyPancyHandler, entries));
                                }
                            }
                        }

                    }
                }
            }
        }catch(Throwable ex) {
            ex.printStackTrace();
        }

        for (FancyPancyHandler fancyPancyHandler : fancyTypes.values()) {
            fancyPancyHandler.setReady(true);
        }
    }




}
