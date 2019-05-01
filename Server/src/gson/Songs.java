package gson;
import java.io.*;

public class Songs {
    private String SongTitle;
    private String SongArtist;
    private String MusicFile;

    public Songs(String SongTitle, String SongArtist, String MusicFile){
        this.SongTitle = SongTitle;
        this.SongArtist = SongArtist;
        this.MusicFile = MusicFile;
    }
    
    public String getSongTitle(){
        return SongTitle;
    }
    
    public String getSongArtist(){
        return SongArtist;
    }
    
    public String getMusicFile(){
        return MusicFile;
    }
}