package org.example;

import com.google.gson.Gson;
import com.squareup.okhttp.*;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class GatosService {

    public static void verGatos() throws IOException{
        //1. vamos a traer los datos de la API

        /*
        OkHttpClient client = new OkHttpClient().newBuilder()
          .build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
          .url("https://api.thecatapi.com/v1/images/search")
          .method("GET", body)
          .build();
        Response response = client.newCall(request).execute();;*/

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url("https://api.thecatapi.com/v1/images/search").get().build();
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();

        //cortar los corchetes
        elJson = elJson.substring(1, elJson.length());
        elJson = elJson.substring(0, elJson.length()-1);

        //crear u objeto de la clase Gson
        Gson gson = new Gson();
        Gatos gatos = gson.fromJson(elJson, Gatos.class);

        //redimensionar en caso de necesitar
        Image image = null;
        try{
            URL url = new URL(gatos.getUrl());
            image = ImageIO.read(url);

            ImageIcon fondoGato = new ImageIcon(image);

            if(fondoGato.getIconWidth() > 800){
                //redimensionamos
                Image fondo = fondoGato.getImage();
                Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                fondoGato = new ImageIcon(modificada);
            }

            String menu = "Opciones: \n"
                    + " 1. ver otra imagen \n"
                    + " 2. Favorito \n"
                    + " 3. Volver \n";

            String[] botones = { "ver otra imagen", "favorito", "volver" };
            String id_gato = gatos.getId();
            String opcion = (String) JOptionPane.showInputDialog(null,menu,id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones,botones[0]);

            int seleccion = -1;
            //validamos que opcion selecciona el usuario
            for(int i=0;i<botones.length;i++){
                if(opcion.equals(botones[i])){
                    seleccion = i;
                }
            }

            switch (seleccion){
                case 0:
                    verGatos();
                    break;
                case 1:
                    favoritoGato(gatos);
                    break;
                default:
                    break;
            }

        }catch(IOException e){
            System.out.println(e);
        }


    }

    public static void favoritoGato(Gatos gato) {

        try{
            OkHttpClient client = new OkHttpClient();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\r\n\t\"image_id\":\""+gato.getId()+"\"\r\n\t\r\n}");
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gato.getApikey())
                    .build();
            Response response = client.newCall(request).execute();
        }catch(IOException e){
            System.out.println(e);

        }
    }

    public static void verFavoritos(String apiKey) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.thecatapi.com/v1/favourites")
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .build();
        Response response = client.newCall(request).execute();

        String elJson = response.body().string();// se guarda el string con la respuesta

        //creamos el objeto gson
        Gson gson= new Gson();

        GatosFavoritos[] gatosArray = gson.fromJson(elJson, GatosFavoritos[].class);

        if(gatosArray.length > 0){
            int min = 1 ;
            int max = gatosArray.length;
            int aleatorio = (int)(Math.random()*(max-min)+1)+min;
            int indice = aleatorio-1;

            GatosFavoritos gatosFavoritos=gatosArray[indice];

            //redimensionar en caso de necesitar
            Image image = null;
            try{
                URL url = new URL(gatosFavoritos.image.getUrl());
                image = ImageIO.read(url);

                ImageIcon fondoGato = new ImageIcon(image);

                if(fondoGato.getIconWidth() > 800){
                    //redimensionamos
                    Image fondo = fondoGato.getImage();
                    Image modificada = fondo.getScaledInstance(800, 600, java.awt.Image.SCALE_SMOOTH);
                    fondoGato = new ImageIcon(modificada);
                }

                String menu = "Opciones: \n"
                        + " 1. ver otra imagen \n"
                        + " 2. Eliminar Favorito \n"
                        + " 3. Volver \n";

                String[] botones = { "ver otra imagen", "eliminar favorito", "volver" };
                String id_gato = gatosFavoritos.getId();
                String opcion = (String) JOptionPane.showInputDialog(null,menu,id_gato, JOptionPane.INFORMATION_MESSAGE, fondoGato, botones,botones[0]);

                int seleccion = -1;
                //validamos que opcion selecciona el usuario
                for(int i=0;i<botones.length;i++){
                    if(opcion.equals(botones[i])){
                        seleccion = i;
                    }
                }

                switch (seleccion){
                    case 0:
                        verFavoritos(apiKey);
                        break;
                    case 1:
                        borrarFavorito(gatosFavoritos);
                        break;
                    default:
                        break;
                }

            }catch(IOException e){
                System.out.println(e);
            }

        }

    }

    private static void borrarFavorito(GatosFavoritos gatosFavoritos) {

        try{
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.thecatapi.com/v1/favourites/"+gatosFavoritos.getId()+" ")
                    .delete()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("x-api-key", gatosFavoritos.getApiKey())
                    .build();
            Response response = client.newCall(request).execute();

        }catch(IOException e){
            System.out.println(e);
        }



    }
}