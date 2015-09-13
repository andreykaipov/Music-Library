/**
 * @author Andrey Kaipov
 * @version 1.0 October 21, 2014
 * This class represents a Track object. As of now, it has just a name.
 * However, we can easily add the length of the track as a parameter, etc.
 * Then we would need a TrackComparator for comparing lengths!
 */
package music;

/**
 * A class representing a track (song) object.
 * An album object will have multiple songs on it in the form of an ArrayList.
 */
public class Track implements Comparable
{
    private String track;

    /**
     * A track object has a name. 
     * We can add other parameters later like rating and length if necessary.
     */
    public Track(String title)
    {
        track = title;
    }
    
    public String getName()
    {
        return track;
    }
    
    /**
     * Compares two tracks the same way we'd compare two strings.
     * @param otherTrack the track to be compared.
     * @return (see String method API).
     */
    @Override
    public int compareTo(Object o)
    {
        Track otherTrack = (Track) o;
        return track.compareTo(otherTrack.track);
    }
}
