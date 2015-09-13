/**
 * @author Andrey Kaipov
 * @version 1.0 October 21, 2014
 * This class represents an album object. It is the basis for a music library
 * because we can't listen to music without albums to listen from!
 */
package music;

import java.util.ArrayList;

/**
 * A class that represents an album object.
 */
public class Album implements Comparable
{
    private String artistName;
    private String albumName;
    private ArrayList<Track> tracks;

    /**
     * An album object has an artist, an album name, and tracks on the album.
     * @param artistName whoever wrote the album.
     * @param albumName the album name.
     * @param tracks an ArrayList of tracks found on the album.
     */
    public Album(String artistName, String albumName, ArrayList<Track> tracks)
    {
        this.artistName = artistName.toUpperCase();
        this.albumName = albumName.toUpperCase();
        this.tracks = tracks;
    }    

    /**
     * Gets the album name of our Album.
     * @return the album name of our Album.
     */
    public String getAlbum()
    {
        return albumName;
    }    
    
    /**
     * Gets the artist of our album.
     * @return the artist of our album.
     */
    public String getArtist()
    {
        return artistName;
    }    
           
    /**
     * Gets the tracks of our album.
     * @return the tracks of our album in the form of an ArrayList.
     */
    public ArrayList<Track> getTracks()
    {
        return tracks;
    }
 
    /**
     * Returns our album object in the form of a formatted string.
     * @param albumsFirst
     * true if we want to display the album name of our Album object first.
     * false if we want to display the artist of our Album object first.
     * @return the Album as a string
     */
    public String toString(boolean albumsFirst)
    {
        String names = "", title = "";
        
        for(Track t : tracks)
        {
            title = t.getName();            
            if ( (t.getName()).length() > 31)           //some track names are
                title = title.substring(0, 30) + "..."; //awfully long, so we
                                                        //shorten it if it so.
            names += String.format("%-35s", title);
        }
        
        //for nice-looking output. toUpperCase to standardize output.
        //if we're crazy, we could even remove the underscores in our output.
        if(albumsFirst)
            return String.format("%-40s" + "%-31s" + "%s",
                     albumName, artistName, names);
        else
            return String.format("%-31s" + "%-40s" + "%10s ",
                     artistName, albumName, names);
    }
    
    /**
     * Compares two albums the same way we'd compare two strings.
     * @param otherTrack the albums to be compared.
     * @return (see String method API).
     */
    @Override
    public int compareTo(Object o)
    {        
        Album otherAlbum = (Album) o;
        
        return albumName.compareTo(otherAlbum.albumName);
    }
}
