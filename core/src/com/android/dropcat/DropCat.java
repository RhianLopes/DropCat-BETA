package com.android.dropcat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import java.util.Random;

public class DropCat extends ApplicationAdapter {

	private SpriteBatch batch;
	private Texture[] gatos;
	private Texture funda;
	private Texture nuvemdireita;
	private Texture nuvemesqueda;
	private Random random;
	private BitmapFont fonte;
	private BitmapFont gameover;
	private BitmapFont iniciar;
	private BitmapFont reiniciar;
	private Circle gatoCirculo;
	private Rectangle nuvemDireita;
	private Rectangle nuvemEsquerda;
	private Rectangle nuvemDireita2;
	private Rectangle nuvemEsquerda2;
	//private ShapeRenderer shape;

	private float LarguraDispositivo;
	private float AlturaDispositivo;
	private int estadoNuvem = 0;
	private int estadoJogo=0;
	private int pontuacao=0;

	private float v = 0;
	private float velocidadeQueda = 0;
	private float posicaoinicialHorizontal;
	private float posicaoinicialVertical;
	private float posicaoHorizontalNuvemEsqueda;
	private float posicaoHorizontalNuvemDireita;
	private float espacoEntreNuvens;
	private float deltaTime;
	private float posicaoDaNuvemVertical;
	private float posicaoDaNuvemVertical2;
	private float DistanciaLoka;
	private float DistanciaLoka2;
	private boolean MarcouPonto=false;

	//camera
	private OrthographicCamera camera;
	private Viewport viewport;
	private final float VIRTUAL_WIDTH = 500;
	private final float VIRTUAL_HEIGHT = 950;


	@Override
	public void create() {

		batch = new SpriteBatch();
		random = new Random();
		gatoCirculo = new Circle();
		nuvemEsquerda = new Rectangle();
		nuvemDireita = new Rectangle();
		nuvemDireita2 = new Rectangle();
		nuvemEsquerda2 = new Rectangle();
		//shape = new ShapeRenderer();
		reiniciar = new BitmapFont();
		reiniciar.setColor(Color.WHITE);
		reiniciar.getData().setScale(3);
		iniciar = new BitmapFont();
		iniciar.setColor(Color.WHITE);
		iniciar.getData().setScale(3);
		gameover = new BitmapFont();
		gameover.setColor(Color.WHITE);
		gameover.getData().setScale(5);
		fonte = new BitmapFont();
		fonte.setColor(Color.WHITE);
		fonte.getData().setScale(5);
		gatos = new Texture[4];
		gatos[0]= new Texture("gato.png");
		gatos[1]= new Texture("gatoo.png");
		gatos[2]= new Texture("gatooo.png");
		gatos[3]= new Texture("gatoooo.png");
		funda = new Texture("fundotesteoficial.png");
		nuvemdireita =  new Texture("nuvemdireita.png");
		nuvemesqueda = new Texture("nuvemesquerda.png");

		//confguracoes da camera
		camera = new OrthographicCamera();
		camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
		viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);


		LarguraDispositivo = VIRTUAL_WIDTH;
		AlturaDispositivo = VIRTUAL_HEIGHT;

		espacoEntreNuvens = 105;
		posicaoinicialHorizontal = 30;
		posicaoHorizontalNuvemEsqueda = (LarguraDispositivo/2)-(LarguraDispositivo/2)-(LarguraDispositivo/3)-(LarguraDispositivo/8);
		posicaoHorizontalNuvemDireita = LarguraDispositivo/2;
		posicaoDaNuvemVertical = AlturaDispositivo-AlturaDispositivo-100;
		posicaoDaNuvemVertical2 = AlturaDispositivo-AlturaDispositivo-100;

		posicaoinicialVertical = AlturaDispositivo-AlturaDispositivo/5;

	}

	@Override
	public void render() {

		camera.update();


		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);


		//Variacao que deixa o gato girando com o jogo não inciado
		deltaTime = Gdx.graphics.getDeltaTime();
		v += deltaTime * 10;
		if (v > 3) v = 0;

		if(estadoJogo==0){
			if (Gdx.input.justTouched()){
				estadoJogo = 1;
			}
		}else {

			velocidadeQueda += 1;

			///////////////////Limites do gato///////////////////////////////
			/////////////////////////////////////////////////////////////////
			if (posicaoinicialHorizontal < LarguraDispositivo - 10) {
				posicaoinicialHorizontal += velocidadeQueda;
			}

			if (posicaoinicialHorizontal < LarguraDispositivo - LarguraDispositivo + 10) {
				posicaoinicialHorizontal -= velocidadeQueda;
			}

			if (posicaoinicialHorizontal > LarguraDispositivo - 100) {
				posicaoinicialHorizontal -= velocidadeQueda;
			}
			///////////////////Limites do gato///////////////////////////////
			/////////////////////////////////////////////////////////////////

			if(estadoJogo==1){

				posicaoDaNuvemVertical += deltaTime * 300;
				if (estadoNuvem == 1) {
					posicaoDaNuvemVertical2 += deltaTime * 300;
				}
				if (Gdx.input.justTouched()) {
					velocidadeQueda = -12;
				}


				///////////////////////////Nuvens/////////////////////////////////
				//////////////////////////////////////////////////////////////////
				if (posicaoDaNuvemVertical > AlturaDispositivo + 100) {
					posicaoDaNuvemVertical = AlturaDispositivo - AlturaDispositivo - 100;
					DistanciaLoka = random.nextInt(130) - 100;
					MarcouPonto=false;
				}
				if (posicaoDaNuvemVertical > AlturaDispositivo / 2) {
					estadoNuvem = 1;
				}
				if (posicaoDaNuvemVertical2 > AlturaDispositivo + 100) {
					posicaoDaNuvemVertical2 = AlturaDispositivo - AlturaDispositivo - 100;
					DistanciaLoka2 = random.nextInt(130) - 30;
					MarcouPonto=false;
				}
				///////////////////////////Nuvens/////////////////////////////////
				//////////////////////////////////////////////////////////////////


				///////////////////////////Pontuacao///////////////////////////////
				///////////////////////////////////////////////////////////////////
				if(posicaoDaNuvemVertical>posicaoinicialVertical||posicaoDaNuvemVertical2>posicaoinicialVertical){
					if(!MarcouPonto) {
						pontuacao++;
						MarcouPonto=true;
					}
				}
			}else{
				//////////////////////////// Tela de game over////////////////////////////

				if(Gdx.input.justTouched()){

					estadoJogo=0;
					pontuacao=0;
					velocidadeQueda=0;
					posicaoinicialVertical = AlturaDispositivo-AlturaDispositivo/5;
					posicaoDaNuvemVertical = AlturaDispositivo-AlturaDispositivo-100;
					posicaoDaNuvemVertical2 = AlturaDispositivo-AlturaDispositivo-100;
					espacoEntreNuvens = 100;
					posicaoinicialHorizontal = 30;
					posicaoHorizontalNuvemEsqueda = (LarguraDispositivo/2)-(LarguraDispositivo/2)-(LarguraDispositivo/3)-(LarguraDispositivo/8);
					posicaoHorizontalNuvemDireita = LarguraDispositivo/2;
					estadoNuvem = 0;
				}


			}

		}
		//////////////configurar dados de projeçao da camera////////////////
		batch.setProjectionMatrix(camera.combined);
		////////////////////////////////////////////////////////////////////
		batch.begin();

		batch.draw(funda, LarguraDispositivo-LarguraDispositivo, AlturaDispositivo-AlturaDispositivo);
		if(estadoJogo==0){
			iniciar.draw(batch, "Toque para Jogar!", LarguraDispositivo/6, AlturaDispositivo/2);
		}
		batch.draw( nuvemesqueda, posicaoHorizontalNuvemEsqueda-espacoEntreNuvens+DistanciaLoka ,posicaoDaNuvemVertical);
		batch.draw( nuvemdireita, posicaoHorizontalNuvemDireita+espacoEntreNuvens+DistanciaLoka, posicaoDaNuvemVertical);
		if(estadoNuvem == 1) {
			batch.draw(nuvemesqueda, posicaoHorizontalNuvemEsqueda - espacoEntreNuvens+DistanciaLoka2 , posicaoDaNuvemVertical2);
			batch.draw(nuvemdireita, posicaoHorizontalNuvemDireita + espacoEntreNuvens+DistanciaLoka2 , posicaoDaNuvemVertical2);
		}
		batch.draw(gatos[(int)v], posicaoinicialHorizontal, posicaoinicialVertical);
		fonte.draw(batch, String.valueOf(pontuacao), LarguraDispositivo/10, AlturaDispositivo-20);
		if(estadoJogo==2){
			gameover.draw(batch, "GAME OVER", LarguraDispositivo/14, AlturaDispositivo/2+AlturaDispositivo/8);
			reiniciar.draw(batch, "Toque para Reiniciar!", LarguraDispositivo/10, AlturaDispositivo/2);

		}

		batch.end();

		//////////////////Colisao//////////////////
		///////////////////////////////////////////

		gatoCirculo.set(posicaoinicialHorizontal+gatos[3].getWidth()/2, posicaoinicialVertical+gatos[3].getHeight()/2, gatos[0].getHeight()/5);
		nuvemDireita.set(posicaoHorizontalNuvemDireita+espacoEntreNuvens+DistanciaLoka, posicaoDaNuvemVertical, nuvemdireita.getWidth(), 25);
		nuvemEsquerda.set(posicaoHorizontalNuvemEsqueda-espacoEntreNuvens+DistanciaLoka, posicaoDaNuvemVertical, nuvemesqueda.getWidth(), 25);
		nuvemDireita2.set(posicaoHorizontalNuvemDireita+espacoEntreNuvens+DistanciaLoka2, posicaoDaNuvemVertical2, nuvemdireita.getWidth(), 25);
		nuvemEsquerda2.set(posicaoHorizontalNuvemEsqueda-espacoEntreNuvens+DistanciaLoka2, posicaoDaNuvemVertical2, nuvemesqueda.getWidth(), 25);

        /*shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.circle(gatoCirculo.x, gatoCirculo.y , gatoCirculo.radius);
        shape.rect(nuvemDireita.x,nuvemDireita.y,nuvemDireita.width, nuvemDireita.height);
        shape.rect(nuvemEsquerda.x, nuvemEsquerda.y, nuvemEsquerda.width, nuvemEsquerda.height);
        shape.rect(nuvemDireita2.x,nuvemDireita2.y,nuvemDireita2.width, nuvemDireita2.height);
        shape.rect(nuvemEsquerda2.x, nuvemEsquerda2.y, nuvemEsquerda2.width, nuvemEsquerda2.height);


        shape.setColor(Color.RED);

        shape.end();
*/
		if(Intersector.overlaps(gatoCirculo, nuvemDireita) || Intersector.overlaps(gatoCirculo, nuvemEsquerda)|| Intersector.overlaps(gatoCirculo,nuvemDireita2)|| Intersector.overlaps(gatoCirculo, nuvemEsquerda2)){
			estadoJogo=2;
		}

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}
}