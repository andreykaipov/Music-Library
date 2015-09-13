/**
 * @author Andrey Kaipov
 * @version 1.0 October 21, 2014
 * This class exists so we can compare our Album objects not just in terms of
 * album names, but also in terms of artist names.
 */
package music;

import java.util.Comparator;

/**
 * A class for comparing album objects in terms of their artist.
 */
public class ArtistComparator implements Comparator<Album>
{
    @Override
    public int compare(Album a, Album b)
    {
        return ( a.getArtist().compareTo(b.getArtist()) );
    }
}
