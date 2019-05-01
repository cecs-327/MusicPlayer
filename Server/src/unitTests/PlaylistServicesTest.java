package unitTests;
import java.util.ArrayList;

import gson.Playlists;
import gson.Songs;
import gson.UserPlaylists;
import services.PlaylistServices;

public class PlaylistServicesTest {

    public static void main(String[] args) {
        PlaylistServices ps = new PlaylistServices();
        
        //Ensuring Proper Output
        String testuser3 = "Test2";
        UserPlaylists testup = ps.getUsersPlaylists(testuser3);
        ArrayList<Playlists> testplist = testup.getPlaylists();
//        Playlists p = ps.getPlaylist("Test Playlist", testuser3);
        //Song addition testing
        Songs testSong = new Songs("As Mist Lay Silent Beneath", "Keep Of Kalessin", "../mp3/bensound-epic.mp3");
//        String addsuccess = ps.addSongsToPlaylistEditor(testSong, testuser3);
//        System.out.println(addsuccess);
//        
        Songs testsong2 = new Songs("lololol", "AAAAAAAAA", "../mp3");
//        String addsuccess2 = ps.addSongsToPlaylistEditor(testsong2, testuser3);
//        System.out.println(addsuccess2);
//
        //Playlist saving testing
//        String success = ps.savePlaylist("Test Playlist", "Test Playlist", testuser3);
//        System.out.println(success);
        
//        //"New" Playlist Testing
//        String newplSuccess = ps.savePlaylist("Wew", "", testuser3);
//        System.out.println(newplSuccess);
        
//        Playlists p2 = ps.getPlaylist("Wew", testuser3);
//        String adds1 = ps.addSongsToPlaylistEditor(testSong, testuser3);
//        System.out.println(adds1);
//        String adds2 = ps.addSongsToPlaylistEditor(testsong2, testuser3);
//        System.out.println(adds2);
//        
//        String newplSave = ps.savePlaylist("Wew", "Wew", testuser3);
//        System.out.println(newplSave);
//        //Song removal testing
//        String rvs = ps.deleteSongsOnPlaylist(testsong2, "Wew", testuser3);
//        String removesuccess = ps.savePlaylist("Wew", "Wew", testuser3);
//        System.out.println(rvs);
//        System.out.println(removesuccess);

        //Playlist deletion testing
        String pdeletesuccess = ps.deletePlaylist("Wew", testuser3);
        System.out.println(pdeletesuccess);
    }

}