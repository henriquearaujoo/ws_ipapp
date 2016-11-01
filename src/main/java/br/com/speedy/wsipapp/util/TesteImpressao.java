package br.com.speedy.wsipapp.util;

/**
 * Created by Henrique on 21/08/2015.
 */
public class TesteImpressao {

    public static void main (String args[]){

        char[] cutP = new char[]{0x1d, 'V', 1};

        String conteudo = "  IRANDUBA PESCADOS \n" +
                "\n" +
                "COMPROVANTE DE COMPRA \n" +
                "\n" +
                "DATA: 21/08/2015 11:10:07 \n" +
                "CODIGO: 180815024242 \n" +
                "USUARIO: Silvano Gaio de Castro \n" +
                "FORNECEDOR: GEDALVO FALCÃO DE AZEVEDO JUNIOR \n" +
                "TRANSPORTE: Bau \n" +
                "ITENS: (1) \n" +
                "----------------------------------------\n" +
                "SURUBIM A S/C  935KG - 10 = 915KG\n" +
                "\n" +
                "TOTAIS----------------------------------\n" +
                "TOTAL P. BRUTO: 935KG \n" +
                "TOTAL P. LIQUIDO: 915KG \n" +
                "TOTAL DESCONTOS: 0KG \n" +
                "TOTAL CAIXAS: 10 \n" +
                "----------------------------------------\n" +
                " \n" +
                " \n" +
                " \n" +
                " \n" +
                " \n" ;

        conteudo += "\n \n \n \n \n \n" + new String(cutP);

        try{
            new ImpressaoUtil("epson").imprimir(conteudo);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
