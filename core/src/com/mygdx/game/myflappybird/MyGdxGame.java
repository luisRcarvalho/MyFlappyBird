package com.mygdx.game.myflappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class MyGdxGame extends ApplicationAdapter {

	//Construção
	private SpriteBatch batch;
	private Texture passaro;
	private Texture fundo;

	//Movimento
	private int movimentaY = 0;
	private int movimentaX = 0;

	//Tela
	private float larguraDispositivo;
	private float alturaDispositivo;

	//metodo que instancia os objetos na tela
	@Override
	public void create () {

		batch = new SpriteBatch();
		passaro = new Texture( "passaro1.png");
		fundo = new Texture("fundo.png" );

		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();

	}
//metodo que imprime a interface de layout da aplicação
	@Override
	public void render () {

		batch.begin();

		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);
		batch.draw(passaro,50,50,movimentaX,movimentaY);

		movimentaX++;
		movimentaY++;

		batch.end();
	}

	//metodo que retorna os dados na aplicação
	@Override
	public void dispose () {

	}
}
