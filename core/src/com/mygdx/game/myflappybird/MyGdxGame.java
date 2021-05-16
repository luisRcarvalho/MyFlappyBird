package com.mygdx.game.myflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
    private Texture canoSuperior;     //imagem do cano superior
    private Texture canoInferior;    //imagem do cano inferior

    private int pontos = 0;

	//Movimento
	private int movimentaY = 0;
	private int movimentaX = 0;

	//Tela
	private float larguraDispositivo;//float que recebe a largura do dispositivo
	private float alturaDispositivo;//float que recebe a altura do dispositivo
    private float posicaoCanoHorizontal;
    private float espaçoEntreCanos; //float para calcular o espaço entre os canos
    private float posicaoCanoVertical;

    private Random random; //usado para os canos

    private float variacao = 0; //variação da altura para a animação
	private float gravidade = 0;//float da gravidade para que o passarinho caia
	private float posicaoInicialVerticalPassaro = 0; // posiçao que o passaro

    BitmapFont textoPontuacao;
    private boolean passouCano = false;

    private ShapeRenderer shapeRenderer;
    private Circle circulopassaro;
    private Rectangle retanguloCanoSuperior;
    private Rectangle retanguloCanoInferior;

	//metodo que instancia os objetos na tela
	@Override
	public void create () {
        inicializaImagens();
        inicializaTela();
    }

    private void inicializaTela() {
        batch = new SpriteBatch();

        larguraDispositivo = Gdx.graphics.getWidth();//pega a largura do dispositivo
        alturaDispositivo = Gdx.graphics.getHeight();//pega a altura do dispositivo
        posicaoInicialVerticalPassaro = alturaDispositivo/2; // posiciona o passaro no meio da tela
        posicaoCanoHorizontal = larguraDispositivo;           //posiciona o cano
        espaçoEntreCanos = 350;                               //define o espaço entre os canos

        textoPontuacao = new BitmapFont();
        textoPontuacao.setColor( Color.WHITE);
        textoPontuacao.getData().setScale(10);
    }

    private void inicializaImagens() {

        random = new Random();

        passaros = new Texture[3];
        passaros[0] = new Texture("passaro1.png");//define no elemento 0 qual é a imagem do passaro
        passaros[1] = new Texture("passaro2.png");//define no elemento 1 qual é a imagem do passaro
        passaros[2] = new Texture("passaro3.png");//define no elemento 2 qual é a imagem do

        fundo = new Texture("fundo.png" );//instancia a imagem de background

        canoSuperior = new Texture("cano_topo_maior.png");    //instancia o cano superior
        canoInferior = new Texture("cano_baixo_maior.png");  //instancia o cano inferior
    }


    //metodo que imprime a interface de layout da aplicação
	@Override
	public void render () {
        gameplay();
        validarPontos();
        desenhaImagens();
        detectarColisao();
    }

    private void detectarColisao() {

        //criação dos colliders
        circulopassaro.set(50 + passaros[0].getWidth() / 2, posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

        retanguloCanoSuperior.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoSuperior.getHeight() - espaçoEntreCanos/ 2 + posicaoCanoVertical, canoSuperior.getWidth(), canoSuperior.getHeight() );

        retanguloCanoInferior.set(posicaoCanoHorizontal, alturaDispositivo / 2 - canoInferior.getHeight() - espaçoEntreCanos / 2 + posicaoCanoVertical, canoInferior.getWidth(), canoInferior.getHeight());

        //bool que vai detectar as colisões
        boolean colisaoCanoSuperior = Intersector.overlaps(circulopassaro, retanguloCanoSuperior);
        boolean colisaoCanoInferior = Intersector.overlaps(circulopassaro, retanguloCanoInferior);

        //avisa se o passaro colidiu
        if (colisaoCanoInferior || colisaoCanoSuperior)
        {
            Gdx.app.log("Log", "Bateu");
        }

    }
    private void gameplay() {

        posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;//faz os canos virem em direção do player
        if(posicaoCanoHorizontal < - canoInferior.getWidth()){
            posicaoCanoHorizontal = larguraDispositivo;
            posicaoCanoHorizontal = random.nextInt(400)-200;
            passouCano = false;
        }
        boolean toqueTela = Gdx.input.justTouched();//verifica se a tela foi tocada
        if(Gdx.input.justTouched()){//se a tela é tocada, o passarinho vai pra cima
            gravidade = -25;
        }
        if(posicaoInicialVerticalPassaro > 0 || toqueTela) //diminui a gravidade
            posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
        variacao += Gdx.graphics.getDeltaTime() * 10; //associa os graficos do gdx e associa com a variação

        if(variacao > 3)//altera a variação para que a animação mude
            variacao = 0;

        gravidade++;//adciona gravidade
        movimentaX++;//adiciona o movimento no X
    }

    private void desenhaImagens() {
        batch.begin();//inicia a execução
        batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);//instancia o fundo, usando os parametros criados antes como altura e largura
        batch.draw(passaros[(int) variacao],50, posicaoInicialVerticalPassaro);//instancia o passarinho

        batch.draw( canoInferior, posicaoCanoHorizontal , alturaDispositivo/2 - canoInferior.getHeight() - espaçoEntreCanos/2 + posicaoCanoVertical);  //instancia o cano na tela com espaço entre eles
        batch.draw( canoSuperior, posicaoCanoHorizontal ,alturaDispositivo/2 + espaçoEntreCanos/2 + posicaoCanoVertical);                             //instancia o cano na tela com espaço entre eles

        textoPontuacao.draw( batch, String.valueOf( pontos ),larguraDispositivo /2, alturaDispositivo - 100 );

        batch.end();//termina a execução
    }

    private void validarPontos() {
        if (posicaoCanoVertical < 50 - passaros[0].getWidth())
        {
            if (!passouCano){
                pontos++;
                passouCano = true;
            }
        }
    }

    //metodo que retorna os dados na aplicação
	@Override
	public void dispose () {

	}
}
