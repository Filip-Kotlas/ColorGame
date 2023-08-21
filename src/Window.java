import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class Window extends JFrame
{
    public Window()
    {
        try
        {
            initialization();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initialization() throws Exception
    {
        Dimension screen_size = Toolkit.getDefaultToolkit().getScreenSize();
        setSize( new Dimension( screen_size.width/2, screen_size.height/2 ) );
        setLocation( screen_size.width/4, screen_size.height/4 );
        setTitle("Color Game");
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        layout = new CardLayout();
        result_screen = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                drawResultFieldToScreen(g);
            }
        };
        score_screen = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                drawScoreToScreen( this );
            }
        };
        patterns_screen = new JPanel()
        {
            public void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                for( int i = 0; i < this.getComponentCount(); i++ )
                {
                    this.getComponent(i).repaint();
                }
            }
        };
        white_screen = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                drawWhiteToScreen(g);
            }
        };
        this.setLayout( layout );
        this.add( result_screen, "result" );
        this.add( score_screen, "score" );
        this.add( patterns_screen, "patterns" );
        this.add( white_screen, "white" );

        layout.show(this.getContentPane(), "white" );

        initMenuBar();

        results = new ResultsField();

    }

    private void initMenuBar()
    {
        JMenuBar window_bar = new JMenuBar();
        JMenu results_menu = new JMenu("Výsledky hry");
        JMenuItem count_points_item = new JMenuItem("Spočítat body");
        JMenuItem load_results_from_file_item = new JMenuItem("Nahrát výsledky ze souboru");
        JMenuItem delete_results_item = new JMenuItem("Smazat výsledky");
        JMenu patterns_settings_menu = new JMenu("Nastavení vzorů");
        JMenuItem show_patterns_item = new JMenuItem("Ukázat vzory");
        JMenuItem add_pattern_item = new JMenuItem("Přidat vzor");

        // inicializace nabídky
        setJMenuBar(window_bar);
        window_bar.add(results_menu);
        window_bar.add(patterns_settings_menu);
        results_menu.add(count_points_item);
        results_menu.add(load_results_from_file_item);
        results_menu.add(delete_results_item);
        patterns_settings_menu.add(show_patterns_item);
        patterns_settings_menu.add(add_pattern_item);

        // nastavení action listenerů
        count_points_item.addActionListener( new ActionListener()
                                                {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e)
                                                    {
                                                        countPoints();
                                                        drawScoreToScreen( score_screen );
                                                        show("score");
                                                        //repaint();
                                                    }
                                                }
                                            );
        load_results_from_file_item.addActionListener(new ActionListener()
                                                {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e)
                                                    {
                                                        JFileChooser file_chooser = new JFileChooser(".");
                                                        file_chooser.showOpenDialog(null);
                                                        File file = file_chooser.getSelectedFile();
                                                        results.loadFromFile( file );
                                                        show("result");
                                                        //repaint();
                                                    }
                                                }
                                            );
        delete_results_item.addActionListener(new ActionListener()
                                                {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e)
                                                    {
                                                        results.deleteData();
                                                        show("white");
                                                        //repaint();
                                                    }
                                                }
                                            );
        show_patterns_item.addActionListener(new ActionListener()
                                                {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e)
                                                    {
                                                        show("patterns");
                                                        drawPatternsToScreen(patterns_screen);
                                                        //repaint();
                                                        
                                                    }
                                                }
                                            );
        add_pattern_item.addActionListener(new ActionListener()
                                                {
                                                    @Override
                                                    public void actionPerformed(ActionEvent e)
                                                    {
                                                        addPattern();
                                                        drawPatternsToScreen(patterns_screen);
                                                    }
                                                }
                                            );
    }

    private void drawResultFieldToScreen( Graphics screen )
    {
        if( results == null )
        {
            return;
        }

        int offset = 20;
        int functional_width = getSize().width - getInsets().left - getInsets().right;
        int functional_height = getSize().height - getInsets().bottom - getInsets().top - this.getJMenuBar().getHeight();
        int size_of_panel = Math.min( functional_width, functional_height) - offset;
        int size_of_tile = size_of_panel / results.getNumberOfTiles();
        size_of_panel -= size_of_panel % ( size_of_tile * results.getNumberOfTiles() );

        int starting_point_x = ( functional_width - size_of_panel ) / 2 + getInsets().left;
        int starting_point_y = ( functional_height - size_of_panel ) / 2 + getInsets().top - this.getJMenuBar().getHeight();

        for( int i = 0; i < results.getNumberOfTiles(); i++ )
        {
            for( int j = 0; j < results.getNumberOfTiles(); j++)
            {
                screen.setColor( results.getColorOfTile(i, j));
                screen.fillRect(    starting_point_x + j * size_of_tile,
                                    starting_point_y + i * size_of_tile,
                                    size_of_tile,
                                    size_of_tile );

                screen.setColor( Color.BLACK);
                screen.drawRect(    starting_point_x + j * size_of_tile,
                                    starting_point_y + i * size_of_tile,
                                    size_of_tile,
                                    size_of_tile );
            }
        }
        screen.setColor( Color.BLACK );
        screen.drawRect(    starting_point_x,
                            starting_point_y,
                            results.getNumberOfTiles() * size_of_tile,
                            results.getNumberOfTiles() * size_of_tile );
    }

    private void drawScoreToScreen( JPanel screen )
    {
        screen.removeAll();
        screen.revalidate();

        Font font = new Font( "Arial", Font.PLAIN, 30);

        screen.setLayout( new BoxLayout(screen, BoxLayout.Y_AXIS) );
        JPanel teams_panel = new JPanel();
        JPanel label_panel = new JPanel();
        JPanel red_panel = new JPanel();
        JPanel blue_panel = new JPanel();
        JPanel yellow_panel = new JPanel();
        JPanel green_panel = new JPanel();

        JLabel red = new JLabel("Červení");
        JLabel blue = new JLabel("Modří");
        JLabel yellow = new JLabel("Žlutí" );
        JLabel green = new JLabel( "Zelení" );
        JLabel label = new JLabel( "Konečné skóre je" );

        JLabel red_points = new JLabel( Integer.toString( red_score ) );
        JLabel blue_points = new JLabel( Integer.toString( blue_score ) );
        JLabel yellow_points = new JLabel( Integer.toString( yellow_score ) );
        JLabel green_points = new JLabel( Integer.toString( green_score ) );

        red_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        blue_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        yellow_panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        green_panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        teams_panel.setLayout( new BoxLayout( teams_panel, BoxLayout.X_AXIS ) );
        label_panel.setLayout( new BoxLayout( label_panel, BoxLayout.Y_AXIS ) );
        red_panel.setLayout( new BoxLayout( red_panel, BoxLayout.Y_AXIS) );
        blue_panel.setLayout( new BoxLayout( blue_panel, BoxLayout.Y_AXIS) );
        yellow_panel.setLayout( new BoxLayout( yellow_panel, BoxLayout.Y_AXIS) );
        green_panel.setLayout( new BoxLayout( green_panel, BoxLayout.Y_AXIS) );

        red.setAlignmentX(Component.CENTER_ALIGNMENT);
        blue.setAlignmentX(Component.CENTER_ALIGNMENT);
        yellow.setAlignmentX(Component.CENTER_ALIGNMENT);
        green.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        red.setOpaque( true );
        blue.setOpaque( true );
        yellow.setOpaque( true );
        green.setOpaque( true );

        red.setBackground( Color.RED );
        blue.setBackground( Color.BLUE );
        yellow.setBackground( Color.YELLOW );
        green.setBackground( Color.GREEN );

        blue.setForeground( Color.WHITE );
        green.setForeground( Color.WHITE );

        red.setFont(font);
        blue.setFont(font);
        yellow.setFont(font);
        green.setFont(font);

        red_points.setFont(font);
        blue_points.setFont(font);
        yellow_points.setFont(font);
        green_points.setFont(font);

        label.setFont(font);

        label_panel.add(label);

        red_panel.add(red);
        blue_panel.add(blue);
        yellow_panel.add(yellow);
        green_panel.add(green);

        red_panel.add(Box.createVerticalStrut(20));
        blue_panel.add(Box.createVerticalStrut(20));
        yellow_panel.add(Box.createVerticalStrut(20));
        green_panel.add(Box.createVerticalStrut(20));

        red_panel.add( red_points );
        blue_panel.add( blue_points );
        yellow_panel.add( yellow_points );
        green_panel.add( green_points );

        teams_panel.add( red_panel );
        teams_panel.add( blue_panel );
        teams_panel.add( yellow_panel );
        teams_panel.add( green_panel );

        screen.add( Box.createVerticalGlue() );
        screen.add( label_panel );
        screen.add( Box.createVerticalGlue() );
        screen.add( teams_panel );
        screen.add( Box.createVerticalGlue() );

    }

    private void drawPatternsToScreen( JPanel screen )
    {
        screen.removeAll();
        screen.revalidate();
        
        //int count_of_records = Database.get_count_of_records();
        int size = 20;
        int max_id = Database.get_max_id();
        JPanel[] whole_panels = new JPanel[max_id];
        JPanel[] patterns_panels = new JPanel[max_id];
        JButton[] delete_buttons = new JButton[max_id];
        JLabel[] name_of_patterns = new JLabel[max_id];
        screen.setLayout( new FlowLayout( FlowLayout.CENTER, 10, 10) );

        
        if( max_id == 0 )
        {
            JPanel panel_in_screen = new JPanel()
            {
                @Override protected void paintComponent( Graphics panel )
                {
                    super.paintComponent(panel);
                    Font font = new Font("Arial", Font.PLAIN, 30);
                    panel.setFont(font);
                    FontMetrics metrics = panel.getFontMetrics();
                    String message = "V databázi nejsou uloženy žádné vzory.";
                    panel.drawString(    message,
                                            ( screen.getWidth() - metrics.stringWidth( message ) )/ 2,
                                            ( screen.getHeight() - metrics.getHeight() ) / 2 );
                    this.setPreferredSize( new Dimension(screen.getWidth(), screen.getHeight() ) );
                }
            };
            panel_in_screen.setPreferredSize( new Dimension(screen.getWidth(), screen.getHeight() ) );
            screen.add(panel_in_screen);
        }
        

        for( int i = 0; i < max_id; i++ )
        {
            final Pattern curr_pattern = Database.find_by_id(i + 1); //SQL idnexuje od 1
            if( curr_pattern != null )
            {
                final int index = i;
                whole_panels[index] = new JPanel();
                patterns_panels[index] = new JPanel()
                {
                    @Override protected void paintComponent( Graphics panel )
                    {
                        super.paintComponent( panel );
                        int width = this.getSize().width;
                        int height = this.getSize().height;
                        int dim_x = curr_pattern.width;
                        int dim_y = curr_pattern.height;
                        int size_of_tile = Math.min( width / dim_x, height / dim_y );

                        Font font = new Font( "Arial", Font.BOLD, size_of_tile);
                        panel.setFont( font );
                        //Graphics g = this.getGraphics();

                        FontMetrics fontMetrics = panel.getFontMetrics(font);

                        for( int k = 0; k < dim_y; k++ )
                        {
                            for( int j = 0; j < dim_x; j++ )
                            {
                                panel.setColor( curr_pattern.getColorOfTile(k, j) );
                                panel.fillRect( j * size_of_tile, k * size_of_tile, size_of_tile, size_of_tile );

                                panel.setColor( Color.BLACK );
                                panel.drawRect( j * size_of_tile, k * size_of_tile, size_of_tile, size_of_tile );

                                char character = curr_pattern.getCharOfTile( k, j );
                                if( character != 'N' ) 
                                {
                                    if( character == 'C' )
                                    {
                                        panel.setColor( Color.WHITE );
                                    }
                                    panel.drawString(   Character.toString( character ),
                                                        (int) ( ( j + ( size_of_tile - fontMetrics.charWidth(character) ) / 2.0 / size_of_tile ) * size_of_tile ),
                                                        (int) ( ( k + 1 + ( size_of_tile - fontMetrics.getHeight() ) / 2.0 / size_of_tile ) * size_of_tile ) );
                                    if( character == 'C' )
                                    {
                                        panel.setColor( Color.BLACK );
                                    }
                                }
                            }
                        }
                        //panel.drawRect( 0, 0, size_of_tile * dim_x, size_of_tile * dim_y );
                    }
                };
                patterns_panels[index].setPreferredSize( new Dimension(curr_pattern.width * size, curr_pattern.height * size ) );

                delete_buttons[index] = new JButton("Smazat");
                delete_buttons[index].addActionListener( new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e)
                    {
                        int want_to_delete = JOptionPane.showConfirmDialog( null,
                                                                            "Opravdu chcete tento vzor smazat?",
                                                                            null,
                                                                            JOptionPane.YES_NO_OPTION,
                                                                            JOptionPane.QUESTION_MESSAGE );
                        if( want_to_delete == JOptionPane.YES_OPTION )
                        {
                            Database.deleteRecordById(index + 1);
                            drawPatternsToScreen( screen );
                            screen.repaint();
                        }
                    }
                });

                name_of_patterns[index] = new JLabel( curr_pattern.name );

                int to_be_width = Math.max( Math.max( curr_pattern.width * size, delete_buttons[index].getPreferredSize().width ), name_of_patterns[index].getPreferredSize().width );
                whole_panels[index].setSize( new Dimension( to_be_width, curr_pattern.height * size + 90 ) );
                whole_panels[index].setLayout( new BoxLayout( whole_panels[index], BoxLayout.Y_AXIS ));

                JPanel horizontal_panel = new JPanel();
                horizontal_panel.setLayout( new FlowLayout(FlowLayout.CENTER) );
                horizontal_panel.add( name_of_patterns[index] );
                whole_panels[index].add( horizontal_panel );

                horizontal_panel = new JPanel();
                horizontal_panel.setLayout( new FlowLayout(FlowLayout.CENTER) );
                horizontal_panel.add( patterns_panels[index] );
                whole_panels[index].add( horizontal_panel );

                horizontal_panel = new JPanel();
                horizontal_panel.setLayout( new FlowLayout(FlowLayout.CENTER) );
                horizontal_panel.add( delete_buttons[index] );
                whole_panels[index].add( horizontal_panel );

                screen.add( whole_panels[index] );
            }
        }
        screen.repaint();
    }

    private void drawWhiteToScreen( Graphics screen )
    {

    }

    @Override public void paint( Graphics screen )
    {
        super.paint(screen);

        result_screen.repaint();
        score_screen.repaint();
        patterns_screen.repaint();
        white_screen.repaint();
    }

    private void addPattern()
    {
        Pattern patt = new Pattern();

        //Nastavení Id
        patt.id = Database.get_max_id() + 1;

        //Nastavení jména
        patt.name = JOptionPane.showInputDialog(    null,
                                                    "Zadejte jméno nového vzoru.",
                                                    "Jméno",
                                                    JOptionPane.QUESTION_MESSAGE );
        if (patt.name == null )
        {
            return;
        }

        //Spinner pro šířku
        SpinnerModel modelw = new SpinnerNumberModel( 1, 1,10, 1 );
        JSpinner width = new JSpinner( modelw );   //width
        JPanel panel_w = new JPanel( new FlowLayout() );
        panel_w.add( new JLabel( "Šířka: " ) );
        panel_w.add( width );

        //Spinner pro výšku
        SpinnerModel modelh = new SpinnerNumberModel( 1, 1, 10, 1 );
        JSpinner height = new JSpinner( modelh );   //height
        JPanel panel_h = new JPanel( new FlowLayout() );
        panel_h.add( new JLabel( "Výška: " ) );
        panel_h.add( height );

        Object[] dimension_options = { panel_w, panel_h, "Pokračovat" };

        //Nastavení rozměrů
        JOptionPane.showOptionDialog(   null,
                                        "Zadejte rozměry nového vzoru.",
                                        "Rozměry",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        dimension_options,
                                        dimension_options[2] );
        patt.width = (int) width.getValue();
        patt.height = (int) height.getValue();
        patt.resize();

        //Nastavení samotného vzoru
        JPanel pattern_panel = new JPanel( new GridLayout( patt.height, patt.width ) );
        for( int i = 0; i < patt.width * patt.height; i++ )
        {
            JButton butt = new JButton( " " );
            butt.setFont( new Font( "Consolas", Font.BOLD, 16 ) );
            butt.setBackground( Color.WHITE );

            butt.addActionListener( new ActionListener()
                                    {
                                        @Override
                                        public void actionPerformed(ActionEvent e)
                                        {
                                            if( butt.getBackground() == Color.WHITE )
                                            {
                                                butt.setBackground( Color.LIGHT_GRAY );
                                                butt.setText("A");
                                            }
                                            else if( butt.getBackground() == Color.LIGHT_GRAY )
                                            {
                                                butt.setBackground( Color.GRAY );
                                                butt.setText("B");   
                                            }
                                            else if( butt.getBackground() == Color.GRAY )
                                            {
                                                butt.setBackground( Color.DARK_GRAY );
                                                butt.setForeground( Color.WHITE );
                                                butt.setText("C");
                                            }
                                            else if( butt.getBackground() == Color.DARK_GRAY )
                                            {
                                                butt.setBackground( Color.WHITE );
                                                butt.setForeground( Color.BLACK );
                                                butt.setText(" ");
                                            }
                                        }
                                    } );
        pattern_panel.add(butt);
        }

        Object[] pattern_options = { pattern_panel, "Pokračovat" };
        JOptionPane.showOptionDialog(   null,
                                        "Zadejte vzor.",
                                        "Vzor",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.PLAIN_MESSAGE,
                                        null,
                                        pattern_options,
                                        pattern_options[1] );

        
        for(int i = 0; i < patt.height; i++)
        {
            for( int j = 0; j < patt.width; j++ )
            {
                patt.pattern_field[i][j] = pattern_panel.getComponent( i * patt.width + j ).getBackground();
            }
        }

        //Nastavení bodového ohodnocení
        SpinnerModel model[] = new SpinnerModel[6];
        JSpinner spinner[] = new JSpinner[6];
        JPanel score_panel[] = new JPanel[6];
        for( int i = 0; i < 6; i++ )
        {
            model[i] = new SpinnerNumberModel(1, -100, 200, 1);
            spinner[i] = new JSpinner( model[i] );
            score_panel[i] = new JPanel( new FlowLayout() );
        }
        score_panel[0].add( new JLabel("Obyč. A: ") );
        score_panel[1].add( new JLabel("Unikát A: ") );
        score_panel[2].add( new JLabel("Obyč. B: ") );
        score_panel[3].add( new JLabel("Unikát B: ") );
        score_panel[4].add( new JLabel("Obyč. C: ") );
        score_panel[5].add( new JLabel("Unikát C: ") );
        for(int i = 0; i < 6; i++)
        {
            score_panel[i].add( spinner[i] );
        }
        
        Object[] score_options = {  score_panel[0],
                                    score_panel[1],
                                    score_panel[2],
                                    score_panel[3],
                                    score_panel[4],
                                    score_panel[5],
                                    "Přidat" };

        JOptionPane.showOptionDialog(   null,
                                        "Zadejte, jak je vzor ohodnocen.",
                                        "Body",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        score_options,
                                        score_options[6]
                                        );
        for(int i = 0; i < 6; i++ )
        {
            patt.points[i] = (int) spinner[i].getValue();
        }

        Database.insertRecord(patt);
    }

    private void show( String card )
    {
        layout.show(this.getContentPane(), card );
    }

    private void countPoints()
    {
        red_score = 0;
        blue_score = 0;
        yellow_score = 0;
        green_score = 0;

        if(results.getSize() == 0)
        {
            return;
        }

        Pattern pattern_variants[] = new Pattern[8];
        for( int i = 0; i < Database.get_max_id(); i++ )
        {
            pattern_variants[0] = Database.find_by_id( i + 1 ); //SQL indexuje od 1
            if( pattern_variants[0] == null )
            {
                continue;
            }

            pattern_variants[1] = pattern_variants[0].getTurned();
            pattern_variants[2] = pattern_variants[1].getTurned();
            pattern_variants[3] = pattern_variants[2].getTurned();
            pattern_variants[4] = pattern_variants[3].getMirrored();
            pattern_variants[5] = pattern_variants[4].getTurned();
            pattern_variants[6] = pattern_variants[5].getTurned();
            pattern_variants[7] = pattern_variants[6].getTurned();

            for( int j = 0; j < 7; j++ )
            {
                for( int k = j + 1; k < 8; k++)
                {
                    if( pattern_variants[j] != null )
                    {
                        if( pattern_variants[j].isEquivalent( pattern_variants[k] ) )
                        {
                            pattern_variants[k] = null;
                        }
                    }
                }
            }

            int count_of_occurances = 0;
            Integer point_difference[] = new Integer[4];
            for(int k = 0; k < 4; k++)
            {
                point_difference[k] = 0;
            }

            for( int j = 0; j < 8; j++)
            {
                if( pattern_variants[j] != null )
                {
                    count_of_occurances += addUpCommonPointsForPattern( pattern_variants[j], point_difference );
                }
            }

            if( count_of_occurances == 1 ) //přidá body za unikátní výskyt
            {
                red_score += point_difference[0];
                blue_score += point_difference[1];
                yellow_score += point_difference[2];
                green_score += point_difference[3];
            }

        }
    }

    private int addUpCommonPointsForPattern( Pattern patt, Integer[] point_diff )
    {
        int count_of_occurances = 0;

        for( int i = 0; i < results.getSize() - patt.height + 1; i++ )
        {
            for( int j = 0; j < results.getSize() - patt.width + 1; j++ )
            {
                if( checkMatch(patt, i, j, point_diff ) )
                {
                    count_of_occurances++;
                }

            }
        }
        return count_of_occurances;
    }

    private boolean checkMatch( Pattern patt, int i, int j, Integer[] point_diff ) //současně přičte skóre pro neunikátní pattern
    {
        Color A = Color.WHITE;
        Color B = Color.WHITE;
        Color C = Color.WHITE;
        boolean A_is_set = false;
        boolean B_is_set = false;
        boolean C_is_set = false;

        for( int k = 0; k < patt.height; k++ )
        {
            for( int l = 0; l < patt.width; l++ )
            {
                if( patt.getColorOfTile(k, l) == Color.WHITE )
                {
                    continue;
                }
                else if( results.getColorOfTile(i + k, j + l) == Color.WHITE )
                {
                    return false;
                }
                else if( patt.getColorOfTile(k, l) == Color.LIGHT_GRAY )
                {
                    if( !A_is_set )
                    {
                        A = results.getColorOfTile( i + k, j + l );
                        A_is_set = true;
                    }
                    else
                    {
                        if( A != results.getColorOfTile( i + k, j + l ) )
                        {
                            return false;
                        }
                    }
                }
                else if( patt.getColorOfTile(k, l) == Color.GRAY )
                {
                    if( !B_is_set )
                    {
                        B = results.getColorOfTile( i + k, j + l );
                        B_is_set = true;
                    }
                    else
                    {
                        if( B != results.getColorOfTile( i + k, j + l ) )
                        {
                            return false;
                        }
                    }
                }
                else if( patt.getColorOfTile(k, l) == Color.DARK_GRAY )
                {
                    if( !C_is_set )
                    {
                        C = results.getColorOfTile( i + k, j + l );
                        C_is_set = true;
                    }
                    else
                    {
                        if( C != results.getColorOfTile( i + k, j + l ) )
                        {
                            return false;
                        }
                    }
                }
            }
        }

        if( A == B || B == C || C == A )
        {
            return false;
        }

        if( A == Color.RED )
        {
            red_score += patt.points[0];
            point_diff[0] = patt.points[1] - patt.points[0];
        }
        else if( A == Color.BLUE )
        {
            blue_score += patt.points[0];
            point_diff[1] = patt.points[1] - patt.points[0];
        }
        else if( A == Color.YELLOW )
        {
            yellow_score += patt.points[0];
            point_diff[2] = patt.points[1] - patt.points[0];
        }
        else if( A == Color.GREEN )
        {
            green_score += patt.points[0];
            point_diff[3] = patt.points[1] - patt.points[0];
        }

        if( B == Color.RED )
        {
            red_score += patt.points[2];
            point_diff[0] = patt.points[3] - patt.points[2];
        }        
        else if( B == Color.BLUE )
        {
            blue_score += patt.points[2];
            point_diff[1] = patt.points[3] - patt.points[2];
        }
        else if( B == Color.YELLOW )
        {
            yellow_score += patt.points[2];
            point_diff[2] = patt.points[3] - patt.points[2];
        }
        else if( B == Color.GREEN )
        {
            green_score += patt.points[2];
            point_diff[3] = patt.points[3] - patt.points[2];
        }
                    
        if( C == Color.RED )
        {
            red_score += patt.points[4];
            point_diff[0] = patt.points[5] - patt.points[4];
        }
        else if( C == Color.BLUE )
        {
            blue_score += patt.points[4];
            point_diff[1] = patt.points[5] - patt.points[4];
        }
        else if( C == Color.YELLOW )
        {
            yellow_score += patt.points[4];
            point_diff[2] = patt.points[5] - patt.points[4];
        }
        else if( C == Color.GREEN )
        {
            green_score += patt.points[4];
            point_diff[3] = patt.points[5] - patt.points[4];
        }

        if( C == Color.GREEN || A == Color.GREEN || B == Color.GREEN )
        {
            System.out.println( patt.name );
        }

        return true;
    }
    
    
    ResultsField results;

    int red_score = 0; 
    int blue_score = 0;
    int yellow_score = 0;
    int green_score = 0;

    // Layout
    CardLayout layout;
    JPanel result_screen;
    JPanel score_screen;
    JPanel patterns_screen;
    JPanel white_screen;

    // Pole s výsledky
    JPanel results_panel;

}