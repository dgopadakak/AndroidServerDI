import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import directions.Tour;
import directions.DirectionOperator;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MultiServer
{
    private ServerSocket serverSocket;
    private static DirectionOperator diro = new DirectionOperator();
    private static String diroJSON;
    private final static String filePath = "info.txt";

    public void start(int port) throws IOException
    {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        try {
            diroJSON = readFile(filePath, StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        diro = gson.fromJson(diroJSON, DirectionOperator.class);


//        diro.addTour("Западная Европа", new Tour("Португалия", "2 недели", 5,
//                "12.01.2023", "26.01.2023", 150000, 1,
//                "Посетите солнечную Португалию!"));
//        diro.addTour("Западная Европа", new Tour("Испания", "3 недели", 4,
//                "03.02.2023", "24.02.2023", 165000, 0,
//                "Посетите солнечную Испанию!"));
//
//        diro.addTour("Восток", new Tour("Китай", "1,5 недели", 4,
//                "08.02.2023", "18.02.2023", 120000, 1,
//                "Посетите загадочный Китай!"));
//        diro.addTour("Восток", new Tour("Вьетнам", "1 неделя", 3,
//                "29.01.2023", "04.02.2023", 95000, 1,
//                "Посетите интересный Вьетнам!"));
//
//        diroJSON = gson.toJson(diro);
//        writeFile(filePath, diroJSON);
//        System.out.println("Done!");

        serverSocket = new ServerSocket(port);
        while (true)
        {
            new EchoClientHandler(serverSocket.accept()).start();
        }
    }

    public void stop() throws IOException
    {
        serverSocket.close();
    }

    private static class EchoClientHandler extends Thread
    {
        private final Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run()
        {
            try
            {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String inputLine = null;
            while (true)
            {
                try
                {
                    if ((inputLine = in.readLine()) == null) break;
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                if (".".equals(inputLine))
                {
                    out.println("bye");
                    break;
                }
                if ("REFRESH".equals(inputLine))
                {
                    out.println(diroJSON);
                }
                if (inputLine != null)
                {
                    if ('d' == inputLine.charAt(0))     // d0,1
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] ids = inputLine.substring(1).split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        diro.delTour(groupID, examID);
                        diroJSON = gson.toJson(diro);
                        writeFile(filePath, diroJSON);
                        out.println(diroJSON);
                    }
                    if ('e' == inputLine.charAt(0))     // e0,3##json
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        String[] ids = parts[0].split(",");
                        int groupID = Integer.parseInt(ids[0]);
                        int examID = Integer.parseInt(ids[1]);
                        Tour tempTour = gson.fromJson(parts[1], Tour.class);
                        diro.editTour(groupID, examID, tempTour);
                        diroJSON = gson.toJson(diro);
                        writeFile(filePath, diroJSON);
                        out.println(diroJSON);
                    }
                    if ('u' == inputLine.charAt(0))     // ujson
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        Gson gson = gsonBuilder.create();
                        DirectionOperator tempGo = gson.fromJson(inputLine.substring(1), DirectionOperator.class);
                        diro.setDirections(tempGo.getDirections());
                        diroJSON = gson.toJson(diro);
                        writeFile(filePath, diroJSON);
                    }
                    if ('a' == inputLine.charAt(0))
                    {
                        GsonBuilder gsonBuilder = new GsonBuilder();        // agroupName##json
                        Gson gson = gsonBuilder.create();
                        String[] parts = inputLine.substring(1).split("##");
                        Tour tempTour = gson.fromJson(parts[1], Tour.class);
                        diro.addTour(parts[0], tempTour);
                        diroJSON = gson.toJson(diro);
                        writeFile(filePath, diroJSON);
                        out.println(diroJSON);
                    }
                }
            }

            try
            {
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            out.close();
            try
            {
                clientSocket.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static String readFile(String path, Charset encoding) throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    public static void writeFile(String path, String text)
    {
        try(FileWriter writer = new FileWriter(path, false))
        {
            writer.write(text);
            writer.flush();
        }
        catch(IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
