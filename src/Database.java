import java.io.*;

public class Database
{

    public static void insertRecord( Pattern patt )
    {
        try
        {
            File database = new File( name_of_database );
            database.createNewFile();
            
            FileWriter writer = new FileWriter( name_of_database, true );
            writer.write( patt.id + ":" );
            writer.write( patt.name + ":" );
            writer.write( patt.width + ":" );
            writer.write( patt.height + ":" );
            writer.write( patt.toString() + ":" );
            writer.write( patt.points[0] + ":" );
            writer.write( patt.points[1] + ":" );
            writer.write( patt.points[2] + ":" );
            writer.write( patt.points[3] + ":" );
            writer.write( patt.points[4] + ":" );
            writer.write( patt.points[5] + "\n" );

            writer.close();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        
    }

    public static Pattern find_by_id( int id )
    {
        try
        {
            File database = new File( name_of_database );
            database.createNewFile();

            BufferedReader reader = new BufferedReader( new FileReader( database ) );
            try
            {
                String line;
                int pattern_id;

                while ((line = reader.readLine()) != null)
                {
                    String [] patt_attributes = line.split(":");
                    pattern_id = Integer.parseInt( patt_attributes[0] );
                    if( pattern_id == id )
                    {
                        return new Pattern( pattern_id,
                                            patt_attributes[1],
                                            Integer.parseInt( patt_attributes[2] ),
                                            Integer.parseInt( patt_attributes[3] ),
                                            patt_attributes[4],
                                            Integer.parseInt( patt_attributes[5] ),
                                            Integer.parseInt( patt_attributes[6] ),
                                            Integer.parseInt( patt_attributes[7] ),
                                            Integer.parseInt( patt_attributes[8] ),
                                            Integer.parseInt( patt_attributes[9] ),
                                            Integer.parseInt( patt_attributes[10] ) );
                    }
                }
                return null;
            }
            finally
            {
                reader.close();
            }
        }
        catch( IOException e )
        {
            e.getStackTrace();
            return null;
        }
    }

    public static int get_max_id()
    {
        try
        {
            File database = new File( name_of_database );
            database.createNewFile();

            BufferedReader reader = new BufferedReader( new FileReader( database ) );
            try
            {
                String line;
                int pattern_id = 0;
                int max_id = -1;
                while ((line = reader.readLine()) != null && line != "")
                {
                    pattern_id = Integer.parseInt( line.substring(0, line.indexOf(":") ) );
                    if( pattern_id > max_id )
                    {
                        max_id = pattern_id;
                    }
                    
                }
                return pattern_id;
            }
            finally
            {
                reader.close();
            }
        }
        catch( IOException e )
        {
            e.getStackTrace();
            return -1;
        }
    }

    public static int get_count_of_records()
    {
        try
        {
            File database = new File( name_of_database );
            database.createNewFile();

            BufferedReader reader = new BufferedReader( new FileReader( database ) );
            try
            {
                int counter = 0;
                String line;
                while ((line = reader.readLine()) != null)
                {
                    counter ++;
                }
                return counter;
            }
            finally
            {
                reader.close();
            }
        }
        catch( IOException e )
        {
            e.getStackTrace();
            return -1;
        }
    }
    
    public static void deleteRecordById( int id )
    {
        try
        {
            String content = "";
            BufferedReader reader = new BufferedReader( new FileReader( name_of_database ) );
            try
            {
                String line;
                while ( ( line = reader.readLine() ) != null )
                {
                    int pattern_id = Integer.parseInt( line.substring( 0, line.indexOf(":") ) );
                    if( pattern_id != id )
                    {
                        content += (line + "\n" );
                    }
                }
            }
            finally
            {
                reader.close();
            }

            BufferedWriter writer = new BufferedWriter( new FileWriter( name_of_database ) );
            try
            {
                writer.write( content );
            }
            finally
            {
                writer.close();
            }
        }
        catch( IOException e )
        {
            e.getStackTrace();
        } 
    }


    static public String name_of_database = "patterns.txt";
}
