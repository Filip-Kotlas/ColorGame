import java.awt.*;

public class Pattern
{
    public Pattern()
    {
        id = 0;
        name = "";
        width = 0;
        height = 0;
        pattern_field = null;
        for( int i = 0; i < 6; i++ )
        {
            points[i] = 0;
        }
    }

    public Pattern( int _id,
                    String _name,
                    int _width,
                    int _height,
                    String pattern_in_string,
                    int points_a_common,
                    int points_a_unique,
                    int points_b_common,
                    int points_b_unique,
                    int points_c_common,
                    int points_c_unique )
    {
        id = _id;
        name = _name;
        width = _width;
        height = _height;
        pattern_field = new Color[height][width];
        fromString( pattern_in_string );
        points[0] = points_a_common;
        points[1] = points_a_unique;
        points[2] = points_b_common;
        points[3] = points_b_unique;
        points[4] = points_c_common;
        points[5] = points_c_unique;
    }

    public Pattern( int _id,
                    String _name,
                    int _width,
                    int _height,
                    int points_a_common,
                    int points_a_unique,
                    int points_b_common,
                    int points_b_unique,
                    int points_c_common,
                    int points_c_unique )
    {
        id = _id;
        name = _name;
        width = _width;
        height = _height;
        pattern_field = new Color[height][width];
        points[0] = points_a_common;
        points[1] = points_a_unique;
        points[2] = points_b_common;
        points[3] = points_b_unique;
        points[4] = points_c_common;
        points[5] = points_c_unique;
    }


    public Pattern( Pattern origin )
    {
        this.id = origin.id;
        this.name = new String( origin.name) ;
        this.width = origin.width;
        this.height = origin.height;
        this.pattern_field = new Color[height][width];
        for( int i = 0; i < this.height; i++ )
        {
            for( int j = 0; j < this.width; j++ )
            {
                this.pattern_field[i][j] = origin.pattern_field[i][j];
            }
        }
        for( int i = 0; i < 6; i++ )
        {
            this.points[i] = origin.points[i];
        }
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        
        for( int i = 0; i < height; i++ )
        {
            for( int j = 0; j < width; j++ )
            {
                if( pattern_field[i][j] == Color.WHITE )
                {
                    result.append('N');
                }
                else if( pattern_field[i][j] == Color.LIGHT_GRAY )
                {
                    result.append('A');
                }
                else if( pattern_field[i][j] == Color.GRAY )
                {
                    result.append('B');
                }
                else if( pattern_field[i][j] == Color.DARK_GRAY )
                {
                    result.append('C');
                }
            }
            result.append( ";" );         
        }
        return result.toString();
    }

    public void fromString(String pattern)
    {
        if(pattern.length() != ( width + 1 ) * height )
        {
            System.out.println("Neodpovídá délka stringu při načítání vzoru ze stringu. Metoda fromString");
            System.out.print("Očekávaná hodnota:");
            System.out.print( (width + 1 ) * height );
            System.out.print( "\n" );
            System.out.print( "Získaná hodnota: " );
            System.out.print( pattern.length() );
            System.out.println( " " );
        }

        String without_semicolon = pattern.replace(";", "");
        char curr_color;
        for( int i = 0; i < height; i++ )
        {
            for( int j = 0; j < width; j++ )
            {
                curr_color = without_semicolon.charAt( j + i * width );
                if( curr_color == 'A' )
                {
                    pattern_field[i][j] = Color.LIGHT_GRAY;
                }
                else if( curr_color == 'B' )
                {
                    pattern_field[i][j] = Color.GRAY;
                }
                else if( curr_color == 'C' )
                {
                    pattern_field[i][j] = Color.DARK_GRAY;
                }
                else if( curr_color == 'N' )
                {
                    pattern_field[i][j] = Color.WHITE;
                }
                else
                {
                    System.out.println("Neodpovídá načtený char. Metoda fromString");
                }
            }
        }
    }

    public void resize()
    {
        pattern_field = new Color[height][width];
    }
    
    public Color getColorOfTile(int i, int j)
    {
        return pattern_field[i][j];
    }

    public char getCharOfTile( int i, int j )
    {
        if( pattern_field[i][j] == Color.WHITE )
        {
            return 'N';
        }
        else if( pattern_field[i][j] == Color.LIGHT_GRAY )
        {
            return 'A';
        }
        else if( pattern_field[i][j] == Color.GRAY )
        {
            return 'B';
        }
        else if( pattern_field[i][j] == Color.DARK_GRAY )
        {
            return 'C';
        }
        else
        {
            return 'O';
        }
    }

    public boolean isEquivalent( Pattern to_compare )
    {
        if( to_compare == null )
        {
            return false;
        }

        if( this.width != to_compare.width || this.height != to_compare.height )
        {
            return false;
        }
        else
        {
            int AisA = 0;
            int AisB = 0;
            int AisC = 0;
            int BisA = 0;
            int BisB = 0;
            int BisC = 0;
            int CisA = 0;
            int CisB = 0;
            int CisC = 0;

            for(int i = 0; i < this.height; i++ )
            {
                for( int j = 0; j < this.width; j++ )
                {
                    if( this.getCharOfTile(i, j) == 'N' )
                    {
                        if( to_compare.getCharOfTile(i, j) != 'N')
                        {
                            return false;
                        }
                        else
                        {
                            continue;
                        }
                    }
                    else if( this.getCharOfTile(i, j) == 'A' )
                    {
                        if( to_compare.getCharOfTile(i, j) == 'A' )
                        {
                            AisA = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'B' )
                        {
                            AisB = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'C' )
                        {
                            AisC = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'N' )
                        {
                            return false;
                        }
                        else
                        {
                            System.out.println("Nastala chyba ve funkci isEquivalent. (1)");
                        }
                    }
                    else if( this.getCharOfTile(i, j) == 'B' )
                    {
                        if( to_compare.getCharOfTile(i, j) == 'A' )
                        {
                            BisA = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'B' )
                        {
                            BisB = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'C' )
                        {
                            BisC = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'N' )
                        {
                            return false;
                        }
                        else
                        {
                            System.out.println("Nastala chyba ve funkci isEquivalent. (2)");
                        }
                    }
                    else if( this.getCharOfTile(i, j) == 'C' )
                    {
                        if( to_compare.getCharOfTile(i, j) == 'A' )
                        {
                            CisA = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'B' )
                        {
                            CisB = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'C' )
                        {
                            CisC = 1;
                        }
                        else if( to_compare.getCharOfTile(i, j) == 'N' )
                        {
                            return false;
                        }
                        else
                        {
                            System.out.println("Nastala chyba ve funkci isEquivalent. (3)");
                        }
                    }
                    else
                        {
                            System.out.println("Nastala chyba ve funkci isEquivalent. (4)");
                        }
                }
            }

            if( AisA + AisB + AisC > 1 )
            {
                return false;
            }
            if( BisA + BisB + BisC > 1 )
            {
                return false;
            }
            if( CisA + CisB + CisC > 1 )
            {
                return false;
            }

            return true;
        }
    }

    public Pattern getMirrored()
    {
        Pattern mirrored_clone = new Pattern( this );
        for( int i = 0; i < this.height; i++ )
        {
            for( int j = 0; j < this.width; j++ )
            {
                mirrored_clone.pattern_field[i][j] = this.pattern_field[i][this.width - j - 1];
            }
        }
        return mirrored_clone;
    }

    public Pattern getTurned()
    {
        Pattern turned_clone = new Pattern( this.id,
                                            this.name,
                                            this.height,
                                            this.width,
                                            this.points[0],
                                            this.points[1],
                                            this.points[2],
                                            this.points[3],
                                            this.points[4],
                                            this.points[5]
                                            );
        for( int i = 0; i < turned_clone.height; i++)
        {
            for( int j = 0; j < turned_clone.width; j++ )
            {
                turned_clone.pattern_field[i][j] = this.pattern_field[this.height - 1 - j][i];
            }
        }
        return turned_clone;
    }

    public int id;
    public String name;
    public int width;
    public int height;
    public Color[][] pattern_field; 
    public int[] points = new int[6];
}


/*

class Points
{
    Points()
    {
        a_common = 0;
        a_unique = 0;
        b_common = 0;
        b_unique = 0;
        c_common = 0;
        c_unique = 0;
    }

    Points( int a_c,
            int a_u,
            int b_c,
            int b_u,
            int c_c,
            int c_u )
    {
        a_common = a_c;
        a_unique = a_u;
        b_common = b_u;
        b_unique = b_u;
        c_common = c_c;
        c_unique = c_u;
    }

    public int a_common;
    public int a_unique;
    public int b_common;
    public int b_unique;
    public int c_common;
    public int c_unique;
}

 */