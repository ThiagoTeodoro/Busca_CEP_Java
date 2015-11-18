/*
 * Classe que executa a Busca do CEP via Web Site.
 *
 * Essa Classe realiza uma busca de CEP no site : http://www.qualocep.com/, pela
 * URL de pesquisa http://www.qualocep.com/busca-cep/(CEP). 
 * Após enviar o CEP para pesquisa o algoritmo dessa classe trabalha em cima do
 * HTML recebido, procurando as Tag's <title></title> do HTML e retirando os dados que 
 * precisamos. Esse algorítimo é adaptado à realidade do site na data
 * de concepção do algorítimo o que sanguifica que caso o site sofra mudanças de 
 * funcionamento ou de layout implica também na adaptação do algorítimo.
 *
 * Sobre o funcionamento do Algorítimo.
 *
 * O algorítimo realiza uma triagem da String que é recebida. Essa String 
 * recebida nada mais é que o HTML da página. Mediante isso o Algorítimo procura
 * as Tag's <title></title> e retira o conteúdo dentre elas, conteúdo esse
 * que vai possuir os dados de endereço do CEP solicitado. Após obter esse 
 * conteúdo o mesmo é quebrado em um vetor pelo delimitador virgulá (,) pois o 
 * site devolve a Rua,Cidade,UF,CEP em um String separada por vírgula. Depois
 * da separação se tudo ocorrer bem, os dados são colocados nos atributos
 * da classe para que possam ser acessados por meio de acesso GET.
 * Os dados de URL, e Tag's são configurados no construtor da Classe.
 */
package Busca_CEP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Thiago Teodoro Rodrigues
 * @version 1.0 - 17/11/2015
 */
public class Busca_CEP {

    //Atributos da Classe
    private String URL;
    private String Tag_Abertura;
    private String Tag_Fechamento;
    private String RUA;
    private String Cidade;
    private String UF;
    
    /**
     * Construtor da Classe
     * 
     * O Construtor obriga a passagem do CEP a ser pesquisado, e já executa
     * a busca realizando a verificação de sucesso ou fracasso na obtenção
     * dos dados.
     * 
     * @param CEP int -> CEP a ser pesquisado.
     */
    public Busca_CEP(String CEP){ //O CEP tem que ser uma String por que se você 
                                  //receber 0 no incio de um CEP quando coverter
                                  //para int ele vai remover os primeiros 0 até
                                  //encontrar um numero mair que 0 e isso vai 
                                  //ferrar com o CEP impossibilitando a procura 
                                  //de CEP's iniciados com 0. Tem que ser String
                                  //por que se não ceps como 00121121 quando
                                  //convertidos para int se tornaria 121121 
                                  //isso não é um CEP valido, se eu trabalhar
                                  //com string ele não vai fazer isso ele vai
                                  //continuar a ser 00121121. Lembrando que o 
                                  //CEP tem que chegar na classe já sem o 
                                  //separador "-"
        
        //Inicializando Variáveis {Boa prática}
        this.Cidade = "";
        this.RUA = "";
        this.Tag_Abertura = "";
        this.Tag_Fechamento = "";
        this.UF = "";
        this.URL = "";
        
        
        //Setando Configurações de Tag's e URL de pesquisa.
        this.URL = "http://www.qualocep.com/busca-cep";
        this.Tag_Abertura = "<title>";
        this.Tag_Fechamento = "</title>";
        
        //Montando URL para realizar Pesquisa e obter o Arquivo.
        String URL_Pesquisa = this.URL + "/" + CEP;
        
        //Executando Pesquisa
        boolean StatusOperacao = this.Buscar_CEP(URL_Pesquisa);
        
        //Checando Resultado da Pesquisa
        if(StatusOperacao != true) {
            
            //Informando Falha na Operação.
            System.out.println("Erro na obtenção dos dados de endereço do CEP informado.");
            
        }
        
    }
    
    
    /**
     * Método que realiza a pesquisa do CEP no Web Site Configurado no atributo
     * this.URL. 
     * 
     * Esse método vai enviar a solicitação de pesquisa, receber uma página 
     * HTML em um String, triar o conteúdo entre as Tags <title></title>, 
     * separar os dados por vírgula separando os em um vetor contado com o 
     * delimitador (,) e se tudo correr bem vamos armazenar nas variáveis
     * da classe correspondente ao endereço o resultado para que possam 
     * ser acessadas via GET.
     * 
     * 
     * @param Url_Pesquisa
     * @return true or false -> Dependendo do sucesso ou fracasso da operação.
     */
    public boolean Buscar_CEP(String Url_Pesquisa){
        
        //Intanciando Objeto Url_Para_Pesquisar do tipo URL
        URL Url_Para_Pesquisar = null; //Objeto requer inicialização com null, e não da problema por que é feito nul new logo abaixo no Try|Catch
        
        //Colocando a Url_Pesquisa em um Objeto de Tipo URL.
        try {
            
            //Atribuindo Url_Pesquisa para Url_Para_Pesquisar
            Url_Para_Pesquisar = new URL(Url_Pesquisa);
        
        } catch (MalformedURLException ex) {
            
            //Erro ao tentar acessar a Url de pesquisa.
            System.out.println("Erro ao tentar acessar a URL : " + Url_Pesquisa + " . Descrição do erro : " + ex);
            
            //Sinalizando Falha.
            return false;
        }
        
        //Obtendo arquivo HTML com o resultado da Consulta.
            //Instanciando Leitor
            BufferedReader LeitorHTML; 
            
            /*
                Instanciando String para receber Todo o HTML da página.
                o Instanciamento tem que ser feito fora, pois eu não consigo
                acessar de fora do Try|Catch nada que for declarado dentro
                dele, como o processo continua fora do Try tive que declarar
                isso aqui, fora do Try, para poder continuar o processo.
            */
            String PaginaHtml = ""; //Inicializando Váraivel      
            
            /*
                Consultando a Página para obtermos os dados de endereço e
                e armazenando o HTML no BufferedReader LeitorHTML
            */    
            try {

                /*
                    ATENÇÃO!!!
                
                    Você precisa sinalizar o Charset da Página que você está
                    lendo para UTF-8 se não você vai ter um monte de problema
                    referente a acentuação. Por que ele por padrão vai adotar
                    o ISO8859-1 e o Java trabalha com o UTF-8 se você não 
                    falar pro InputStreamReader receber em UTF-8 vai dar pal
                    em toda a acentuação
                */
                LeitorHTML = new BufferedReader(new InputStreamReader(Url_Para_Pesquisar.openStream(), "UTF-8")); 
                
                //Instanciando String para ler cada Linha.
                String Linha = ""; //Inicializando Váriavel
                
                /*
                    Transferindo o conteúdo do BufferedReader LeitorHTML para
                    uma String para que possamos manipula como um todo.
                */
                while ((Linha = LeitorHTML.readLine()) != null) {
                    
                    PaginaHtml = PaginaHtml + Linha;
                    
                }
                
                /*
                    Página Web Obtida com sucesso fechando BufferedReader e
                    iniciando processo de triagem de resultados após o Try|Cath. 
                */
                LeitorHTML.close();
                
            } catch (IOException ex) {
                
                //Erro ao tentar ler a página HTML
                System.out.println("Erro na leitura da página HTML. Descrição do erro : " + ex);                
                
                //Sinalizando Falha
                return false;
                
            }
        //Fim da Obtenção do arquivo HTML com o resultado da Consulta.    
            
            
        //Procurando pela Tag <title> obtendo sua posição de inicio.
        int Posicao_Inicio_Tag_Abertura = PaginaHtml.indexOf(this.Tag_Abertura);
        
        /*
            Adicionando à Posição de Inicio da Tag de Abertura, seu próprio 
            tamanho, para que o ponteiro fique dessa forma no final da
            palavra/tag <title> para que quando formos "Capturar" o contéudo
            entre as Tags a palavra/tag <title> não esteja presente
        */
        Posicao_Inicio_Tag_Abertura = Posicao_Inicio_Tag_Abertura + this.Tag_Abertura.length();
        
        /*
            Procurando pela Tag </title> obtendo sua posição de inicio. Como é
            a posição de inicio de uma tag de fechamento eu não vou precisar
            mover o ponteiro para o fim da tag, pois o contéudo termina 
            exatamente no inicio da tag de fechamento.
        */
        int Posicao_Inicio_Tag_Fechamento = PaginaHtml.indexOf(this.Tag_Fechamento);
        
        
        /*
            Verificando se as Tags de Fechamento e abertura foram encontradas 
            se elas não forem encontras seus valores de de posição vão conter 
            -1
        */
        if((Posicao_Inicio_Tag_Abertura == -1) || (Posicao_Inicio_Tag_Fechamento == -1)) {
            
            /*
                Erro, ou a Tag de abertura ou a Tag de Fechamento não foram 
                encontradas
            */
            System.out.println("Erro, Posição da Tag de Abertura ou da Tag de Fechamento não localizadas, impossível obter os dados de endereço do CEP informado.");
            
            //Sinalizando Falha na operação
            return false;
            
        } else {
            
            /*
                Tags encontradas, "Capturando|Obtendo" da PaginaHTML somente o
                conteúdo entre as Tags.
            
                Aqui diferente lá do PHP, eu não vou informar quantas posições
                na String eu quero "Recortar" a partir de um início, o Java 
                trabalha diferente, o Java pede a posição de início 
                em que ele deve começar a substring, e a posição final
                onde ele vai parar de "Recortar" a substring.
            
                A diferença é que o Java trabalha com posição de início e final.
                Já o PHP trabalha com a posição de início e quantas posições
                à frente daquele inicio ele deve "Recortar" para a substring.
            
                No fim é só a diferença de funcionamento das funções entre as
                duas linguagens.
            */
            String Conteudo_Entre_Tags = PaginaHtml.substring(Posicao_Inicio_Tag_Abertura, Posicao_Inicio_Tag_Fechamento);
            
            /*
                Removendo da Conteúdo_Entre_Tags, a expressão, "Qual o CEP da "
                o Site coloca esse Prefixo em todos os seus resultados, então, 
                antes de quebrar a String pelo separador de Virgulas vou 
                precisar remover essa expressão da própria String para não 
                ter "Lixo" no resultado.
            */
            /*
                Veja estou substituindo por nada, o que ele encontrar 
                "Qual o CEP da " repare que existem um espaço no final de "da "
                para já deixar formatadinho o nome da rua.
            */
            Conteudo_Entre_Tags = Conteudo_Entre_Tags.replace("Qual o CEP da", "");
            
            /*
                Quebrando o conteúdo da String, em um vetor, realizando a 
                separação pelo carácter delimitador virgula (,)
            */
            String[] DadosEndereco = Conteudo_Entre_Tags.split(","); //A Declaração de Vetor é assim mesmo Tipo[], ou seja o tipo e abre e fecha coxete
            
            /*
                Checando se os Dados de Endereço foram obtidos corretamente, 
                a ideia aqui é a seguinte:
                
                Se os dados forem obtidos corretamente, o vetor do tipo String
                deve conter 4 posições. Qualquer coisa diferente disso, 
                indica que os dados não foram obtidos de maneira correta, ou
                então o CEP não existe.
                
                Por tanto, se o vetor for diferente de 4 posições, eu vou emitir
                uma mensagem de erro indicando que houve uma falha na separação
                da String em Vetor, confira o CEP informado e tente novamente.
            */
            if(DadosEndereco.length != 4) {
                
                /*
                    Vetor resultante diferente de 4 posições Erro na obtenção dos 
                    dados
                */
                System.out.println("Houve uma falha na separação da String obtida entre as Tags, o CEP pode estar invalido.\nRedigite o CEP e tente novamente, caso o erro continue, digite os dados manualmente.");
                
                //Retornando False indicando falha na operação
                return false;
                
            } else {
                
                /*
                    Vetor separado corretamente, populando os atributos da 
                    classe para que possam ser acessados por método get
                */
                
                //Populando RUA
                     /*
                        O Primeiro Carácter depois da Virgula é um espaço então 
                        eu vou fazer um substring aqui pulando a posição 0 
                        ou seja pulando o espaço e ir até o final da palavra
                        que o próprio tamanho da palavra. Para pular a 
                        posição 0 eu vou começar na posição 1 = )
                    */
                    this.RUA = DadosEndereco[0].substring(1, DadosEndereco[0].length());;
                
                //Populando Cidade 
                    /*
                        O Primeiro Carácter depois da Virgula é um espaço então 
                        eu vou fazer um substring aqui pulando a posição 0 
                        ou seja pulando o espaço e ir até o final da palavra
                        que o próprio tamanho da palavra. Para pular a 
                        posição 0 eu vou começar na posição 1 = )
                    */
                    this.Cidade = DadosEndereco[1].substring(1, DadosEndereco[1].length());
                
                //Populando UF
                    /*
                        O Primeiro Carácter depois da Virgula é um espaço então 
                        eu vou fazer um substring aqui pulando a posição 0 
                        ou seja pulando o espaço e ir até o final da palavra
                        que o próprio tamanho da palavra. Para pular a 
                        posição 0 eu vou começar na posição 1 = )
                    */
                    this.UF = DadosEndereco[2].substring(1, DadosEndereco[2].length());;
                
                /*
                    Não vou pegar a quarta posição pois a quarta posição é o
                    proprio CEP como eu informo ele e ele é retornado, é 
                    desnecessário eu armazenar ele, apesar dele existir no 
                    vetor String.
                */
                
                //Processo terminado com sucesso. Sinalizando Sucesso
                return true;
                
            }
            
        }
        
    }

    //Métodos GET de Acesso a RUA, Cidade e UF

    /**
     * @return the RUA
     */
    public String getRUA() {
        return RUA;
    }

    /**
     * @return the Cidade
     */
    public String getCidade() {
        return Cidade;
    }

    /**
     * @return the UF
     */
    public String getUF() {
        return UF;
    }

}
