package com.mygdx.game.myflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {

	//Construção
	private SpriteBatch batch;
	private Texture[] passaros; //array com os sprites do passaro
	private Texture fundo; //imagem do background

	//Movimento
	private int movimentaY = 0;
	private int movimentaX = 0;

	//Tela
	private float larguraDispositivo;//float que recebe a largura do dispositivo
	private float alturaDispositivo;//float que recebe a altura do dispositivo

	private float variacao = 0; //variação da altura para a animação
	private float gravidade = 0;//float da gravidade para que o passarinho caia
	private float posicaoInicialVerticalPassaro = 0; // posiçao que o passaro

	//metodo que instancia os objetos na tela
	@Override
	public void create () {

		batch = new SpriteBatch();
		passaros = new Texture[3];
        passaros[0] = new Texture("passaro1.png");//define no elemento 0 qual é a imagem do passaro
        passaros[1] = new Texture("passaro2.png");//define no elemento 1 qual é a imagem do passaro
        passaros[2] = new Texture("passaro3.png");//define no elemento 2 qual é a imagem do passaro

		fundo = new Texture("fundo.png" );//instancia a imagem de background

		larguraDispositivo = Gdx.graphics.getWidth();//pega a largura do dispositivo
		alturaDispositivo = Gdx.graphics.getHeight();//pega a altura do dispositivo
		posicaoInicialVerticalPassaro = alturaDispositivo/2; // posiciona o passaro no meio da tela

	}
//metodo que imprime a interface de layout da aplicação
	@Override
	public void render () {

		batch.begin();//inicia a execução
		if(variacao > 3)//altera a variação para que a animação mude
		    variacao = 0;

		boolean toqueTela = Gdx.input.justTouched();//verifica se a tela foi tocada

		if(Gdx.input.justTouched()){//se a tela é tocada, o passarinho vai pra cima
		    gravidade = -25;
        }
		if(posicaoInicialVerticalPassaro > 0 || toqueTela) //diminui a gravidade
		    posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;

		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);//instancia o fundo, usando os parametros criados antes como altura e largura
		batch.draw(passaros[(int) variacao],30, posicaoInicialVerticalPassaro);//instancia o passarinho

		variacao += Gdx.graphics.getDeltaTime() * 10; //associa os graficos do gdx e associa com a variação

		gravidade++;//adciona gravidade
		movimentaX++;//adiciona o movimento no X
		movimentaY++;//adiciona o movimento no Y

		batch.end();//termina a execução
	}

	//metodo que retorna os dados na aplicação
	@Override
	public void dispose () {

	}
}
