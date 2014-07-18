package controle;

// Classe desenvolvida por Lucas e Willian
public class MudaTipo {

    public static TipoDeVariavelEnum getTipoDeVariavelEnum(TipoPreDefinidoEnum tipoPreDefinidoEnum)
    {
        switch(tipoPreDefinidoEnum)
        {
            case BOOLEANO : return TipoDeVariavelEnum.BOOLEANO;
            case CADEIA   : return TipoDeVariavelEnum.CADEIA;
            case CARACTER : return TipoDeVariavelEnum.CARACTER;
            case NUM_INT  : return TipoDeVariavelEnum.INTEIRO;
            case NUM_REAL     : return TipoDeVariavelEnum.REAL;
        }
        return TipoDeVariavelEnum.BOOLEANO;   
    }
    
    public static TipoPreDefinidoEnum getTipoPreDefinido(TipoDeVariavelEnum tipoVariavelDeEnum)
    {
        switch(tipoVariavelDeEnum)
        {
            case BOOLEANO : return TipoPreDefinidoEnum.BOOLEANO;
            case CADEIA   : return TipoPreDefinidoEnum.CADEIA;
            case CARACTER : return TipoPreDefinidoEnum.CARACTER;
            case INTEIRO  : return TipoPreDefinidoEnum.NUM_INT;
            case REAL     : return TipoPreDefinidoEnum.NUM_REAL;
        }
        return TipoPreDefinidoEnum.BOOLEANO;   
    }
}
