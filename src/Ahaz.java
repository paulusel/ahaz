import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Ahaz {

    private static final char ETHIOPIC_ONE          = 0x1369;
    private static final char ETHIOPIC_TEN          = 0x1372;
    private static final char ETHIOPIC_HUNDRED      = 0x137B;
    private static final char ETHIOPIC_TEN_THOUSAND = 0x137C;

    public static String toGeez( String asciiNumberString ) throws IllegalArgumentException {
        int n = asciiNumberString.length() - 1;

        if ( (n%2) == 0 ) {
            asciiNumberString = "0" + asciiNumberString;
            n++;
        }

        String ethioNumberString = "";
        char asciiOne, asciiTen, ethioOne, ethioTen;

        for ( int place = n; place >= 0; place-- ) {
            asciiOne = asciiTen = ethioOne = ethioTen = 0x0;

            asciiTen = asciiNumberString.charAt( n - place );
            place--;
            asciiOne = asciiNumberString.charAt( n - place );

            if( !(Character.isDigit(asciiOne) && Character.isDigit(asciiTen)) )
                throw new IllegalArgumentException();

            if ( asciiOne != '0' )
            ethioOne = (char)((int)asciiOne + ( ETHIOPIC_ONE - '1' ));

            if ( asciiTen != '0' )
            ethioTen = (char)((int)asciiTen + ( ETHIOPIC_TEN - '1'));

            int pos = ( place % 4 ) / 2;

            char sep = ( place != 0 )
                        ? ( pos != 0 )
                            ? ( ( ethioOne != 0x0 ) || ( ethioTen != 0x0 ) )
                                ? ETHIOPIC_HUNDRED
                                :  0x0
                            : ETHIOPIC_TEN_THOUSAND
                        :  0x0 ;

            if ( ( ethioOne == ETHIOPIC_ONE ) && ( ethioTen == 0x0 ) && ( n > 1 ) )
                if ( ( sep == ETHIOPIC_HUNDRED ) || ( (place + 1) == n ) )
                    ethioOne = 0x0;

            if ( ethioTen != 0x0 )
                ethioNumberString += String.valueOf ( ethioTen );
            if ( ethioOne != 0x0 )
                ethioNumberString += String.valueOf ( ethioOne );
            if ( sep != 0x0 )
                ethioNumberString += String.valueOf ( sep );
        }

        return ( ethioNumberString );
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        if(args.length == 0){
            ;
        }
        else if(args[0].equalsIgnoreCase("-f")){
            if(args.length == 1){
                System.err.println("ERROR: missing filename");
                System.exit(1);
            }
            File file = new File(args[1]);
            try{
                in = new Scanner(file);
            }
            catch(FileNotFoundException e){
                System.err.println("ERROR: No such file or directory");
                System.exit(1);
            }
        }
        else{
            in = new Scanner(String.join("\n", args));
        }

        int success = 0;
        while(in.hasNext()){
            try{
                String result = toGeez(in.nextLine());
                System.out.println(result);
            }
            catch(IllegalArgumentException e){
                System.err.println("Not a number");
                success = 1;
            }
        }

        in.close();
        System.exit(success);
    }
}
