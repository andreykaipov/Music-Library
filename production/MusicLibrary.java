/**
 * @author Andrey Kaipov
 * @version 2.5 October 21 2014 -> 9/13/2015
 * This is a music library. 
 * After we read the original input file, a menu is presented to the user.
 * This menu is user-friendly as it is self-navigating, and includes detailed
 * instructions from within. 
 */
package music;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class MusicLibrary
{
   private String fileName;
   private ArrayList<Album> catalog = new ArrayList<>();
    
   public static void main(String[] args)
   {
      MusicLibrary myMusic = new MusicLibrary();
      myMusic.readFile();
      myMusic.promptMenu();
   }
   
   /**
    * Simply reads our file. Dissects the file line by line, parsing the
    * necessary information from it.
    * Then, creates an album object from each line.
    * Also sorts the tracks in alphabetical order immediately once gotten.
    */
   public void readFile()
   {
      try {
         fileName = JOptionPane.showInputDialog(null,
                 "Please enter the file you'd like to access."
               + "\nHint: You want to access 'catalog.txt'!");
           
         File aFile = new File(fileName);
         Scanner myFile = new Scanner(aFile);
          
         String artist, albumName;  
         while(myFile.hasNext())
         {                                      //first we scan the document
            String line = myFile.nextLine();    //for the next line. then we
            Scanner myLine = new Scanner(line); //scan that line for our info.
              
            artist = myLine.next();
            albumName  = myLine.next();
            ArrayList<Track> tracks = new ArrayList<>();
                
            while(myLine.hasNext()) //to make sure we get all the tracks
            {                       //that are on the line.
               Track song = new Track(myLine.next());
               tracks.add(song);
            }
            myLine.close();
            Collections.sort(tracks); //sort the list of tracks as soon as we
                                      //get all of them included in the album.
            Album anAlbum = new Album(artist, albumName, tracks);
            catalog.add(anAlbum);
         }
         myFile.close();
      }
      catch (NullPointerException e) {
         System.err.println("You failed to enter a file name!");
         System.exit(1);
      }
      catch (FileNotFoundException e) {
         System.err.println("Sorry, no file under the name '" + fileName + "' exists!");
         System.exit(2);
      }
   }
   
   /**
    * The menu for our music library. It has several options.
    * This menu is recursive: once we carry out an option, 
    *                         the menu is prompted again.
    * This is my favorite method.
    */
   public void promptMenu()
   {
      Object[] options = {"Display by Albums-First", "Display by Artists-First",
                          "Search by Album Title", "Search by Artist Name",
                          "Add Album to Catalog"};
        
      String action = (String) JOptionPane.showInputDialog(null, 
                      "Select an option:", "Music Library Menu",
                   JOptionPane.INFORMATION_MESSAGE,null,options,options[0]);
      try {
         switch(action)
         {
            case "Display by Albums-First"  : displayByAlbums();
               break;
            case "Display by Artists-First" : displayByArtists();
               break;
            case "Search by Album Title"    : searchByAlbum();
               break;
            case "Search by Artist Name"    : searchByArtist();
               break;
            case "Add Album to Catalog"     : addAlbum(fileName);
               break;
            default                         : //do nothing
               break;
         }
      }
      catch(NullPointerException e) { //NPE is thrown if user Cancels or X's.
         int quit = JOptionPane.showConfirmDialog(null, "Do you want to quit?",
                                        "Quit Menu",JOptionPane.YES_NO_OPTION);
         if(quit == 0)
         {
            JOptionPane.showMessageDialog(null, "Thanks for using the library!");     
            System.exit(0);
         }
         else
            promptMenu();
      }
      catch (FileNotFoundException e) { //for the case of addAlbum(fileName);
         System.err.println("Sorry, no file under the name '" + fileName + "' exists!");
         System.exit(2);
      }
      promptMenu();
   }

   /**
    * Searches our music catalog for a specific artist using binary search.
    */
   public void searchByAlbum()
   {
      String input = JOptionPane.showInputDialog(null,"Enter the album you'd like to search for:");
      input = input.replaceAll(" ", "_").toUpperCase();
        
      Collections.sort(catalog);
      int index = Collections.binarySearch(catalog, new Album("",input,null), null);
      if(index >= 0)
         System.out.println("Album:\t" + catalog.get(index).getAlbum()
                        + "\nArtist: " + catalog.get(index).getArtist()+"\n");
      else System.err.println("Album '" + input + "' not found!\n");    
   }
    
   /**
    * Searches our music catalog for a specific artist using binary search
    *                                                 (plus a little extra).
    */
   public void searchByArtist()
   {
      String input = JOptionPane.showInputDialog(null,"Enter the artist you'd like to search for:");
      input = input.replaceAll(" ", "_").toUpperCase(); //standardize output.
        
      Collections.sort(catalog, new ArtistComparator());
      int index = Collections.binarySearch(catalog, 
                          new Album(input,"",null), new ArtistComparator());
      //Since an artist could be listed multiple times, there is *no guarantee*
      //which artist index we find. So, we have to look at the artists
      //left and right of the initial match until we stop matching.
      if(index >= 0)
      {
         Album initial = catalog.get(index);
         String artist = initial.getArtist();
         
         ArrayList<Album> foundAlbums = new ArrayList<>();
         //this ArrayList will have only the albums of our found artist         
         foundAlbums.add(initial);
         try {  
            int j = index, k = index;
            while(artist.equalsIgnoreCase(catalog.get(j+1).getArtist()))
            {
               foundAlbums.add(catalog.get(j+1));
               j++;
            }
            while(artist.equalsIgnoreCase(catalog.get(k-1).getArtist()))
            {
               foundAlbums.add(catalog.get(k-1));
               k--;
            }
         }
         catch(IndexOutOfBoundsException e) {
            //happens on the last round of the while-loop test expressions
            //don't do anyhting because now we need to print out our albums.
         }
         System.out.print("Artist: " + artist + "\nAlbums: ");
         for(Album a : foundAlbums)
            System.out.print(a.getAlbum() + "\n\t");
         System.out.println();
      }
      else System.err.println("Artist '" + input + "' not found!\n");              
    }
      
   /**
    * Adds a new album to our catalog. Also appends it to the actual .txt file.
    * @param fileName the file name of the file we want to append the info to
    * @throws FileNotFoundException if the file name does not exist
    */
   public void addAlbum(String fileName) throws FileNotFoundException
   {
      String albumTitle = JOptionPane.showInputDialog(null, "Enter an album name:");
      albumTitle = albumTitle.replaceAll(" ", "_");
        
      Collections.sort(catalog);
      int index = Collections.binarySearch(catalog, new Album("",albumTitle,null), null);
      //this is to make sure we do not have duplicate albums
      if(index < 0) 
      {
         String artistName = JOptionPane.showInputDialog(null, "Enter the artist name:");
         artistName = artistName.replaceAll(" ", "_");
            
         FileOutputStream fos = new FileOutputStream(new File(fileName),true);
         PrintWriter pw = new PrintWriter(fos);
          
         String[] buttons = {"I understand.", "I do not understand."};
         int selection = JOptionPane.showOptionDialog(null, "You will now "
                 + "enter the songs that belong on the album.\nEnter ONE "
                 + "song at a time. When finished adding songs, just "
                 + "press cancel.", "Instructions.", JOptionPane.YES_OPTION,
                 JOptionPane.WARNING_MESSAGE, null, buttons, buttons[0]);
          
        if(selection == 0) //user understands the instructions
        {
           pw.print(artistName + " " + albumTitle);
           ArrayList<Track> albumsTracks = new ArrayList<>();

           String song = JOptionPane.showInputDialog(null, "Submit song #1:");
           for(int i=2; song != null; i++)
           {
              song = song.replaceAll(" ", "_");
              pw.print(" " + song);
              albumsTracks.add(new Track(song));
              song = JOptionPane.showInputDialog(null, "Submit song #"+i+":");
           }
           pw.println();
           pw.close();
           JOptionPane.showMessageDialog(null,
                   "Album has successfully been written to '" + fileName +"'!");
           
           Album newAlbum = new Album(artistName, albumTitle, albumsTracks);
           catalog.add(newAlbum);     //here we put our new album in the 
           Collections.sort(catalog); //general catalog and sort it by albums.
         }
         else //the user didn't understand the above instructions.
              //so the artist and album that the user entered is not printed.
              //since promptMenu() is recursive, we go back to the menu.
          JOptionPane.showMessageDialog(null, "Going back to the menu now...");
      }
      else //duplicate album found!
         System.out.println("Album '" + albumTitle + "' already exists in the catalog!");
    }
   
  /**
    * Sorts our catalog by albums, and displays the catalog by albums first by
    * iterating over the sorted catalog.
    */
   public void displayByAlbums()
   {
      Collections.sort(catalog);
      
      System.out.printf("%-39s %-30s %s %n","ALBUM", "ARTIST", "TRACKS");  
      for(Album a : catalog)
         System.out.println(a.toString(true)); //since true, albums are first. 
      
      for(int i=0; i<30; i++)
         System.out.print("*"); //line of asterisks
      System.out.print("\n\n");
   }
    
  /**
    * Sorts our catalog by artists, and displays the catalog by artists first
    * by iterating over the sorted catalog.
    */
   public void displayByArtists()
   {
      Collections.sort(catalog, new ArtistComparator());
      
      System.out.printf("%-30s %-39s %s %n","ARTIST", "ALBUM", "TRACKS");   
      for(Album a : catalog)
         System.out.println(a.toString(false)); //prints the artist first.
      
      for(int i=1; i<30; i++)
         System.out.print("*");
      System.out.print("\n\n");
   }
}
