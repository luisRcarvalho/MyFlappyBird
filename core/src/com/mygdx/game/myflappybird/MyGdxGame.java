package com.mygdx.game.myflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {

	//Construção
	private SpriteBatch batch;
	private Texture[] passaros; //array com os sprites do passaro
	private Texture fundo; //imagem do background
    private Texture canoSuperior;//imagem do cano superior
    private Texture canoInferior;//imagem do cano inferior
    private Texture gameOver; //imagem de game over

    private int pontuacaoMaxima = 0; //int que guarda a pontuação maxima obtida
    private int pontos = 0; //int de pontos na partida
    private int estadoJogo = 0;//altera os estados do jogo

	//Tela
	private float larguraDispositivo; //float que recebe a largura do dispositivo
	private float alturaDispositivo; //float que recebe a altura do dispositivo

    private float posicaoCanoHorizontal = 0;
    private float posicaoCanoVertical;
    private float espaçoEntreCanos; //float para calcular o espaço entre os canos

    private Random random; //usado para randomizar os canos

    private float variacao = 0; //variação da altura para a animação
	private float gravidade = 0;//float da gravidade para que o passarinho caia
	private float posicaoInicialVerticalPassaro = 0; // posiçao que o passaro
    private float posicaoHorizontalPassaro = 0; //posição horizontal do passaro

    //variaveis com os textos
    BitmapFont textoPontuacao; //texto de pontuação
    BitmapFont textoReiniciar; //texto de reinicio
    BitmapFont textoMelhorPontuacao; //texto da melhor pontuação

    //variaveis dos sons do jogo
    Sound somColisao;
    Sound somVoar;
    Sound somPontos;

    private boolean passouCano = false; //bool que verifica se passou pelo cano

    //Renderização e criação dos colisores
    private ShapeRenderer shapeRenderer;
    private Circle circulopassaro;
    private Rectangle retanguloCanoSuperior;
    private Rectangle retanguloCanoInferior;

    Preferences preferencias;

	//metodo que instancia os objetos na tela
	@Override
	public void create () {
        inicializaImagens();
        inicializaTela();
    }
    //metodo que imprime a interface de layout da aplicação
    @Override
    public void render () {
        verificarEstadoJogo();
        validarPontos();
        desenhaImagens();
        detectarColisao();
    }


    private void inicializaTela() {
        batch = new SpriteBatch();

        random = new Random();

        larguraDispositivo = Gdx.graphics.getWidth();//pega a largura do dispositivo
        alturaDispositivo = Gdx.graphics.getHeight();//pega a altura do dispositivo
        posicaoInicialVerticalPassaro = alturaDispositivo/2; // posiciona o passaro no meio da tela
        posicaoCanoHorizontal = larguraDispositivo; //posiciona o cano
        espaçoEntreCanos = 350;//define o espaço entre os canos


        //renderizações dos textos, cada um com uma cor e scale diferente
        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor( Color.WHITE);
        textoPontuacao.getData().setScale(10);

        textoMelhorPontuacao = new BitmapFont();
        textoMelhorPontuacao.setColor(Color.RED);
        textoMelhorPontuacao.getData().setScale(2);

        textoReiniciar = new BitmapFont();
        textoReiniciar.setColor(Color.GREEN);
        textoReiniciar.getData().setScale(2);

        //inicializa e cria os colliders
        shapeRenderer = new ShapeRenderer();
        circulopassaro = new Circle();
        retanguloCanoSuperior = new Rectangle();
        retanguloCanoInferior = new Rectangle();

        //renderiza os sons
        somVoar = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
        somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
        somPontos = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));

        //salva as prefs e pontuação maxima atingida
        preferencias = Gdx.app.getPreferences("flappyBird");
        pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);
    }

    private void inicializaImagens() {

        passaros = new Texture[3];
        passaros[0] = new Texture("passaro1.png");//define no elemento 0 qual é a imagem do passaro
        passaros[1] = new Texture("passaro2.png");//define no elemento 1 qual é a imagem do passaro
        passaros[2] = new Texture("passaro3.png");//define no elemento 2 qual é a imagem do passaro

        fundo = new Texture("fundo.png" );//instancia a imagem de background
        canoSuperior = new Texture("cano_topo_maior.png");    //instancia o cano superior
        canoInferior = new Texture("cano_baixo_maior.png");  //instancia o cano inferior
        gameOver = new Texture("game_over.png"); //instancia a imagem de game over
    }

    private void detectarColisao(){

        //criação dos colliders
        circulopassaro.set(50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

        retanguloCanoSuperior.set(posicaoCanoHorizontal, alturaDispositivo / 2 + espaçoEntreCanos/ 2 + posicaoCanoVertical, canoSuperior.getWidth(), canoSuperior.getHeight() );
        retanguloCanoInferior.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoInferior.getHeight() - espaçoEntreCanos / 2 + posicaoCanoVertical, canoInferior.getWidth(), canoInferior.getHeight());

        //bool que vai detectar as colisões
        boolean colisaoCanoSuperior = Intersector.overlaps(circulopassaro, retanguloCanoSuperior);
        boolean colisaoCanoInferior = Intersector.overlaps(circulopassaro, retanguloCanoInferior);

        //avisa se o passaro colidiu
        if (colisaoCanoInferior || colisaoCanoSuperior)
        {
            if(estadoJogo ==1){
                somColisao.play();
                estadoJogo = 2;
            }
        }

    }
    private void verificarEstadoJogo() {

        boolean toqueTela = Gdx.input.justTouched();//verifica se a tela foi tocada

        if (estadoJogo == 0) {//se o estado do jogo for 0, o jogo ainda esta no estado inicial e nao começou ainda
            if (Gdx.input.justTouched())//se a tela é tocada, o passarinho vai pra cima e faz o som de voo
            {
                gravidade = -20;
                estadoJogo = 1;
                somVoar.play();
            }
        } else if (estadoJogo == 1) {//Se estado for igual a 1 o jogo começa
            if (Gdx.input.justTouched()) {//se a tela é tocada, o passarinho vai pra cima e faz o som de voo
                gravidade = -20;
                somVoar.play();
            }

        posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;//faz os canos virem em direção do player

        if (posicaoCanoHorizontal < -canoInferior.getWidth()) { //pega a largura da tela para fazer ela avançar quando terminar
            posicaoCanoHorizontal = larguraDispositivo;
            posicaoCanoHorizontal = random.nextInt( 900 ) - 50;//randomiza o espaçamento
            passouCano = false; // e define como falsa a booleana
        }

        if (posicaoInicialVerticalPassaro > 0 || toqueTela)//diminui a gravidade
            posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
            gravidade++;
        } else if (estadoJogo == 2) {//se o estado do jogo for 2, significa que o player perdeu
            if (pontos > pontuacaoMaxima) {//se a pontuação na partida for maior que a pontuação maxima feita em outras tentivas
                pontuacaoMaxima = pontos; // a pontuação maxima vira essa nova pontuação
                preferencias.putInteger( "pontuacaoMaxima", pontuacaoMaxima );//salva a pontuação
            }
            posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500;//quando o passaro colide ele volta para tras

            if (toqueTela) {//se tocar na tela
                estadoJogo = 0;//o estado do jogo volta para o inicial
                pontos = 0;//os pontos voltaram para zero
                gravidade = 0;//a gravidade fica zerada
                posicaoHorizontalPassaro = 0;// reseta a posição do passaro
                posicaoInicialVerticalPassaro = alturaDispositivo / 2;// e o passaro é instanciado no ponto inicial
                posicaoCanoHorizontal = larguraDispositivo; // e define a posição dos canos
            }
        }
    }
    private void desenhaImagens() {
        batch.begin();//inicia a execução

        batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);//instancia o fundo, usando os parametros criados antes como altura e largura
        batch.draw(passaros[(int) variacao],50 + posicaoHorizontalPassaro, posicaoInicialVerticalPassaro);//instancia o passarinho

        batch.draw( canoInferior, posicaoCanoHorizontal , alturaDispositivo/2 - canoInferior.getHeight() - espaçoEntreCanos/2 + posicaoCanoVertical);//instancia o cano na tela com espaço entre eles
        batch.draw( canoSuperior, posicaoCanoHorizontal ,alturaDispositivo/2 + espaçoEntreCanos/2 + posicaoCanoVertical);//instancia o cano na tela com espaço entre eles

        textoPontuacao.draw( batch, String.valueOf( pontos ),larguraDispositivo /2, alturaDispositivo - 100 );//desenha a pontuação no topo da tela

        if(estadoJogo == 2)//se o estado for 2, é game over, onde se instancia as frases de game over, reinicio e melhor pontuação
        {
            batch.draw(gameOver, larguraDispositivo / 2 +200 - gameOver.getWidth(), alturaDispositivo / 2);
            textoReiniciar.draw(batch, "TOQUE NA TELA PARA REINICIAR", larguraDispositivo / 2 - 250, alturaDispositivo /2 - gameOver.getHeight() / 2);
            textoMelhorPontuacao.draw(batch, "SUA MELHOR PONTUAÇÃO É: " + pontuacaoMaxima +" Pontos", larguraDispositivo /2 - 250, alturaDispositivo /2 - gameOver.getHeight() * 2);
        }

        batch.end();//termina a execução
    }

    private void validarPontos() {

        if (posicaoCanoHorizontal < 50 - passaros[0].getWidth())
        {
            if (!passouCano){//quando passa por um cano, soma 1 ponto
                pontos++;
                passouCano = true;
                somPontos.play();
            }
        }
        variacao += Gdx.graphics.getDeltaTime() * 10; //associa os graficos do gdx e associa com a variação
        if(variacao > 3)//altera a variação para que a animação mude
            variacao = 0;
    }

    //metodo que retorna os dados na aplicação
	@Override
	public void dispose () {

	}
}
