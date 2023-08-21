import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;




public class ResultsField
{
    public ResultsField()
    {
        size = 0;
        color_field = new Color[0][0];
    }

    public boolean loadFromFile( File file )
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader( file ));
            reader.mark(100);
            String c_line = reader.readLine();
            int new_size = c_line.length();
            resize(new_size);
            reader.reset();

            for(int i = 0; i < size; i++)
            {
                c_line = reader.readLine();
                for(int j = 0; j < size; j++)
                {
                    char tile_color = c_line.charAt(j);
                    switch (tile_color)
                    {
                        case 'z':
                            color_field[i][j] = Color.green;
                            break;
                        case 'm':
                            color_field[i][j] = Color.blue;
                            break;
                        case 'c':
                            color_field[i][j] = Color.red;
                            break;
                        case 'l':
                            color_field[i][j] = Color.yellow;
                            break;
                        case '.':
                            color_field[i][j] = Color.white;
                            break;
                        default:
                            System.out.println("Někde jste v přepisování výsledků udělali chybu. Objevil se znak, který do souboru nepatří.");
                    }
                }
            } 
            reader.close();
        }

        
        catch (IOException e) {
            System.err.println("Nastala chyba při čtení souboru: " + e.getMessage());
        }
        return true;
    }

    public void deleteData()
    {
        resize(0);
    }

    private void resize( int Size )
    {
        size = Size;
        color_field = new Color[size][size];
    }

    public Color getColorOfTile(int i, int j)
    {
        return color_field[i][j];
    }

    public int getNumberOfTiles()
    {
        return size;
    }

    public int getSize()
    {
        return size;
    }

    int size;
    Color[][] color_field;
}
