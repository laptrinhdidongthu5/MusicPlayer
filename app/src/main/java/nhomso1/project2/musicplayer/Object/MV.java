package nhomso1.project2.musicplayer.Object;

public class MV {
    private String nameSong ;
    private int imageSong ;
    private String countSong ;

    public MV(String nameSong, int imageSong, String countSong) {
        this.nameSong = nameSong;
        this.imageSong = imageSong;
        this.countSong = countSong;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public int getImageSong() {
        return imageSong;
    }

    public void setImageSong(int imageSong) {
        this.imageSong = imageSong;
    }

    public String getCountSong() {
        return countSong;
    }

    public void setCountSong(String countSong) {
        this.countSong = countSong;
    }
}
