package gson;
import java.util.ArrayList;

public class Playlists {
    private String PlaylistTitle;
    private ArrayList<Songs> Songs;

    public Playlists(String PlaylistTitle){
        this.PlaylistTitle = PlaylistTitle;
        Songs = new ArrayList<Songs>();
    }

    public String getPlaylistTitle(){
        return PlaylistTitle;
    }

    public ArrayList<Songs> getPlaylistsSongs(){
        return Songs;
    }

    public void setPlaylistTitle(String newTitle){
        PlaylistTitle = newTitle;
    }

    public void setPlaylistsSongs(ArrayList<Songs> songsList){
        Songs = songsList;
    }
}