package nhomso1.project2.musicplayer;

public class PlayList {
    private int imageSong ;
    private String namePlayList ;
    private String singer ;

    public PlayList(int imageSong, String namePlayList, String singer) {
        this.imageSong = imageSong;
        this.namePlayList = namePlayList;
        this.singer = singer;
    }

    public int getImageSong() {
        return imageSong;
    }

    public void setImageSong(int imageSong) {
        this.imageSong = imageSong;
    }

    public String getNamePlayList() {
        return namePlayList;
    }

    public void setNamePlayList(String namePlayList) {
        this.namePlayList = namePlayList;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }
}
