package org.example;

public class GatosFavoritos {
    String id;
    String image_id;
    String apiKey = "live_EsreeUcXxzfxH39HkDjCDDlZlJGKhNReSipGEt6kbDqtYicLpkEe9Iq7i7a10Rtp";
    ImagenX image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public ImagenX getImage() {
        return image;
    }

    public void setImage(ImagenX image) {
        this.image = image;
    }
}
