import java.util.*;

/**
 * Classe Game
 * 
 * Classe principale du TP3
 * 
 * @author Gabin CARON
 */
public class Game
{
    private static int row, col;
    private static char[][] config;
    private static Map<char[][], Integer> map = new HashMap<>();

    public static void main(String args[])
    {
        Scanner scan = new Scanner( System.in );
        
        System.out.println("Entrer le nombre de lignes :");
        row = scan.nextInt();
        
        System.out.println("Entrer le nombre de colonnes :");
        col = scan.nextInt();
        
        config = new char[row][col];
        System.out.println("Entrer les positions des pions :");
        scan.nextLine();
        int i=0;
        while(i<row)
        {
            String position = scan.nextLine();
            for(int j=0; j<col; j++)
            {
                config[i][j] = position.charAt(j);
            }
            i++;
        }

        //System.out.println(algoNaif(config, true));
        System.out.println(algoDynamique(config, true));
    }

    public static int algoNaif(char[][] conf, boolean actif)
    {
        int res;
        List<char[][]> list = new ArrayList<>();
        
        if(actif)
        {
            list = playWhite(conf);
        } else {
            list = playBlack(conf);
        }

        if(list.isEmpty())
        {
            return 0;
        }

        int t[] = new int[list.size()];
        for(int i=0; i<list.size(); i++)
        {
            t[i] = algoNaif(list.get(i), !actif);
        }

        boolean ennemy = false;
        res = t[0];
        for(int i=0; i<t.length; i++)
        {
            int valCoup = t[i];
            if(valCoup <= 0)
            {
                if(ennemy && valCoup>res)
                {
                    res = valCoup;
                }
                ennemy = true;
            } else
            {
                if(!ennemy && valCoup>res)
                {
                    res = valCoup;
                }
            }
        }

        if(res <= 0)
        {
            return -(res-1);
        } else
        {
            return -(res+1);
        }
    }

    public static int algoDynamique(char[][] conf, boolean actif)
    {
        if(map.containsKey(conf))
        {
            return map.get(conf);
        }
        int res;
        List<char[][]> list = new ArrayList<>();
        if(actif)
        {
            list = playWhite(conf);
        } else
        {
            list = playBlack(conf);
        }

        if(list.isEmpty())
        {
            return 0;
        }
        
        int t[] = new int[list.size()];
        for(int i=0; i<list.size(); i++)
        {
            t[i] = algoDynamique(list.get(i), !actif);
            map.put(list.get(i), t[i]);
        }

        boolean ennemy = false;
        res = t[0];
        for(int i=0; i<t.length; i++)
        {
            int valCoup = t[i];
            if(valCoup<=0)
            {
                if(ennemy && valCoup>res)
                {
                    res = valCoup;
                }
                ennemy = true;
            } else
            {
                if(!ennemy && valCoup>res)
                {
                    res = valCoup;
                }
            }
        }

        if(res<=0)
        {
            return -(res-1);
        } else
        {
            return -(res+1);
        }
    }

    public static char[][] copy(char[][] conf)
    {
        char[][] res = new char[row][col];
        for(int i=0; i<row; i++)
        {
            for(int j=0; j<col; j++)
            {
                res[i][j] = conf[i][j];
            }
        }
        return res;
    }

    public static List<char[][]> playWhite(char[][] conf)
    {
        List<char[][]> list = new ArrayList<>();
        
        if(winWhite(conf) || winBlack(conf))
        {
            return list;
        }

        for(int i=0; i<row; i++)
        {
            for(int j=0; j<col; j++)
            {
                if(conf[i][j]=='P')
                {
                    if(conf[i-1][j]==' ')
                    {
                        char[][] tmp = copy(conf);
                        tmp[i][j] = ' ';
                        tmp[i-1][j-1] = 'P';
                        list.add(tmp);
                    }

                    if(j>0 && conf[i-1][j-1]=='p')
                    {
                        char[][] tmp = copy(conf);
                        tmp[i][j] = ' ';
                        tmp[i-1][j-1] = 'P';
                        list.add(tmp);
                    }

                    if(j<col-2 && conf[i-1][j+1]=='p')
                    {
                        char[][] tmp = copy(conf);
                        tmp[i][j] = ' ';
                        tmp[i-1][j+1] = 'P';
                        list.add(tmp);
                    }
                }
            }
        }
        return list;
    }

    public static List<char[][]> playBlack(char[][] conf)
    {
        List<char[][]> list = new ArrayList<>();
        
        if(winBlack(conf) || winWhite(conf))
        {
            return list;
        }

        for(int i=0; i<row; i++)
        {
            for(int j=0; j<col; j++)
            {
                if(conf[i][j]=='p')
                {
                    if (conf[i+1][j]==' ')
                    {
                        char[][] tmp = copy(conf);
                        tmp[i][j] = ' ';
                        tmp[i+1][j] = 'p';
                        list.add(tmp);
                    }

                    if(j>0 && conf[i+1][j-1]=='P')
                    {
                        char[][] tmp = copy(conf);
                        tmp[i][j] = ' ';
                        tmp[i+1][j-1] = 'p';
                        list.add(tmp);
                    }

                    if(j<col-2 && conf[i+1][j+1]=='P')
                    {
                        char[][] tmp = copy(conf);
                        tmp[i][j] = ' ';
                        tmp[i+1][j+1] = 'p';
                        list.add(tmp);
                    }
                }
            }
        }
        return list;
    }


    public static boolean playableWhite(char[][] conf, int i, int j)
    {
      return (i-1>=0 && conf[i-1][j]==' ')
              || (i-1>=0 && j+1<col && conf[i-1][j+1]=='P')
              || (i-1>=0 && j-1>=0 && conf[i-1][j-1]=='P');
    }

    public static boolean playableBlack(char[][] conf, int i, int j)
    {
      return (i+1<row && conf[i+1][j]==' ')
              || (i+1<row && j+1<col && conf[i+1][j+1]=='p')
              || (i+1<row && j-1>=0 && conf[i+1][j-1]=='p');
    }

    public static boolean winWhite(char[][] conf)
    {
        for(int i=0; i<col; i++)
        {
            if(conf[0][i]=='P')
            {
                return true;
            }
        }

        for(int i=0; i<row; i++)
        {
            for(int j=0; j<col; j++)
            {
                if(conf[i][j]=='p')
                {
                    if(playableBlack(conf, i, j))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean winBlack(char[][] conf)
    {
        for(int i=0; i<col; i++)
        {
            if(conf[row-1][i]=='p')
            {
                return true;
            }
        }

        for(int i=0; i<row; i++)
        {
            for(int j=0; j<col; j++)
            {
                if(conf[i][j]=='P')
                {
                    if(playableWhite(conf, i, j))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}